package com.automatedworkspace.inventorymanagement.ui.EditItem;

import com.automatedworkspace.inventorymanagement.statistics.Config;
import com.automatedworkspace.inventorymanagement.statistics.ConfigManager;
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
	 * The Supplier combo box.
	 */
	private JComboBox<String> supplierComboBox;
	/**
	 * The Edit supplier panel.
	 */
	private JPanel editSupplierPanel;
	/**
	 * The Supplier name text field.
	 */
	private JTextField supplierNameTextField;
	/**
	 * The Ok button.
	 */
	private JButton OkButton;
	/**
	 * The Cancel button.
	 */
	private JButton CancelButton;


	/**
	 * Instantiates a new Edit supplier form.
	 *
	 * @param parent the parent
	 */
	public EditSupplierForm(JFrame parent) {
		super(parent);
		setSize(500, 450);
		setVisible(true);
		setContentPane(editSupplierPanel);
		setLocationRelativeTo(parent);
		OkButton.setEnabled(false);
		try {
			AddSupplierToComboBox();
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
		// create document listener
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

		// add document listener to each text field
		supplierNameTextField.getDocument().addDocumentListener(documentListener);
		// add action listener to the "OK" button to clear the memory from the listener
		OkButton.addActionListener(e -> {
			// remove document listener from each text field
			supplierNameTextField.getDocument().removeDocumentListener(documentListener);
		});
	}

	/**
	 * Check fields.
	 */
	private void checkFields() {
		OkButton.setEnabled(!supplierNameTextField.getText().isEmpty());
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
		OkButton.addActionListener(e -> {
			try {
				EchangeSupplier();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
			dispose();
			new SelectionEditForm(null);
		});
	}

	/**
	 * If cancel pressed.
	 */
	private void IfCancelPressed() {
		CancelButton.addActionListener(e -> {
			dispose();
			new SelectionEditForm(null);
		});

	}

	/**
	 * Add supplier to combo box.
	 *
	 * @throws IOException the io exception
	 */
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

	/**
	 * Echange supplier.
	 *
	 * @throws IOException the io exception
	 */
	private void EchangeSupplier() throws IOException {
		Config config = ConfigManager.readConfig();

		//List<Integer> limitList = config.get();
		List<String> supplierList = config.getSupplierList();
		int selectedIndx = supplierComboBox.getSelectedIndex();
		String prevName = supplierList.get(selectedIndx);
		String newName = supplierNameTextField.getText();
		//Integer назва = Integer.parse

		if (supplierList.contains(newName)) {
			// Ask user for a new name if it already exists
			while (supplierList.contains(newName)) {
				newName = JOptionPane.showInputDialog(null, "Supplier already exists in config file. Please enter a new Supplier:");
			}
		}
		newName = newName.replaceAll("\\s+", "");
		if (newName.equals("")) {
			return;
		}
		supplierList.set(selectedIndx, newName);
		// Open the Excel workbook
		FileInputStream filePath = new FileInputStream(EXEL_FILE_PATH);
		Workbook workbook = WorkbookFactory.create(filePath);
		Sheet sheet = workbook.getSheetAt(0);
		Row row;
		Cell cell;


		for (int i = 0; i < config.getNotNullRows(); i++) {
			int index = i + 3;
			row = sheet.getRow(index);
			cell = row.getCell(4);
			//якщо є
			if (cell.getStringCellValue().equals(prevName)) {
				cell.setCellValue(newName);
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
