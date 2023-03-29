package com.automatedworkspace.inventorymanagement.ui.AddItem;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.event.*;
import java.io.*;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class AddItemForm extends JDialog {

	private static int NotNullRows = 3;
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


	private String AddIdText;
	private String AddNameText;
	private int AddPriceInt;
	private int AddLimitInt;
	private String AddManufacturerText;
	private String AddGroupText;


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
				AddNewElementToSheet();
				dispose();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		});
	}
	private void IfCancelPressed() {
		CancelButton.addActionListener(e -> {
			dispose();
			// new AddToExistingForm(null);
		});

	}

	public void AddNewElementToSheet() throws IOException {
		FileInputStream filePath = new FileInputStream("src/com/automatedworkspace/files/Inventory.xlsx");
		Workbook workbook = WorkbookFactory.create(filePath);
		Sheet sheet = workbook.getSheetAt(0);
		int row = NotNullRows;
		while(sheet.getRow(row) != null && sheet.getRow(row).getCell(2) != null && !sheet.getRow(row).getCell(3).toString().isEmpty()) {
			row++;
		}
		NotNullRows=row;
		sheet.getRow(row).getCell(2).setCellValue(AddIDField.getText());
		sheet.getRow(row).getCell(3).setCellValue(AddNameField.getText());
		sheet.getRow(row).getCell(4).setCellValue((String) AddManufacturerBox.getSelectedItem());
		sheet.getRow(row).getCell(5).setCellValue(Integer.valueOf(AddPriceField.getText()));
		sheet.getRow(row).getCell(8).setCellValue(Integer.valueOf(AddLimitField.getText()));
		sheet.getRow(row).getCell(12).setCellValue((String) AddGroupBox.getSelectedItem());
		filePath.close();
		try {

			FileOutputStream out = new FileOutputStream("src/com/automatedworkspace/files/Inventory.xlsx");
			workbook.write(out);
			out.close();
			workbook.close();
		} catch (FileNotFoundException ex) {
			System.out.println("eer");
		}
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
