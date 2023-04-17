package com.automatedworkspace.inventorymanagement.ui.AddItem;

import com.automatedworkspace.inventorymanagement.FiledFilter.NumericFilter;
import com.automatedworkspace.inventorymanagement.statistics.Config;
import com.automatedworkspace.inventorymanagement.statistics.ConfigManager;
import org.apache.poi.ss.usermodel.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


/**
 * The type Add item form.
 */
public class AddItemForm extends JDialog {


	/**
	 * The Create form brand.
	 */
	private JPanel CreateFormBrand;
	/**
	 * The Add id field.
	 */
	private JTextField AddIDField;
	/**
	 * The Add name field.
	 */
	private JTextField AddNameField;
	/**
	 * The Add supplier box.
	 */
	private JComboBox<String> AddSupplierBox;
	/**
	 * The Add price field.
	 */
	private JTextField AddPriceField;
	/**
	 * The Add limit field.
	 */
	private JTextField AddLimitField;
	/**
	 * The Add group box.
	 */
	private JComboBox<String> AddGroupBox;
	/**
	 * The Ok button.
	 */
	private JButton OkButton;
	/**
	 * The Cancel button.
	 */
	private JButton CancelButton;
	/**
	 * The Interval field.
	 */
	private JTextField IntervalField;
	/**
	 * The constant EXEL_FILE_PATH.
	 */
	public static final String EXEL_FILE_PATH = "src/com/automatedworkspace/files/Inventory.xlsx";

	/**
	 * Instantiates a new Add item form.
	 *
	 * @param parent the parent
	 */
	public AddItemForm(JFrame parent) {
		super(parent);
		setSize(500, 450);
		setVisible(true);
		setContentPane(CreateFormBrand);
		setLocationRelativeTo(parent);
		try {
			AddSuppliersToComboBox();
			AddGroupsToComboBox();
			if (AddGroupBox.getItemCount() == 0 || AddSupplierBox.getItemCount() == 0) {
				dispose();
				new SelectionAddForm(null);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		OkButton.setEnabled(false);
		FieldsThatOnlyHandleNumbers();
		Listener();
		IfOkPressed();
		IfCancelPressed();
		CloseApp();
	}

	/**
	 * Add groups to combo box.
	 *
	 * @throws IOException the io exception
	 */
	private void AddGroupsToComboBox() throws IOException {
		// Read the config file
		Config config = ConfigManager.readConfig();
		// Check if the name already exists in the config file
		List<String> groupList = config.getGroupList();

		if (groupList.isEmpty()) {
			// No items in the list
			JOptionPane.showMessageDialog(null, "No Groups in the list");
		} else {
			// Add items to the AddGroupBox
			for (String group : groupList) {
				AddGroupBox.addItem(group);
			}
		}
	}

	/**
	 * Add suppliers to combo box.
	 *
	 * @throws IOException the io exception
	 */
	private void AddSuppliersToComboBox() throws IOException {
		// Read the config file
		Config config = ConfigManager.readConfig();
		// Check if the name already exists in the config file
		List<String> supplierList = config.getSupplierList();

		if (supplierList.isEmpty()) {
			// No items in the list
			JOptionPane.showMessageDialog(null, "No Suppliers in the list");
		} else {
			// Add items to the AddGroupBox
			for (String supplier : supplierList) {
				AddSupplierBox.addItem(supplier);
			}
		}
	}

	/**
	 * Fields that only handle numbers.
	 */
	private void FieldsThatOnlyHandleNumbers() {
		AddPriceField.setDocument(new NumericFilter());
		AddLimitField.setDocument(new NumericFilter());
		IntervalField.setDocument(new NumericFilter());
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
		AddIDField.getDocument().addDocumentListener(documentListener);
		AddNameField.getDocument().addDocumentListener(documentListener);
		AddPriceField.getDocument().addDocumentListener(documentListener);
		AddLimitField.getDocument().addDocumentListener(documentListener);
		IntervalField.getDocument().addDocumentListener(documentListener);

		// add combo box to listener
		AddSupplierBox.addActionListener((e) -> checkFields());
		AddGroupBox.addActionListener((e) -> checkFields());

		// add action listener to the "OK" button to clear the memory from the listener
		OkButton.addActionListener(e -> {
			// remove document listener from each text field
			AddIDField.getDocument().removeDocumentListener(documentListener);
			AddNameField.getDocument().removeDocumentListener(documentListener);
			AddPriceField.getDocument().removeDocumentListener(documentListener);
			AddLimitField.getDocument().removeDocumentListener(documentListener);
			IntervalField.getDocument().removeDocumentListener(documentListener);
		});
	}

	/**
	 * Check fields.
	 */
	private void checkFields() {
		if (AddIDField.getText().isEmpty() || AddNameField.getText().isEmpty() ||
				AddPriceField.getText().isEmpty() || AddLimitField.getText().isEmpty() || IntervalField.getText().isEmpty()) {
			OkButton.setEnabled(false);
		} else {
			OkButton.setForeground(Color.BLACK);
			OkButton.setEnabled(true);
		}
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
				addRowToExcelTable();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
			dispose();
			new SelectionAddForm(null);
		});
	}

	/**
	 * If cancel pressed.
	 */
	private void IfCancelPressed() {
		CancelButton.addActionListener(e -> {
			dispose();
			new SelectionAddForm(null);
		});

	}

	/**
	 * Add row to excel table.
	 *
	 * @throws IOException the io exception
	 */
	private void addRowToExcelTable() throws IOException {
		// Get the current not null rows from the config file
		Config config = ConfigManager.readConfig();
		int notNullRows = config.getNotNullRows();

		// Open the Excel workbook
		FileInputStream filePath = new FileInputStream(EXEL_FILE_PATH);
		Workbook workbook = WorkbookFactory.create(filePath);
		Sheet sheet = workbook.getSheetAt(0);


		// Check if the table is empty
		if (sheet.getRow(3).getCell(2).getStringCellValue().isEmpty()) {
			// Table is empty, set notNullRows to 3
			notNullRows = 3;
		}
		// Find the first empty row
		int row = notNullRows;
		while (sheet.getRow(row) != null && sheet.getRow(row).getCell(2) != null && !sheet.getRow(row).getCell(3).toString().isEmpty()) {
			row++;
		}

		// Update the config file
		config.setNotNullRows(row + 1);
		ConfigManager.writeConfig(config);

		// Fill in the row with data
		Row newRow = sheet.getRow(row);
		if (newRow == null) {
			newRow = sheet.createRow(row);
		}
		Cell cell = newRow.getCell(2);
		if (cell == null) {
			cell = newRow.createCell(2);
		}
		addIDToConfig(AddIDField.getText());
		String newItemID = AddIDField.getText();
		newItemID = newItemID.replaceAll("\\s+", "");
		cell.setCellValue(newItemID);

		cell = newRow.getCell(3);
		if (cell == null) {
			cell = newRow.createCell(3);
		}
		addNameToConfig(AddNameField.getText());
		String newItemName = AddNameField.getText();
		newItemName = newItemName.replaceAll("\\s+", "");
		cell.setCellValue(newItemName);
		cell = newRow.getCell(4);
		if (cell == null) {
			cell = newRow.createCell(4);
		}
		addSupplierStatusToConfig(AddSupplierBox.getSelectedIndex());
		cell.setCellValue((String) AddSupplierBox.getSelectedItem());

		cell = newRow.getCell(5);
		if (cell == null) {
			cell = newRow.createCell(5);
		}
		cell.setCellValue(Integer.parseInt(AddPriceField.getText()));

		cell = newRow.getCell(8);
		if (cell == null) {
			cell = newRow.createCell(8);
		}
		addLimitToConfig(Integer.valueOf(AddLimitField.getText()));
		cell.setCellValue(Integer.parseInt(AddLimitField.getText()));

		cell = newRow.getCell(9);
		if (cell == null) {
			cell = newRow.createCell(9);
		}
		addIntervalToConfig(Integer.valueOf(IntervalField.getText()));
		cell.setCellValue(Integer.parseInt(IntervalField.getText()));

		cell = newRow.getCell(12);
		if (cell == null) {
			cell = newRow.createCell(12);
		}
		addItemGroupToConfig(AddGroupBox.getSelectedIndex());
		cell.setCellValue((String) AddGroupBox.getSelectedItem());
		// Save the workbook
		FileOutputStream out = new FileOutputStream(EXEL_FILE_PATH);
		workbook.write(out);
		out.close();
		workbook.close();
	}

	/**
	 * Add name to config.
	 *
	 * @param newName the new name
	 * @throws IOException the io exception
	 */
	private void addNameToConfig(String newName) throws IOException {
		// Read the config file
		Config config = ConfigManager.readConfig();

		// Check if the name already exists in the config file
		List<String> nameList = config.getNamesList();
		if (nameList.contains(newName)) {
			// Ask user for a new name if it already exists
			while (nameList.contains(newName)) {
				newName = JOptionPane.showInputDialog(null, "Name already exists in config file. Please enter a new name:");
			}
		}
		newName = newName.replaceAll("\\s+", "");
		if (newName.equals("")) {
			return;
		}
		AddNameField.setText(newName);

		// Add the new name to the list
		nameList.add(newName);
		config.setNamesList(nameList);

		// Write the updated config file
		ConfigManager.writeConfig(config);
	}

	/**
	 * Add id to config.
	 *
	 * @param newID the new id
	 * @throws IOException the io exception
	 */
	private void addIDToConfig(String newID) throws IOException {
		// Read the config file
		Config config = ConfigManager.readConfig();

		// Check if the ID already exists in the config file
		List<String> idList = config.getIDList();
		if (idList.contains(newID)) {
			// Ask user for a new name if it already exists
			while (idList.contains(newID)) {
				newID = JOptionPane.showInputDialog(null, "ID already exists in config file. Please enter a new ID:");
			}
		}
		newID = newID.replaceAll("\\s+", "");
		if (newID.equals("")) {
			return;
		}
		AddIDField.setText(newID);
		// Add the new ID to the list
		idList.add(newID);
		config.setIDList(idList);

		// Write the updated config file
		ConfigManager.writeConfig(config);
	}

	/**
	 * Add limit to config.
	 *
	 * @param newLimit the new limit
	 * @throws IOException the io exception
	 */
	private void addLimitToConfig(Integer newLimit) throws IOException {
		// Read the config file
		Config config = ConfigManager.readConfig();

		// Check if the ID already exists in the config file
		List<Integer> LimitList = config.getLimitList();
		// Add the new Limit to the list
		LimitList.add(newLimit);
		config.setLimitList(LimitList);
		ConfigManager.writeConfig(config);
	}

	/**
	 * Add interval to config.
	 *
	 * @param newInterval the new interval
	 * @throws IOException the io exception
	 */
	private void addIntervalToConfig(Integer newInterval) throws IOException {
		// Read the config file
		Config config = ConfigManager.readConfig();

		// Check if the ID already exists in the config file
		List<Integer> IntervalList = config.getIntervalList();
		// Add the new Limit to the list
		IntervalList.add(newInterval);
		config.setIntervalList(IntervalList);
		ConfigManager.writeConfig(config);
	}

	/**
	 * Add item group to config.
	 *
	 * @param newGroup the new group
	 * @throws IOException the io exception
	 */
	private void addItemGroupToConfig(int newGroup) throws IOException {
		// Read the config file
		Config config = ConfigManager.readConfig();

		// Check if the ID already exists in the config file
		List<Integer> itemGroupList = config.getItemGroupList();
		// Add the new Limit to the list
		itemGroupList.add(newGroup);
		config.setItemGroupList(itemGroupList);
		ConfigManager.writeConfig(config);
	}

	/**
	 * Add supplier status to config.
	 *
	 * @param newSupplier the new supplier
	 * @throws IOException the io exception
	 */
	private void addSupplierStatusToConfig(int newSupplier) throws IOException {
		// Read the config file
		Config config = ConfigManager.readConfig();
		List<Integer> supplierList = config.getItemSupplierList();
		supplierList.add(newSupplier);
		config.setItemSupplierList(supplierList);
		ConfigManager.writeConfig(config);
	}


}
