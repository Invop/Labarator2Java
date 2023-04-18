package com.automatedworkspace.inventorymanagement.ui.EditItem;

import com.automatedworkspace.inventorymanagement.FiledFilter.NumericFilter;
import com.automatedworkspace.inventorymanagement.statistics.Config;
import com.automatedworkspace.inventorymanagement.statistics.ConfigManager;
import org.apache.poi.ss.usermodel.*;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.automatedworkspace.inventorymanagement.ui.AddItem.AddItemForm.EXEL_FILE_PATH;

/**
 * The type Edit item form.
 */
public class EditItemForm extends JDialog {
	/**
	 * The Edit item panel.
	 */
	private JPanel EditItemPanel;
	/**
	 * The Identificatorcombo box.
	 */
	private JComboBox<String> IdentificatorcomboBox;
	/**
	 * The Nametext field 1.
	 */
	private JTextField NametextField1;
	/**
	 * The Suppliercombo box 1.
	 */
	private JComboBox<String> SuppliercomboBox1;
	/**
	 * The Pricetext field.
	 */
	private JTextField PricetextField;
	/**
	 * The Limittext field.
	 */
	private JTextField LimittextField;
	/**
	 * The Intervaltext field.
	 */
	private JTextField IntervaltextField;
	/**
	 * The Groupcombo box.
	 */
	private JComboBox<String> GroupcomboBox;
	/**
	 * The Okbutton.
	 */
	private JButton Okbutton;
	/**
	 * The Cancelbutton.
	 */
	private JButton Cancelbutton;
	/**
	 * The Selected indx.
	 */
	private int selectedIndx = 0;

	/**
	 * Instantiates a new Edit item form.
	 *
	 * @param parent the parent
	 */
	public EditItemForm(JFrame parent) {
		super(parent);
		setSize(500, 450);
		setVisible(true);
		setContentPane(EditItemPanel);
		setLocationRelativeTo(parent);
		FieldsThatOnlyHandleNumbers();
		try {
			AddIDToComboBox();
			AddGroupToComboBox();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		IfOkPressed();
		IfCancelPressed();
		CloseApp();
		IdentificatorcomboBox.addActionListener(actionEvent -> {
			try {
				selectedIndx = IdentificatorcomboBox.getSelectedIndex();
				AddSupplierToComboBox();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}

	/**
	 * If ok pressed.
	 */
	private void IfOkPressed() {
		Okbutton.addActionListener(e -> {
			try {
				if (!NametextField1.getText().isEmpty()) {
					EchangeName();
				}
				if (SuppliercomboBox1.getSelectedIndex() != 0) {
					EsupplierChange();
				}
				if (!IntervaltextField.getText().isEmpty()) {
					EIntervalEdit();
				}
				if (!LimittextField.getText().isEmpty()) {
					ELimitEdit();
				}
				if (!PricetextField.getText().isEmpty()) {
					EPriceEdit();
				}
				if (GroupcomboBox.getSelectedIndex() != 0) {
					EgroupChange();
				}
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
			dispose();
			new SelectionEditForm(null);
		});
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
	 * If cancel pressed.
	 */
	private void IfCancelPressed() {
		Cancelbutton.addActionListener(e -> {
			dispose();
			new SelectionEditForm(null);
		});

	}

	/**
	 * Add id to combo box.
	 *
	 * @throws IOException the io exception
	 */
	private void AddIDToComboBox() throws IOException {
		// Read the config file
		Config config = ConfigManager.readConfig();
		// Check if the name already exists in the config file
		List<String> IDList = config.getIDList();

		if (IDList.isEmpty()) {
			// No items in the list
			JOptionPane.showMessageDialog(null, "No ID in the list");
		} else {
			// Add items to the AddGroupBox
			for (String ID : IDList) {
				IdentificatorcomboBox.addItem(ID);
			}
		}
	}

	/**
	 * Add supplier to combo box.
	 *
	 * @throws IOException the io exception
	 */
	private void AddSupplierToComboBox() throws IOException {
		SuppliercomboBox1.removeAllItems();
		// Read the config file
		Config config = ConfigManager.readConfig();
		// Check if the name already exists in the config file
		List<String> SupplierList = config.getSupplierList();
		List<Integer> indexList = config.getItemSupplierList();
		if (SupplierList.isEmpty()) {
			// No items in the list
			JOptionPane.showMessageDialog(null, "No supplier in the list");
		} else {
			SuppliercomboBox1.addItem("");
			if (indexList.get(selectedIndx) != -1) {
				for (int i = 0; i < SupplierList.size(); i++) {
					if (!SupplierList.get(i).equals(SupplierList.get(indexList.get(selectedIndx)))) {
						SuppliercomboBox1.addItem(SupplierList.get(i));
					}
				}
			} else {
				for (String Supplier : SupplierList) {
					SuppliercomboBox1.addItem(Supplier);
				}
			}
		}
	}

	/**
	 * Add group to combo box.
	 *
	 * @throws IOException the io exception
	 */
	private void AddGroupToComboBox() throws IOException {
		GroupcomboBox.removeAllItems();
		// Read the config file
		Config config = ConfigManager.readConfig();
		// Check if the name already exists in the config file
		List<String> GroupList = config.getGroupList();
		List<Integer> indexList = config.getItemGroupList();
		if (GroupList.isEmpty()) {
			// No items in the list
			JOptionPane.showMessageDialog(null, "No groups in the list");
		} else {
			// Add items to the AddGroupBox
			GroupcomboBox.addItem("");
			for (int i = 0; i < GroupList.size(); i++) {
				if (!GroupList.get(i).equals(GroupList.get(indexList.get(selectedIndx)))) {
					GroupcomboBox.addItem(GroupList.get(i));
				}
			}
		}
	}

	/**
	 * Echange name.
	 *
	 * @throws IOException the io exception
	 */
	private void EchangeName() throws IOException {
		Config config = ConfigManager.readConfig();
		//List<Integer> limitList = config.get();
		List<String> NameList = config.getNamesList();
		String prevName = NameList.get(selectedIndx);
		String newName = NametextField1.getText();
		//Integer назва = Integer.parse

		if (NameList.contains(newName)) {
			// Ask user for a new name if it already exists
			while (NameList.contains(newName)) {
				newName = JOptionPane.showInputDialog(null, "Name already exists in config file. Please enter a new name:");
			}
		}
		newName = newName.replaceAll("\\s+", "");
		if (newName.equals("")) {
			return;
		}
		NameList.set(selectedIndx, newName);
		// Open the Excel workbook
		FileInputStream filePath = new FileInputStream(EXEL_FILE_PATH);
		Workbook workbook = WorkbookFactory.create(filePath);
		Sheet sheet = workbook.getSheetAt(0);
		Row row = sheet.getRow(selectedIndx + 3);
		Cell cell = row.getCell(3);
		if (cell.getStringCellValue().equals(prevName)) {
			cell.setCellValue(newName);
		}
		// Save the workbook & config
		FileOutputStream out = new FileOutputStream(EXEL_FILE_PATH);
		config.setNamesList(NameList);
		ConfigManager.writeConfig(config);
		workbook.write(out);
		out.close();
		workbook.close();
	}

	/**
	 * Esupplier change.
	 *
	 * @throws IOException the io exception
	 */
	private void EsupplierChange() throws IOException {
		Config config = ConfigManager.readConfig();
		List<Integer> ItemSuppList = config.getItemSupplierList();

		// Open the Excel workbook
		FileInputStream filePath = new FileInputStream(EXEL_FILE_PATH);
		Workbook workbook = WorkbookFactory.create(filePath);
		Sheet sheet = workbook.getSheetAt(0);
		Row row = sheet.getRow(selectedIndx + 3);
		Cell cell = row.getCell(4);
		cell.setCellValue((String) SuppliercomboBox1.getSelectedItem());
		ItemSuppList.set(selectedIndx, SuppliercomboBox1.getSelectedIndex() - 1);
		// Save the workbook & config
		FileOutputStream out = new FileOutputStream(EXEL_FILE_PATH);
		config.setItemSupplierList(ItemSuppList);
		ConfigManager.writeConfig(config);
		workbook.write(out);
		out.close();
		workbook.close();
	}

	/**
	 * Egroup change.
	 *
	 * @throws IOException the io exception
	 */
	private void EgroupChange() throws IOException {
		Config config = ConfigManager.readConfig();
		List<Integer> ItemGrList = config.getItemGroupList();

		// Open the Excel workbook
		FileInputStream filePath = new FileInputStream(EXEL_FILE_PATH);
		Workbook workbook = WorkbookFactory.create(filePath);
		Sheet sheet = workbook.getSheetAt(0);
		Row row = sheet.getRow(selectedIndx + 3);
		Cell cell = row.getCell(12);
		cell.setCellValue((String) GroupcomboBox.getSelectedItem());
		ItemGrList.set(selectedIndx, GroupcomboBox.getSelectedIndex() - 1);

		// Save the workbook & config
		FileOutputStream out = new FileOutputStream(EXEL_FILE_PATH);
		config.setItemGroupList(ItemGrList);
		ConfigManager.writeConfig(config);
		workbook.write(out);
		out.close();
		workbook.close();
	}

	/**
	 * E interval edit.
	 *
	 * @throws IOException the io exception
	 */
	private void EIntervalEdit() throws IOException {
		Config config = ConfigManager.readConfig();

		List<Integer> intervalList = config.getIntervalList();

		int newInterval = Integer.parseInt(IntervaltextField.getText());
		Integer prevInterval = intervalList.get(selectedIndx);

		// Open the Excel workbook
		FileInputStream filePath = new FileInputStream(EXEL_FILE_PATH);
		Workbook workbook = WorkbookFactory.create(filePath);
		Sheet sheet = workbook.getSheetAt(0);
		Row row = sheet.getRow(selectedIndx + 3);
		Cell cell = row.getCell(9);
		if (cell.getNumericCellValue() == prevInterval) {
			cell.setCellValue(newInterval);
		}
		intervalList.set(selectedIndx, newInterval);

		// Save the workbook & config
		FileOutputStream out = new FileOutputStream(EXEL_FILE_PATH);
		config.setIntervalList(intervalList);
		ConfigManager.writeConfig(config);
		workbook.write(out);
		out.close();
		workbook.close();
	}

	/**
	 * E limit edit.
	 *
	 * @throws IOException the io exception
	 */
	private void ELimitEdit() throws IOException {
		Config config = ConfigManager.readConfig();

		List<Integer> limitList = config.getLimitList();

		int newLimit = Integer.parseInt(LimittextField.getText());
		Integer prevLimit = limitList.get(selectedIndx);

		// Open the Excel workbook
		FileInputStream filePath = new FileInputStream(EXEL_FILE_PATH);
		Workbook workbook = WorkbookFactory.create(filePath);
		Sheet sheet = workbook.getSheetAt(0);
		Row row = sheet.getRow(selectedIndx + 3);
		Cell cell = row.getCell(8);
		if (cell.getNumericCellValue() == prevLimit) {
			cell.setCellValue(newLimit);
		}

		limitList.set(selectedIndx, newLimit);
		// Save the workbook & config
		FileOutputStream out = new FileOutputStream(EXEL_FILE_PATH);
		config.setLimitList(limitList);
		ConfigManager.writeConfig(config);
		workbook.write(out);
		out.close();
		workbook.close();
	}

	/**
	 * E price edit.
	 *
	 * @throws IOException the io exception
	 */
	private void EPriceEdit() throws IOException {
		Config config = ConfigManager.readConfig();
		// Open the Excel workbook
		FileInputStream filePath = new FileInputStream(EXEL_FILE_PATH);
		Workbook workbook = WorkbookFactory.create(filePath);
		Sheet sheet = workbook.getSheetAt(0);
		Row row = sheet.getRow(selectedIndx + 3);
		Cell cell = row.getCell(5);
		cell.setCellValue(Integer.parseInt(PricetextField.getText()));
		// Save the workbook & config
		FileOutputStream out = new FileOutputStream(EXEL_FILE_PATH);
		//config.setLimitList(limitList);
		ConfigManager.writeConfig(config);
		workbook.write(out);
		out.close();
		workbook.close();
	}

	/**
	 * Fields that only handle numbers.
	 */
	private void FieldsThatOnlyHandleNumbers() {
		PricetextField.setDocument(new NumericFilter());
		LimittextField.setDocument(new NumericFilter());
		IntervaltextField.setDocument(new NumericFilter());
	}


}
