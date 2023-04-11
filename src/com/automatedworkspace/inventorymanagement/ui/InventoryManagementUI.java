package com.automatedworkspace.inventorymanagement.ui;

import com.automatedworkspace.inventorymanagement.statistics.Config;
import com.automatedworkspace.inventorymanagement.statistics.ConfigManager;
import com.automatedworkspace.inventorymanagement.ui.AddItem.AddItemForm;
import com.automatedworkspace.inventorymanagement.ui.AddItem.SelectionAddForm;
import com.automatedworkspace.inventorymanagement.ui.DeleteItem.SelectionDeleteForm;
import com.automatedworkspace.inventorymanagement.ui.Nomenclature.AddToExistingForm;
import com.automatedworkspace.inventorymanagement.ui.Nomenclature.WriteOffFromExistingForm;
import org.apache.poi.ss.usermodel.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import static com.automatedworkspace.inventorymanagement.ui.AddItem.AddItemForm.EXEL_FILE_PATH;
import static javax.swing.ButtonGroup.*;

public class InventoryManagementUI extends JDialog{
	private JPanel MainPanel;
	private JTable table;
	private JButton addButton;
	private JButton editButton;
	private JButton deleteButton;
	private JButton importButton;
	private JButton exportButton;
	private JLabel searchLabel;
	private JButton findButton;
	private JRadioButton byGroupRadioButton;
	private JRadioButton byNameRadioButton;
	private JRadioButton byIdRadioButton;
	private JRadioButton bySupplierRadioButton;
	private JTextField textFieldNameId;
	private JComboBox<String> comboBoxGroupSupplier;
	private JRadioButton byPriceFromToRadioButton;
	private JTextField fromField;
	private JTextField toField;
	private JPanel ProductSearchPanel;
	private JPanel ButtonsPanel;
	private JToolBar toolBar;



	public InventoryManagementUI(JFrame parent){
		super(parent);
		setVisible(true);
		setSize(1280, 720);
		setContentPane(MainPanel);
		setLocationRelativeTo(parent);
		FieldsThatOnlyHandleNumbers();
		btnListeners();
		radioListeners();
		toolBar.setFloatable(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});

	}
	private void btnListeners(){
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new SelectionAddForm(null);
			}
		});
		editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new SelectionDeleteForm(null);
			}
		});
		importButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new AddToExistingForm(null);
			}
		});
		exportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new WriteOffFromExistingForm(null);
			}
		});

		findButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(byNameRadioButton.isSelected()) {
					displayInfoInTableNameID(true);
					System.gc();
				}
				if(byIdRadioButton.isSelected()) {
					displayInfoInTableNameID(false);
					System.gc();
				}
				if(byPriceFromToRadioButton.isSelected()) {
					displayInfoInTablePrice();
					System.gc();
				}
				if(byGroupRadioButton.isSelected()){
					try {
						displayInfoInTableGroupSupplier(true);

					} catch (IOException ex) {
						throw new RuntimeException(ex);
					}
					System.gc();
				}
				if(bySupplierRadioButton.isSelected()){
					try {
						displayInfoInTableGroupSupplier(false);
					} catch (IOException ex) {
						throw new RuntimeException(ex);
					}
					System.gc();
				}
			}
		});
	}
	private void radioListeners() {
		byIdRadioButton.addActionListener(e -> onRadioSelected(byIdRadioButton));
		byNameRadioButton.addActionListener(e -> onRadioSelected(byNameRadioButton));
		byGroupRadioButton.addActionListener(e -> onRadioSelected(byGroupRadioButton));
		byPriceFromToRadioButton.addActionListener(e -> onRadioSelected(byPriceFromToRadioButton));
		bySupplierRadioButton.addActionListener(e -> onRadioSelected(bySupplierRadioButton));
	}
	private void onRadioSelected(JRadioButton selectedRadioButton) {
		List<JRadioButton> radioButtons = Arrays.asList(byIdRadioButton, byNameRadioButton, byGroupRadioButton, byPriceFromToRadioButton, bySupplierRadioButton);
		radioButtons.stream().filter(r -> !r.equals(selectedRadioButton)).forEach(r -> r.setSelected(false));

		if (selectedRadioButton.equals(byIdRadioButton) || selectedRadioButton.equals(byNameRadioButton)) {
			showNameOrIdFields();
		} else if (selectedRadioButton.equals(byPriceFromToRadioButton)) {
			showPriceFields();
		} else if (selectedRadioButton.equals(byGroupRadioButton) || selectedRadioButton.equals(bySupplierRadioButton)) {
			comboBoxGroupSupplier.removeAllItems();
			Config config = null;
			try {
				config = ConfigManager.readConfig();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			showSupplierOrGroupFields();
			if(selectedRadioButton.equals(byGroupRadioButton)){
				List<String> groupList = config.getGroupList();
				for (String group :groupList){comboBoxGroupSupplier.addItem(group);	}
			}
			else{
				List<String> supplierList = config.getSupplierList();
				for (String supp : supplierList) {comboBoxGroupSupplier.addItem(supp);}
			}
		}
	}
	private void showNameOrIdFields() {
		textFieldNameId.setEnabled(true);
		textFieldNameId.setVisible(true);
		fromField.setVisible(false);
		fromField.setEnabled(false);
		toField.setVisible(false);
		toField.setEnabled(false);
		comboBoxGroupSupplier.setVisible(false);
		comboBoxGroupSupplier.setEnabled(false);
		toolBar.revalidate();
		toolBar.repaint();

	}
	private void showPriceFields() {
		textFieldNameId.setEnabled(false);
		textFieldNameId.setVisible(false);
		comboBoxGroupSupplier.setVisible(false);
		comboBoxGroupSupplier.setEnabled(false);
		fromField.setEnabled(true);
		toField.setEnabled(true);
		fromField.setVisible(true);
		toField.setVisible(true);
		toolBar.revalidate();
		toolBar.repaint();
	}
	private void showSupplierOrGroupFields() {
		textFieldNameId.setEnabled(false);
		textFieldNameId.setVisible(false);
		fromField.setVisible(false);
		fromField.setEnabled(false);
		toField.setVisible(false);
		toField.setEnabled(false);
		comboBoxGroupSupplier.setEnabled(true);
		comboBoxGroupSupplier.setVisible(true);
	}
	private void populateTable(Object[][] data) {
		DefaultTableModel model = new DefaultTableModel(data, new Object[]{"Column 1", "Column 2", "Column 3", "Column 4", "Column 5", "Column 6", "Column 7", "Column 8", "Column 9", "Column 10", "Column 11", "Column 12"});
		table.setModel(model);
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		for (int i = 1; i < 12; i++) {
			table.getColumnModel().getColumn(i).setPreferredWidth(50);
		}
	}
	private Object[][] getDataFromRowThree() {
		try (Workbook workbook = WorkbookFactory.create(new File(EXEL_FILE_PATH))) {
			Sheet sheet = workbook.getSheetAt(0);
			Object[][] data = new Object[1][12];
			Row row = sheet.getRow(2);
			for (int i = 0; i < 12; i++) {
				Cell cell = row.getCell(i + 1);
				if (cell.getCellType() == CellType.FORMULA) {
					data[0][i] = cell.getCellFormula();
				} else if (cell.getCellType() == CellType.NUMERIC) {
					data[0][i] = (int) cell.getNumericCellValue();
				} else {
					data[0][i] = cell.getStringCellValue();
				}
			}

			return data;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Failed to read Excel file: " + e.getMessage());
			return new Object[0][0];
		}
	}
	private void displayInfoInTableNameID(boolean isName) {
		Object[][] dataFromRowThree = getDataFromRowThree();
		String name = textFieldNameId.getText().trim();
		if (name.isEmpty()) {
			if(isName) {
				JOptionPane.showMessageDialog(this, "Please enter a name to search","Error",JOptionPane.ERROR_MESSAGE);
				return;
			}
			else{
				JOptionPane.showMessageDialog(this, "Please enter an ID to search","Error",JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

		try (Workbook workbook = WorkbookFactory.create(new File(EXEL_FILE_PATH))) {
			Sheet sheet = workbook.getSheetAt(0);
			int rowIdx = -1;
			if(isName) {
				for (Row row : sheet) {
					if (row.getRowNum() >= 3 && row.getCell(3) != null && row.getCell(3).getStringCellValue().equalsIgnoreCase(name)) {
						rowIdx = row.getRowNum();
						break;
					}
				}
				if (rowIdx < 0) {
					JOptionPane.showMessageDialog(this, "Name not found","Error",JOptionPane.ERROR_MESSAGE);
					textFieldNameId.setText("");
					return;
				}
			}
			else{
				for (Row row : sheet) {
					if (row.getRowNum() >= 3 && row.getCell(2) != null && row.getCell(2).getStringCellValue().equalsIgnoreCase(name)) {
						rowIdx = row.getRowNum();
						break;
					}
				}
				if (rowIdx < 0) {
					JOptionPane.showMessageDialog(this, "ID not found");
					textFieldNameId.setText("");
					return;
				}
			}
			Object[][] searchData = new Object[1][12];
			Row row = sheet.getRow(rowIdx);
			for (int i = 0; i < 12; i++) {
				Cell cell = row.getCell(i + 1);
				if(i==0){
					if(row.getCell(6).getNumericCellValue()>=row.getCell(8).getNumericCellValue() || row.getCell(12).getStringCellValue().equals("Так")){
						searchData[0][i]="False";
					}
					else searchData[0][i]="True";
				}
				else if (i == 6) {
					double product = row.getCell(5).getNumericCellValue() * row.getCell(6).getNumericCellValue();
					searchData[0][i] = product;
				}
				else if (cell.getCellType() == CellType.NUMERIC) {
					searchData[0][i] = (int) cell.getNumericCellValue();
				} else if(cell.getCellType() == CellType.STRING){
					searchData[0][i] = cell.getStringCellValue();
				}
			}


			Object[][] updatedData = new Object[dataFromRowThree.length + 1][12];
			System.arraycopy(dataFromRowThree, 0, updatedData, 0, dataFromRowThree.length);
			updatedData[dataFromRowThree.length] = searchData[0];

			populateTable(updatedData);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Failed to read Excel file: " + e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	private void displayInfoInTablePrice() {
		Object[][] dataFromRowThree = getDataFromRowThree();
		int from=0, to=0;

		if (fromField.getText().isEmpty() || toField.getText().isEmpty()) {
			// Display an error message to the user
			JOptionPane.showMessageDialog(null, "Please enter a valid integer in both fields.", "Error", JOptionPane.ERROR_MESSAGE);
		} else {
			// Parse the input as integers
			from = Integer.parseInt(fromField.getText());
			to = Integer.parseInt(toField.getText());
		}

		List<Object[]> matchingRows = new ArrayList<>();
		try (Workbook workbook = WorkbookFactory.create(new File(EXEL_FILE_PATH))) {
			Sheet sheet = workbook.getSheetAt(0);
			for (Row row : sheet) {
				if (row.getRowNum() >= 3) {
					Cell priceCell = row.getCell(5);
					if (priceCell != null && priceCell.getCellType() == CellType.NUMERIC) {
						int price = (int) priceCell.getNumericCellValue();
						if (price >= from && price <= to) {
							Object[] rowData = new Object[12];
							for (int i = 0; i < 12; i++) {

								Cell cell = row.getCell(i + 1);
								if(i==0){
									if(row.getCell(6).getNumericCellValue()>=row.getCell(8).getNumericCellValue() || row.getCell(12).getStringCellValue().equals("Так")){
										rowData[i]="False";
									}
									else 	rowData[i]="True";
								}
								else if (i == 6) {
									double product = row.getCell(5).getNumericCellValue() * row.getCell(6).getNumericCellValue();
									rowData[i] = product;
								}
								else if (cell.getCellType() == CellType.FORMULA) {
									rowData[i] = cell.getCellFormula();
								} else if (cell.getCellType() == CellType.NUMERIC) {
									rowData[i] = (int) cell.getNumericCellValue();
								} else {
									rowData[i] = cell.getStringCellValue();
								}
							}
							matchingRows.add(rowData);
						}
					}
				}
			}

		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Failed to read Excel file: " + e.getMessage());
			return;
		}

		Object[][] data = new Object[matchingRows.size() + 1][12];
		data[0] = dataFromRowThree[0]; // copy the header from row 3
		for (int i = 0; i < matchingRows.size(); i++) {
			data[i + 1] = matchingRows.get(i); // copy the matching rows
		}
		populateTable(data);
	}
	private void displayInfoInTableGroupSupplier(boolean isGroup) throws IOException {
		Object[][] dataFromRowThree = getDataFromRowThree();
		List<Object[]> matchingRows = new ArrayList<>();
		try (Workbook workbook = WorkbookFactory.create(new File(EXEL_FILE_PATH))) {
			Sheet sheet = workbook.getSheetAt(0);
			for (Row row : sheet) {
				if (row.getRowNum() >= 3) {
					Cell cellSG;
					if(!isGroup) {
						cellSG = row.getCell(4);
					}
					else {
						cellSG = row.getCell(12);
					}
					if (cellSG != null && cellSG.getCellType() == CellType.STRING) {
						String name = cellSG.getStringCellValue();
						if(name.equals(comboBoxGroupSupplier.getSelectedItem()))
						{   Object[] rowData = new Object[12];
							for (int i = 0; i < 12; i++) {
								Cell cell = row.getCell(i + 1);
								if(i==0){
									if(row.getCell(6).getNumericCellValue()>=row.getCell(8).getNumericCellValue() || row.getCell(12).getStringCellValue().equals("Так")){
										rowData[i]="False";
									}
									else rowData[i]="True";
								}
								else if (i == 6) {
									double product = row.getCell(5).getNumericCellValue() * row.getCell(6).getNumericCellValue();
									rowData[i] = product;
								}
								else if (cell.getCellType() == CellType.FORMULA) {
									rowData[i] = cell.getCellFormula();
								} else if (cell.getCellType() == CellType.NUMERIC) {
									rowData[i] = (int) cell.getNumericCellValue();
								} else {
									rowData[i] = cell.getStringCellValue();
								}
							}
							matchingRows.add(rowData);
						}
					}
				}
			}
		}
		Object[][] data = new Object[matchingRows.size() + 1][12];
		data[0] = dataFromRowThree[0]; // copy the header from row 3
		for (int i = 0; i < matchingRows.size(); i++) {
			data[i + 1] = matchingRows.get(i); // copy the matching rows
		}
		populateTable(data);

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
	private void FieldsThatOnlyHandleNumbers() {
		fromField.setDocument(new NumericFilter());
		toField.setDocument(new NumericFilter());
	}
}