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

public class AddGroupForm extends JDialog {
	private JPanel AddGroupPanel;
	private JLabel NameGroupLabel;
	private JTextField NameGroupField;
	private JButton OKAddGroupButton;
	private JButton CancelAddGroupButton;

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

	private void checkFields() {
		OKAddGroupButton.setEnabled(!NameGroupField.getText().isEmpty());
	}

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

	private void IfCancelPressed() {
		CancelAddGroupButton.addActionListener(e -> {
			dispose();
			new SelectionAddForm(null);
		});

	}
}
