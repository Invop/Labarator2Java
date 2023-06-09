package com.automatedworkspace.inventorymanagement.ui.EditItem;

import com.automatedworkspace.inventorymanagement.statistics.Config;
import com.automatedworkspace.inventorymanagement.statistics.ConfigManager;
import org.apache.poi.ss.usermodel.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.automatedworkspace.inventorymanagement.ui.AddItem.AddItemForm.EXEL_FILE_PATH;


public class EditGroupForm extends JDialog {
	private JPanel mainPanel;
	private JComboBox<String> groupComboBox;
	private JLabel nameLabel;
	private JTextField nameTextField;
	private JButton okButton;
	private JButton cancelButton;

	public EditGroupForm(JFrame parent) {
		super(parent);
		setSize(500, 450);
		setVisible(true);
		setContentPane(mainPanel);
		setLocationRelativeTo(parent);
		okButton.setEnabled(false);
		try {
			AddGroupsToComboBox();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Listener();
		IfOkPressed();
		IfCancelPressed();
		CloseApp();
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
		okButton.addActionListener(e -> {
			try {
				EchangeGroup();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
			dispose();
			new SelectionEditForm(null);
		});
	}

	private void IfCancelPressed() {
		cancelButton.addActionListener(e -> {
			dispose();
			new SelectionEditForm(null);
		});

	}

	private void AddGroupsToComboBox() throws IOException {
		// Read the config file
		Config config = ConfigManager.readConfig();
		// Check if the name already exists in the config file
		List<String> groupList = config.getGroupList();

		if (groupList.isEmpty()) {
			// No items in the list
			JOptionPane.showMessageDialog(null, "No Groups in the list");
		} else {
			// Add items to the AddGroupBox
			for (String group : groupList) {
				groupComboBox.addItem(group);
			}
		}
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
		nameTextField.getDocument().addDocumentListener(documentListener);
		// add combo box to listener
		groupComboBox.addActionListener((e) -> checkFields());


	}

	private void checkFields() {
		okButton.setEnabled(!nameTextField.getText().isEmpty());
	}

	private void EchangeGroup() throws IOException {
		Config config = ConfigManager.readConfig();

		//List<Integer> limitList = config.get();
		List<String> groupList = config.getGroupList();
		int selectedIndx = groupComboBox.getSelectedIndex();
		String prevName = groupList.get(selectedIndx);
		String newGroup = nameTextField.getText();
		//Integer назва = Integer.parse

		if (groupList.contains(newGroup)) {
			// Ask user for a new name if it already exists
			while (groupList.contains(newGroup)) {
				newGroup = JOptionPane.showInputDialog(null, "Group already exists in config file. Please enter a new Group:");
			}
		}
		if (newGroup != null) {
			newGroup = newGroup.replaceAll("\\s+", "");
		}
		if (newGroup == null || newGroup.equals("")) {
			return;
		}
		groupList.set(selectedIndx, newGroup);
		// Open the Excel workbook
		FileInputStream filePath = new FileInputStream(EXEL_FILE_PATH);
		Workbook workbook = WorkbookFactory.create(filePath);
		Sheet sheet = workbook.getSheetAt(0);
		Row row;
		Cell cell;


		//ім'я груп 12 індекс
		for (int i = 0; i < config.getNotNullRows(); i++) {
			int index = i + 3;
			row = sheet.getRow(index);
			cell = row.getCell(12);

			//якщо є
			if (cell.getStringCellValue().equals(prevName)) {
				cell.setCellValue(newGroup);
			}
			//cell.getNumericCellValue()==
		}
		// Save the workbook & config
		FileOutputStream out = new FileOutputStream(EXEL_FILE_PATH);
		config.setGroupList(groupList);
		ConfigManager.writeConfig(config);
		workbook.write(out);
		out.close();
		workbook.close();
	}
}