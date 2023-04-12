package com.automatedworkspace.inventorymanagement.ui.AddItem;

import com.automatedworkspace.inventorymanagement.statistics.Config;
import com.automatedworkspace.inventorymanagement.statistics.ConfigManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;

public class AddSupplierForm extends JDialog {
	private JPanel AddSupplierPanel;
	private JLabel NameSupplierLabel;
	private JTextField NameSupplierField;
	private JButton CancelAddSupplierButton;
	private JButton OKAddSupplierButton;

	public AddSupplierForm(JFrame parent) {
		super(parent);
		setVisible(true);
		setSize(400, 300);
		setContentPane(AddSupplierPanel);
		setLocationRelativeTo(parent);
		OKAddSupplierButton.setEnabled(false);
		Listener();
		IfOkPressed();
		IfCancelPressed();
		CloseApp();
	}

	private void Listener() {
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
		NameSupplierField.getDocument().addDocumentListener(documentListener);

		OKAddSupplierButton.addActionListener(e -> {
			// remove document listener from each text field
			NameSupplierField.getDocument().removeDocumentListener(documentListener);

		});
	}

	private void checkFields() {
		OKAddSupplierButton.setEnabled(!NameSupplierField.getText().isEmpty());
	}

	private void addSupplierToConfig(String newSupplier) throws IOException {
		// Read the config file
		Config config = ConfigManager.readConfig();

		// Check if the group already exists in the config file
		List<String> supplierList = config.getSupplierList();
		if (supplierList.contains(newSupplier)) {
			// Ask user for a new group if it already exists
			while (supplierList.contains(newSupplier)) {
				newSupplier = JOptionPane.showInputDialog(null, "Group already exists in config file. Please enter a new Group:");
			}
		}
		NameSupplierField.setText(newSupplier);

		// Add the new group to the list
		supplierList.add(newSupplier);
		config.setSupplierList(supplierList);

		// Write the updated config file
		ConfigManager.writeConfig(config);
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

	private void IfOkPressed() {
		OKAddSupplierButton.addActionListener(e -> {
			try {
				addSupplierToConfig(NameSupplierField.getText());
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
			dispose();
			new SelectionAddForm(null);
		});
	}

	private void IfCancelPressed() {
		CancelAddSupplierButton.addActionListener(e -> {
			dispose();
			new SelectionAddForm(null);
		});

	}
}

