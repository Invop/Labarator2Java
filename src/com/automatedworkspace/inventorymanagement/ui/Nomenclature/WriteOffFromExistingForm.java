package com.automatedworkspace.inventorymanagement.ui.Nomenclature;

import com.automatedworkspace.inventorymanagement.statistics.Config;
import com.automatedworkspace.inventorymanagement.statistics.ConfigManager;
import com.automatedworkspace.inventorymanagement.statistics.DeliveryConfig;
import com.automatedworkspace.inventorymanagement.ui.InventoryManagementUI;
import com.toedter.calendar.JDateChooser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.automatedworkspace.inventorymanagement.ui.AddItem.AddItemForm.EXEL_FILE_PATH;

public class WriteOffFromExistingForm extends JDialog{
	private JPanel PanelWriteOffForm;
	private JLabel LabelChooseForm;
	private JComboBox<String> ChooseComboBox;
	private JLabel LabelNumber;
	private JTextField NumberField;
	private JLabel IntervalLabel;
	private JButton OKButton;
	private JButton CancelButton;
	private JPanel DatePanel;
	private JDateChooser DateChooser = new JDateChooser();

	public WriteOffFromExistingForm(JFrame parent) {
		super(parent);
		setVisible(true);
		setSize(400, 250);
		setContentPane(PanelWriteOffForm);
		setLocationRelativeTo(parent);
		FieldsThatOnlyHandleNumbers();
		OKButton.setEnabled(false);
		DateChooser.setMinSelectableDate(new Date()); // Set the minimum date to today's date
		//calendar
		DatePanel.add(DateChooser);
		try {
			GetItemNames();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		Listener();
		IfOkPressed();
		IfCancelPressed();
		CloseApp();

	}

	private void Listener() {
		DocumentListener documentListener = new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				checkFields();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				checkFields();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				checkFields();
			}
		};
		NumberField.getDocument().addDocumentListener(documentListener);
		ChooseComboBox.addActionListener((e) -> checkFields());
		OKButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// remove document listener from each text field
				NumberField.getDocument().removeDocumentListener(documentListener);
			}
		});
	}
	private void checkFields() {
		boolean number = !NumberField.getText().isEmpty();
		boolean comboBox = ChooseComboBox.getSelectedIndex() != -1;
		boolean dateSelected = DateChooser.getDate() != null; // check if a date is selected in the DateChooser
		OKButton.setEnabled(number && comboBox && dateSelected);
	}

	private void GetItemNames() throws IOException {
		// Read the config file
		Config config = ConfigManager.readConfig();
		// Check if the name already exists in the config file
		List<String> nameList = config.getNamesList();

		if (nameList.isEmpty()) {
			// No items in the list
			JOptionPane.showMessageDialog(null, "No items in the list");
		} else {
			// Add items to the ChooseComboBox
			for (String name : nameList) {
				ChooseComboBox.addItem(name);
			}
		}
	}

	private void FieldsThatOnlyHandleNumbers() {
		NumberField.setDocument(new WriteOffFromExistingForm.NumericFilter());
	}

	private void CloseApp() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
	}

	private void IfOkPressed(){
		OKButton.addActionListener(e -> {
			try {
				writeOff();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
			if(OKButton.isEnabled()&&NumberField.getText()!=null)dispose();new InventoryManagementUI(null);
		});
	}
	private void IfCancelPressed() {
		CancelButton.addActionListener(e -> {
			dispose();
			new InventoryManagementUI(null);
		});
	}

	public void writeOff() throws IOException {
		Config config = ConfigManager.readConfig();
		List<String> nameList = config.getNamesList();
		int selectedIdx = ChooseComboBox.getSelectedIndex();
		// Open the Excel workbook
		FileInputStream filePath = new FileInputStream(EXEL_FILE_PATH);
		Workbook workbook = WorkbookFactory.create(filePath);
		XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
		Row newRow = sheet.getRow(selectedIdx+3);
		// Get the current quantity from the Excel sheet, or show an error message if the cell is empty
		Cell quantityCell = newRow.getCell(6);
		if (quantityCell == null || quantityCell.getCellType() == CellType.BLANK) {
			JOptionPane.showMessageDialog(PanelWriteOffForm, "Error: The selected cell is empty.");
			workbook.close();
			return;
		}
		double currentQuantity = quantityCell.getNumericCellValue();

		// Get the input quantity from the NumberField
		int inputQuantity = Integer.parseInt(NumberField.getText());
		String newQuantityString=null;
		// Check if the input quantity is negative or greater than the current quantity
		while (inputQuantity < 0 || inputQuantity > currentQuantity) {
			// Display a message to prompt the user to enter a new quantity
			String message = "Please enter a positive quantity that is less than or equal to the current quantity (" + currentQuantity + ")";
			JOptionPane.showMessageDialog(PanelWriteOffForm, message);
			JOptionPane a = new JOptionPane();
			// Get the new input quantity from the user
			newQuantityString = a.showInputDialog(PanelWriteOffForm, "Enter a new quantity:");
			if(newQuantityString!=null && !newQuantityString.equals(""))inputQuantity = Integer.parseInt(newQuantityString);
			else {
				a.getRootFrame().dispose();
				break;

			}
		}
		Date selectedDate = DateChooser.getDate();
		// Update the Excel sheet with the new quantity
		double newQuantity = currentQuantity - inputQuantity;
		newRow.getCell(6).setCellValue(newQuantity);
		saveDeliveryOut(nameList.get(selectedIdx), inputQuantity, selectedDate,config.getItemGroupList().get(selectedIdx),config.getItemSupplierList().get(selectedIdx));
		// Save and close the workbook
		workbook.setForceFormulaRecalculation(true);
		FileOutputStream out = new FileOutputStream(EXEL_FILE_PATH);
		workbook.write(out);
		out.close();
		workbook.close();
	}
	private void saveDeliveryOut(String name, int size, Date date,int group,int supp) throws IOException {
		DeliveryConfig config = ConfigManager.readInOut();
		List<Delivery> deliveriesOut = new ArrayList<>();
		if (config != null && config.getDeliveriesOut() != null) {
			deliveriesOut = config.getDeliveriesOut();
		}
		Delivery delivery = new Delivery(name, size, date,group,supp);
		deliveriesOut.add(delivery);
		config.setDeliveriesOut(deliveriesOut);
		ConfigManager.writeInOut(config);
	}
	//sub classes
	private static class NumericFilter extends PlainDocument {
		@Override
		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
			if (str == null) {
				return;
			}

			char[] chars = str.toCharArray();
			StringBuilder sb = new StringBuilder();
			for (char ch : chars) {
				if (Character.isDigit(ch)) {
					sb.append(ch);
				}
			}
			super.insertString(offs, sb.toString(), a);
		}
	}
}
