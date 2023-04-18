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

/**
 * The type Add supplier form.
 */
public class AddSupplierForm extends JDialog {
	/**
	 * The Add supplier panel.
	 */
	private JPanel AddSupplierPanel;
	/**
	 * The Name supplier field.
	 */
	private JTextField NameSupplierField;
	/**
	 * The Cancel add supplier button.
	 */
	private JButton CancelAddSupplierButton;
	/**
	 * The Ok add supplier button.
	 */
	private JButton OKAddSupplierButton;

	/**
	 * Instantiates a new Add supplier form.
	 *
	 * @param parent the parent
	 */
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

	/**
	 * Listener.
	 */
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

	/**
	 * Check fields.
	 */
	private void checkFields() {
		OKAddSupplierButton.setEnabled(!NameSupplierField.getText().isEmpty());
	}

	/**
	 * Add supplier to config.
	 *
	 * @param newSupplier the new supplier
	 * @throws IOException the io exception
	 */
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
		if(newSupplier!=null) {
			newSupplier = newSupplier.replaceAll("\\s+", "");
			NameSupplierField.setText(newSupplier);
		}
		if (newSupplier==null || newSupplier.equals("")) {
			return;
		}
		NameSupplierField.setText(newSupplier);

		// Add the new group to the list
		supplierList.add(newSupplier);
		config.setSupplierList(supplierList);

		// Write the updated config file
		ConfigManager.writeConfig(config);
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

	/**
	 * If cancel pressed.
	 */
	private void IfCancelPressed() {
		CancelAddSupplierButton.addActionListener(e -> {
			dispose();
			new SelectionAddForm(null);
		});

	}
}

