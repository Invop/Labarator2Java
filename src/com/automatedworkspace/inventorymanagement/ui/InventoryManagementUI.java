package com.automatedworkspace.inventorymanagement.ui;

import com.automatedworkspace.inventorymanagement.ui.AddItem.SelectionAddForm;
import com.automatedworkspace.inventorymanagement.ui.DeleteItem.SelectionDeleteForm;
import com.automatedworkspace.inventorymanagement.ui.Nomenclature.AddToExistingForm;
import com.automatedworkspace.inventorymanagement.ui.Nomenclature.WriteOffFromExistingForm;
import org.apache.poi.ss.usermodel.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
	private JComboBox comboBoxGroupSupplier;
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
				if(byNameRadioButton.isSelected())displayInfoInTableNameID(true);
				if(byIdRadioButton.isSelected())displayInfoInTableNameID(false);
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
			showSupplierOrGroupFields();
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
				JOptionPane.showMessageDialog(this, "Please enter a name to search");
				return;
			}
			else{
				JOptionPane.showMessageDialog(this, "Please enter an ID to search");
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
					JOptionPane.showMessageDialog(this, "Name not found");
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
					if(row.getCell(5).getNumericCellValue()>=row.getCell(7).getNumericCellValue() || row.getCell(11).getStringCellValue().equals("Так")){
						searchData[0][i]="True";
					}
					else searchData[0][i]="False";
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
			JOptionPane.showMessageDialog(this, "Failed to read Excel file: " + e.getMessage());
		}
	}


}
