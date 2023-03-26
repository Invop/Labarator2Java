package com.automatedworkspace.inventorymanagement.ui.AddItem;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


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
		OkButton.setEnabled(false);
		DocumentListener();

		Cancel();
		CloseApp();
	}

	private void FieldsThatOnlyHandleNumbers() {
		AddPriceField.setDocument(new NumericFilter());
		AddLimitField.setDocument(new NumericFilter());
	}

	private void DocumentListener() {
		AddIDField.getDocument().addDocumentListener(new MyDocumentListener());
		AddNameField.getDocument().addDocumentListener(new MyDocumentListener());
		AddPriceField.getDocument().addDocumentListener(new MyDocumentListener());
		AddLimitField.getDocument().addDocumentListener(new MyDocumentListener());
		AddManufacturerBox.addItemListener(new MyItemListener());
		AddGroupBox.addItemListener(new MyItemListener());
	}

	private void checkFields() {
		boolean id = !AddIDField.getText().equals("");
		boolean name = !AddNameField.getText().equals("");
		boolean price = !AddPriceField.getText().equals("");
		boolean limit = !AddLimitField.getText().equals("");
		boolean manufacturer = AddManufacturerBox.getSelectedIndex() != -1;
		boolean group = AddGroupBox.getSelectedIndex() != -1;

		OkButton.setEnabled(id && name && price && limit && manufacturer && group);
	}


	/**
	 * The entry point of application.
	 *
	 * @param args the input arguments
	 */
	public static void main(String[] args) {
		AddItemForm addItemForm = new AddItemForm(null);
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

	private void Cancel() {
		CancelButton.addActionListener(e -> {
			dispose();
			// new SelectiondAddForm(null).setVisible(true);
		});

	}

//sub classes

	/**
	 * The type My document listener.
	 */
	private class MyDocumentListener implements DocumentListener {
		@Override
		public void changedUpdate(DocumentEvent e) {
			checkFields();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			checkFields();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			checkFields();
		}
	}

	/**
	 * The type My item listener.
	 */
	private class MyItemListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			checkFields();
		}
	}

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
