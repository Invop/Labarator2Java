package com.automatedworkspace.inventorymanagement.ui.Nomenclature;

import com.automatedworkspace.inventorymanagement.FiledFilter.NumericFilter;
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.automatedworkspace.inventorymanagement.ui.AddItem.AddItemForm.EXEL_FILE_PATH;

/**
 * The type Write off from existing form.
 */
public class WriteOffFromExistingForm extends JDialog {
	/**
	 * The Panel write off form.
	 */
	private JPanel PanelWriteOffForm;
	/**
	 * The Choose combo box.
	 */
	private JComboBox<String> ChooseComboBox;
	/**
	 * The Number field.
	 */
	private JTextField NumberField;
	/**
	 * The Ok button.
	 */
	private JButton OKButton;
	/**
	 * The Cancel button.
	 */
	private JButton CancelButton;
	/**
	 * The Date panel.
	 */
	private JPanel DatePanel;
	/**
	 * The Date chooser.
	 */
	private final JDateChooser DateChooser = new JDateChooser();

	/**
	 * Instantiates a new Write off from existing form.
	 *
	 * @param parent the parent
	 */
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

	/**
	 * Listener.
	 */
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
		OKButton.addActionListener(e -> {
			// remove document listener from each text field
			NumberField.getDocument().removeDocumentListener(documentListener);
		});
	}

	/**
	 * Check fields.
	 */
	private void checkFields() {
		boolean number = !NumberField.getText().isEmpty();
		boolean comboBox = ChooseComboBox.getSelectedIndex() != -1;
		boolean dateSelected = DateChooser.getDate() != null; // check if a date is selected in the DateChooser
		OKButton.setEnabled(number && comboBox && dateSelected);
	}

	/**
	 * Get item names.
	 *
	 * @throws IOException the io exception
	 */
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

	/**
	 * Fields that only handle numbers.
	 */
	private void FieldsThatOnlyHandleNumbers() {
		NumberField.setDocument(new NumericFilter());
	}

	/**
	 * Close app.
	 */
	private void CloseApp() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
	}

	/**
	 * If ok pressed.
	 */
	private void IfOkPressed() {
		OKButton.addActionListener(e -> {
			try {
				writeOff();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
			if (OKButton.isEnabled() && NumberField.getText() != null) dispose();
			new InventoryManagementUI(null);
		});
	}

	/**
	 * If cancel pressed.
	 */
	private void IfCancelPressed() {
		CancelButton.addActionListener(e -> {
			dispose();
			new InventoryManagementUI(null);
		});
	}

	/**
	 * Write off.
	 *
	 * @throws IOException the io exception
	 */
	public void writeOff() throws IOException {
		Config config = ConfigManager.readConfig();
		List<String> nameList = config.getNamesList();
		int selectedIdx = ChooseComboBox.getSelectedIndex();
		// Open the Excel workbook
		FileInputStream filePath = new FileInputStream(EXEL_FILE_PATH);
		Workbook workbook = WorkbookFactory.create(filePath);
		XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
		Row newRow = sheet.getRow(selectedIdx + 3);
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
		String newQuantityString;
		// Check if the input quantity is negative or greater than the current quantity
		while (inputQuantity < 0 || inputQuantity > currentQuantity) {
			// Display a message to prompt the user to enter a new quantity
			String message = "Please enter a positive quantity that is less than or equal to the current quantity (" + currentQuantity + ")";
			JOptionPane.showMessageDialog(PanelWriteOffForm, message);
			// Get the new input quantity from the user
			newQuantityString = JOptionPane.showInputDialog(PanelWriteOffForm, "Enter a new quantity:");
			if (newQuantityString != null && !newQuantityString.equals(""))
				inputQuantity = Integer.parseInt(newQuantityString);
			else {
				JOptionPane.getRootFrame().dispose();
				break;

			}
		}
		Date selectedDate = DateChooser.getDate();
		// Update the Excel sheet with the new quantity
		double newQuantity = currentQuantity - inputQuantity;
		newRow.getCell(6).setCellValue(newQuantity);
		saveDeliveryOut(nameList.get(selectedIdx), inputQuantity, selectedDate, config.getItemGroupList().get(selectedIdx), config.getItemSupplierList().get(selectedIdx));
		// Save and close the workbook
		workbook.setForceFormulaRecalculation(true);
		FileOutputStream out = new FileOutputStream(EXEL_FILE_PATH);
		workbook.write(out);
		out.close();
		workbook.close();
	}

	/**
	 * Save delivery out.
	 *
	 * @param name  the name
	 * @param size  the size
	 * @param date  the date
	 * @param group the group
	 * @param supp  the supp
	 * @throws IOException the io exception
	 */
	private void saveDeliveryOut(String name, int size, Date date, int group, int supp) throws IOException {
		DeliveryConfig config = ConfigManager.readInOut();
		List<Delivery> deliveriesOut = new ArrayList<>();
		if (config != null && config.getDeliveriesOut() != null) {
			deliveriesOut = config.getDeliveriesOut();
		}
		Delivery delivery = new Delivery(name, size, date, group, supp);
		deliveriesOut.add(delivery);
		assert config != null;
		config.setDeliveriesOut(deliveriesOut);
		ConfigManager.writeInOut(config);
	}
}
