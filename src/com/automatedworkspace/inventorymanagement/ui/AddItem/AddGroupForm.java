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
 * The type Add group form.
 */
public class AddGroupForm extends JDialog {
	/**
	 * The Add group panel.
	 */
	private JPanel AddGroupPanel;
	/**
	 * The Name group label.
	 */
	private JLabel NameGroupLabel;
	/**
	 * The Name group field.
	 */
	private JTextField NameGroupField;
	/**
	 * The Ok add group button.
	 */
	private JButton OKAddGroupButton;
	/**
	 * The Cancel add group button.
	 */
	private JButton CancelAddGroupButton;

	/**
	 * Instantiates a new Add group form.
	 *
	 * @param parent the parent
	 */
	public AddGroupForm(JFrame parent) {
		super(parent);
		setVisible(true);
		setSize(400, 300);
		setContentPane(AddGroupPanel);
		setLocationRelativeTo(parent);
		OKAddGroupButton.setEnabled(false);
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
		NameGroupField.getDocument().addDocumentListener(documentListener);

		OKAddGroupButton.addActionListener(e -> {
			// remove document listener from each text field
			NameGroupField.getDocument().removeDocumentListener(documentListener);

		});
	}

	/**
	 * Check fields.
	 */
	private void checkFields() {
		OKAddGroupButton.setEnabled(!NameGroupField.getText().isEmpty());
	}

	/**
	 * Add group to config.
	 *
	 * @param newGroup the new group
	 * @throws IOException the io exception
	 */
	private void addGroupToConfig(String newGroup) throws IOException {
		// Read the config file
		Config config = ConfigManager.readConfig();

		// Check if the group already exists in the config file
		List<String> groupList = config.getGroupList();
		if (groupList.contains(newGroup)) {
			// Ask user for a new group if it already exists
			while (groupList.contains(newGroup)) {
				newGroup = JOptionPane.showInputDialog(null, "Group already exists in config file. Please enter a new Group:");
			}
		}
		NameGroupField.setText(newGroup);

		// Add the new group to the list
		groupList.add(newGroup);
		config.setGroupList(groupList);

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
		OKAddGroupButton.addActionListener(e -> {
			try {
				addGroupToConfig(NameGroupField.getText());
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
		CancelAddGroupButton.addActionListener(e -> {
			dispose();
			new SelectionAddForm(null);
		});

	}
}
