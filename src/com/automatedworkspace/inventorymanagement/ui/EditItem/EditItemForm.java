package com.automatedworkspace.inventorymanagement.ui.EditItem;

import com.automatedworkspace.inventorymanagement.statistics.Config;
import com.automatedworkspace.inventorymanagement.statistics.ConfigManager;
import com.automatedworkspace.inventorymanagement.ui.DeleteItem.SelectionDeleteForm;
import org.apache.poi.ss.usermodel.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	private JComboBox IdentificatorcomboBox;
	private JTextField NametextField1;
	private JComboBox SuppliercomboBox1;
	private JTextField PricetextField;
	private JTextField LimittextField;
	private JTextField IntervaltextField;
	private JTextField AmounttextField;
	private JComboBox GroupcomboBox;
	private JButton Okbutton;
	private JButton Cancelbutton;
	private int selectedIndx = 0;

	public EditItemForm(JFrame parent){
		super(parent);
		setSize(500, 450);
		setVisible(true);
		setContentPane(EditItemPanel);
		setLocationRelativeTo(parent);
		Okbutton.setEnabled(false);
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
	private void IfOkPressed(){
		Okbutton.addActionListener(e -> {
			try {
				EchangeName();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
			dispose();
			new SelectionDeleteForm(null);
		});
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
	private void IfCancelPressed() {
		Cancelbutton.addActionListener(e -> {
			dispose();
			new SelectionDeleteForm(null);
		});

	}
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
	private void AddSupplierToComboBox() throws IOException {
		SuppliercomboBox1.removeAllItems();
		// Read the config file
		Config config = ConfigManager.readConfig();
		// Check if the name already exists in the config file
		List<String> SupplierList = config.getSupplierList();
		List<Integer> indexList  = config.getItemSupplierList();
		System.out.println(selectedIndx);
		if (SupplierList.isEmpty()) {
			// No items in the list
			JOptionPane.showMessageDialog(null, "No supplier in the list");
		} else {
			// Add items to the AddGroupBox
			if(indexList.get(selectedIndx)!=-1) {
				for (int i = 0; i < SupplierList.size(); i++) {
					if (!SupplierList.get(i).equals(SupplierList.get(indexList.get(selectedIndx)))) {
						SuppliercomboBox1.addItem(SupplierList.get(i));
					}
				}
			}
			else{
				for(String Supplier : SupplierList){SuppliercomboBox1.addItem(Supplier);}
			}
		}
	}
	private void AddGroupToComboBox() throws IOException {
		// Read the config file
		Config config = ConfigManager.readConfig();
		// Check if the name already exists in the config file
		List<String> GroupList = config.getGroupList();

		if (GroupList.isEmpty()) {
			// No items in the list
			JOptionPane.showMessageDialog(null, "No groups in the list");
		} else {
			// Add items to the AddGroupBox
			for (String Groups : GroupList) {
				GroupcomboBox.addItem(Groups);
			}
		}
	}
	private void EchangeName() throws  IOException {
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

		NameList.set(selectedIndx, newName);
		// Open the Excel workbook
		FileInputStream filePath = new FileInputStream(EXEL_FILE_PATH);
		Workbook workbook = WorkbookFactory.create(filePath);
		Sheet sheet = workbook.getSheetAt(0);
		Row row;
		Cell cell;


		for (int i = 0; i < config.getNotNullRows(); i++) {
			int index = i + 3;
			row = sheet.getRow(index);
			cell = row.getCell(3);
			if (config.getNamesList().get(i) == null) {
				System.out.println(11);
			} else {
				//якщо є
				if (cell.getStringCellValue().equals(prevName)) {
					cell.setCellValue(newName);
				}
			}
			//cell.getNumericCellValue()==
		}
		// Save the workbook & config
		FileOutputStream out = new FileOutputStream(EXEL_FILE_PATH);
		config.setNamesList(NameList);
		ConfigManager.writeConfig(config);
		workbook.write(out);
		out.close();
		workbook.close();
	}
	private void EIntervalEdit() throws  IOException {
		Config  config = ConfigManager.readConfig();

		//List<Integer> limitList = config.get();
		List<Integer> intervalList = config.getLimitList();

		Integer newInterval = Integer.parseInt(IntervaltextField.getText());
		Integer prevInterval = intervalList.get(selectedIndx);
		//Integer назва = Integer.parse

		// Open the Excel workbook
		FileInputStream filePath = new FileInputStream(EXEL_FILE_PATH);
		Workbook workbook = WorkbookFactory.create(filePath);
		Sheet sheet = workbook.getSheetAt(0);
		Row row;
		Cell cell;



		for (int i = 0; i < config.getNotNullRows() ; i++) {
			int index = i + 3;
			row = sheet.getRow(index);
			cell = row.getCell(9);
			if(config.getIntervalList().get(i)==null){
				System.out.println(11);
			}else {
				//якщо є
				if (cell.getNumericCellValue()==prevInterval) {
					cell.setCellValue(newInterval);
				}
			}
			//cell.getNumericCellValue()==
		}
		// Save the workbook & config
		FileOutputStream out = new FileOutputStream(EXEL_FILE_PATH);
		config.setIntervalList(intervalList);
		ConfigManager.writeConfig(config);
		workbook.write(out);
		out.close();
		workbook.close();
	}
	private void ELimitEdit() throws  IOException {
		Config  config = ConfigManager.readConfig();

		//List<Integer> limitList = config.get();
		List<Integer> limitList = config.getLimitList();
		int selectedIndx = IdentificatorcomboBox.getSelectedIndex();

		Integer newLimit = Integer.parseInt(LimittextField.getText());
		Integer prevLimit = limitList.get(selectedIndx);
		//Integer назва = Integer.parse

		// Open the Excel workbook
		FileInputStream filePath = new FileInputStream(EXEL_FILE_PATH);
		Workbook workbook = WorkbookFactory.create(filePath);
		Sheet sheet = workbook.getSheetAt(0);
		Row row;
		Cell cell;



		for (int i = 0; i < config.getNotNullRows() ; i++) {
			int index = i + 3;
			row = sheet.getRow(index);
			cell = row.getCell(8);
			if(config.getLimitList().get(i)==null){
				System.out.println(11);
			}else {
				//якщо є
				if (cell.getNumericCellValue()==prevLimit) {
					cell.setCellValue(newLimit);
				}
			}
			//cell.getNumericCellValue()==
		}
		// Save the workbook & config
		FileOutputStream out = new FileOutputStream(EXEL_FILE_PATH);
		config.setLimitList(limitList);
		ConfigManager.writeConfig(config);
		workbook.write(out);
		out.close();
		workbook.close();
	}
	private void EPriceEdit() throws  IOException {
		Config  config = ConfigManager.readConfig();

		//List<Integer> limitList = config.get();
		//List<Integer> priceList = config.getIDList();
		selectedIndx = IdentificatorcomboBox.getSelectedIndex();

		Integer newPrice = Integer.parseInt(PricetextField.getText());
		//Integer prevLimit = limitList.get(selectedIndx);
		//Integer назва = Integer.parse

		// Open the Excel workbook
		FileInputStream filePath = new FileInputStream(EXEL_FILE_PATH);
		Workbook workbook = WorkbookFactory.create(filePath);
		Sheet sheet = workbook.getSheetAt(0);
		Row row = sheet.getRow(selectedIndx);
		Cell cell = row.getCell(5);
		cell.setCellValue(newPrice);

		// Save the workbook & config
		FileOutputStream out = new FileOutputStream(EXEL_FILE_PATH);
		//config.setLimitList(limitList);
		ConfigManager.writeConfig(config);
		workbook.write(out);
		out.close();
		workbook.close();
	}
}
