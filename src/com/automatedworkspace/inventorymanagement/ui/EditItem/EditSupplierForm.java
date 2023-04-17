package com.automatedworkspace.inventorymanagement.ui.EditItem;

import com.automatedworkspace.inventorymanagement.statistics.Config;
import com.automatedworkspace.inventorymanagement.statistics.ConfigManager;
import com.automatedworkspace.inventorymanagement.ui.DeleteItem.SelectionDeleteForm;
import org.apache.poi.ss.usermodel.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.automatedworkspace.inventorymanagement.ui.AddItem.AddItemForm.EXEL_FILE_PATH;

/**
 * The type Edit supplier form.
 */
public class EditSupplierForm extends JDialog {
	/**
	 * The Supplier ok button.
	 */
	private JButton supplierOkButton;
	/**
	 * The Supplier name field.
	 */
	private JTextField supplierNameField;
	/**
	 * The Supplier name label.
	 */
	private JLabel supplierNameLabel;
	/**
	 * The Supplier combo box.
	 */
	private JComboBox supplierComboBox;
	/**
	 * The Edit supplier panel.
	 */
	private JPanel editSupplierPanel;
	/**
	 * The Supplier cancel button.
	 */
	private JButton supplierCancelButton;
	private JTextField textField1;

	public EditSupplierForm(JFrame parent){
		super(parent);
		setSize(500, 450);
		setVisible(true);
		setContentPane(editSupplierPanel);
		setLocationRelativeTo(parent);
		supplierOkButton.setEnabled(false);
		try {
			AddSupplierToComboBox();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		IfOkPressed();
		IfCancelPressed();
		CloseApp();
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
		supplierOkButton.addActionListener(e -> {
			try {
				EchangeSupplier();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
			dispose();
			new SelectionDeleteForm(null);
		});
	}
	private void IfCancelPressed() {
		supplierCancelButton.addActionListener(e -> {
			dispose();
			new SelectionDeleteForm(null);
		});

	}
	private void AddSupplierToComboBox() throws IOException {
		// Read the config file
		Config config = ConfigManager.readConfig();
		// Check if the name already exists in the config file
		List<String> supplierList = config.getSupplierList();

		if (supplierList.isEmpty()) {
			// No items in the list
			JOptionPane.showMessageDialog(null, "No Groups in the list");
		} else {
			// Add items to the AddGroupBox
			for (String supplier : supplierList) {
				supplierComboBox.addItem(supplier);
			}
		}
	}



	private void checkFields() {
		if(supplierNameField.getText().isEmpty()){
			supplierOkButton.setEnabled(false);
		}
		else supplierOkButton.setEnabled(true);
	}

	private void EchangeSupplier() throws  IOException {
		Config  config = ConfigManager.readConfig();

		//List<Integer> limitList = config.get();
		List<String> supplierList = config.getSupplierList();
		int selectedIndx = supplierComboBox.getSelectedIndex();
		String prevName = supplierList.get(selectedIndx);
		String newName = supplierNameField.getText();
		//Integer назва = Integer.parse

		if (supplierList.contains(newName)) {
			// Ask user for a new name if it already exists
			while (supplierList.contains(newName)) {
				newName = JOptionPane.showInputDialog(null, "Supplier already exists in config file. Please enter a new Supplier:");
			}
		}

		supplierList.set(selectedIndx, newName);
		// Open the Excel workbook
		FileInputStream filePath = new FileInputStream(EXEL_FILE_PATH);
		Workbook workbook = WorkbookFactory.create(filePath);
		Sheet sheet = workbook.getSheetAt(0);
		Row row;
		Cell cell;



		for (int i = 0; i < config.getNotNullRows() ; i++) {
			int index = i + 3;
			row = sheet.getRow(index);
			cell = row.getCell(4);
			if(config.getItemSupplierList().get(i)==null){
				System.out.println(11);
			}else {
				//якщо є
				if (cell.getStringCellValue().equals(prevName)) {
					cell.setCellValue(newName);
				}
			}
			//cell.getNumericCellValue()==
		}
		// Save the workbook & config
		FileOutputStream out = new FileOutputStream(EXEL_FILE_PATH);
		config.setSupplierList(supplierList);
		ConfigManager.writeConfig(config);
		workbook.write(out);
		out.close();
		workbook.close();
	}

}
