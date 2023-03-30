package com.automatedworkspace.inventorymanagement.ui.AddItem;

import com.automatedworkspace.inventorymanagement.statistics.Config;
import com.automatedworkspace.inventorymanagement.statistics.ConfigManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.File;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class AddItemForm extends JDialog {


	private JPanel CreateFormBrand;
	private JLabel AddBrandLabel;
	private JLabel AddIDLabel;
	private JTextField AddIDField;
	private JLabel NameLabel;
	private JTextField AddNameField;
	private JLabel ManufacturerLabel;
	private JComboBox AddManufacturerBox;
	private JLabel PriceLabel;
	private JTextField AddPriceField;
	private JLabel LimitLabel;
	private JTextField AddLimitField;
	private JLabel GroupLabel;
	private JComboBox<String> AddGroupBox;
	private JButton OkButton;
	private JButton CancelButton;

	private static final String JSON_FILE_PATH = "src/com/automatedworkspace/files/config.json";
	/**
	 * Instantiates a new Add item form.
	 *
	 * @param parent the parent
	 */
	public AddItemForm(JFrame parent) {
		super(parent);

		setVisible(true);
		setSize(900, 720);
		setContentPane(CreateFormBrand);
		setLocationRelativeTo(parent);
		AddGroupBox.addItem("Item 1");
		AddGroupBox.addItem("Item 2");
		AddManufacturerBox.addItem("Item 1");
		AddManufacturerBox.addItem("Item 2");
		FieldsThatOnlyHandleNumbers();
		Listener();
		IfOkPressed();
		IfCancelPressed();
		CloseApp();
	}

	private void FieldsThatOnlyHandleNumbers() {
		AddPriceField.setDocument(new NumericFilter());
		AddLimitField.setDocument(new NumericFilter());
	}


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

		// add action listener to the "OK" button to clear the memory from the listener
		OkButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// remove document listener from each text field
				AddIDField.getDocument().removeDocumentListener(documentListener);
				AddNameField.getDocument().removeDocumentListener(documentListener);
				AddPriceField.getDocument().removeDocumentListener(documentListener);
				AddLimitField.getDocument().removeDocumentListener(documentListener);
			}
		});
	}
	private void checkFields() {
		if (AddIDField.getText().isEmpty() || AddNameField.getText().isEmpty() ||
				AddPriceField.getText().isEmpty() || AddLimitField.getText().isEmpty()) {
			OkButton.setEnabled(false);
		} else {
			OkButton.setEnabled(true);
		}
	}
	/**
	 * System.exit(0)
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
	private void IfOkPressed(){
		OkButton.addActionListener(e -> {
			try {
				addRowToExcelTable();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
			dispose();
		});
	}
	private void IfCancelPressed() {
		CancelButton.addActionListener(e -> {
			dispose();
			// new AddToExistingForm(null);
		});

	}

	public void addRowToExcelTable() throws IOException {
		// Get the current not null rows from the config file
		Config config = ConfigManager.readConfig();
		int notNullRows = config.getNotNullRows();

		// Open the Excel workbook
		FileInputStream filePath = new FileInputStream("src/com/automatedworkspace/files/Inventory.xlsx");
		Workbook workbook = WorkbookFactory.create(filePath);
		Sheet sheet = workbook.getSheetAt(0);

		// Find the first empty row
		int row = notNullRows;
		while (sheet.getRow(row) != null && sheet.getRow(row).getCell(2) != null && !sheet.getRow(row).getCell(3).toString().isEmpty()) {
			row++;
		}
		// Fill in the row with data
		Row newRow = sheet.getRow(row);
		if (newRow == null) {
			newRow = sheet.createRow(row);
		}
		Cell cell = newRow.getCell(2);
		if (cell == null) {
			cell = newRow.createCell(2);
		}
		cell.setCellValue(AddIDField.getText());

		cell = newRow.getCell(3);
		if (cell == null) {
			cell = newRow.createCell(3);
		}
		cell.setCellValue(AddNameField.getText());

		cell = newRow.getCell(4);
		if (cell == null) {
			cell = newRow.createCell(4);
		}
		cell.setCellValue((String) AddManufacturerBox.getSelectedItem());

		cell = newRow.getCell(5);
		if (cell == null) {
			cell = newRow.createCell(5);
		}
		cell.setCellValue(Integer.valueOf(AddPriceField.getText()));

		cell = newRow.getCell(8);
		if (cell == null) {
			cell = newRow.createCell(8);
		}
		cell.setCellValue(Integer.valueOf(AddLimitField.getText()));

		cell = newRow.getCell(12);
		if (cell == null) {
			cell = newRow.createCell(12);
		}
		cell.setCellValue((String) AddGroupBox.getSelectedItem());


		// Update the config file
		config.setNotNullRows(notNullRows + 1);
		ConfigManager.writeConfig(config);

		// Save the workbook
		FileOutputStream out = new FileOutputStream("src/com/automatedworkspace/files/Inventory.xlsx");
		workbook.write(out);
		out.close();
		workbook.close();
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
