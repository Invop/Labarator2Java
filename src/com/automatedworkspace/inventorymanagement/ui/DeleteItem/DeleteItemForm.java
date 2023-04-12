package com.automatedworkspace.inventorymanagement.ui.DeleteItem;

import com.automatedworkspace.inventorymanagement.statistics.Config;
import com.automatedworkspace.inventorymanagement.statistics.ConfigManager;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.automatedworkspace.inventorymanagement.ui.AddItem.AddItemForm.EXEL_FILE_PATH;


public class DeleteItemForm extends JDialog {
	private JPanel DeleteItemPanel;
	private JLabel DeleteItemLabel;
	private JComboBox<String> DeleteItemComboBox;
	private JButton OKDeleteItemButton;
	private JButton CancelDeleteItemButton;

	public DeleteItemForm(JFrame parent) {
		super(parent);
		setSize(500, 450);
		setVisible(true);
		setContentPane(DeleteItemPanel);
		setLocationRelativeTo(parent);
		try {
			addToComboBox();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
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
		OKDeleteItemButton.addActionListener(e -> {
			try {
				DeleteItem();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
			dispose();
			new SelectionDeleteForm(null);
		});
	}

	private void IfCancelPressed() {
		CancelDeleteItemButton.addActionListener(e -> {
			dispose();
			new SelectionDeleteForm(null);
		});

	}

	private void addToComboBox() throws IOException {
		Config config = ConfigManager.readConfig();
		List<String> nameList = config.getNamesList();
		DeleteItemComboBox.removeAllItems();
		for (String name : nameList) {
			DeleteItemComboBox.addItem(name);
		}
	}

	private void DeleteItem() throws IOException {

		Config config = ConfigManager.readConfig();

		int selectedIdx = DeleteItemComboBox.getSelectedIndex();
		if (selectedIdx != -1) {
			int rowIdx = selectedIdx + 3; // values start from row 3

			FileInputStream filePath = new FileInputStream(EXEL_FILE_PATH);
			Workbook workbook = WorkbookFactory.create(filePath);
			XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
			Row row = sheet.getRow(rowIdx);
			if (row != null) {
				sheet.removeRow(row);

				// shift rows to fill in the gap
				sheet.shiftRows(rowIdx + 1, sheet.getLastRowNum(), -1);
				workbook.setForceFormulaRecalculation(true);
				FileOutputStream out = new FileOutputStream(EXEL_FILE_PATH);
				workbook.write(out);
				out.close();
				workbook.close();
				// remove the item from config
				config.getNamesList().remove(selectedIdx);
				config.getLimitList().remove(selectedIdx);
				config.getIDList().remove(selectedIdx);
				config.getIntervalList().remove(selectedIdx);
				config.setNotNullRows(config.getNotNullRows() - 1);
				config.getItemGroupList().remove(selectedIdx);
				ConfigManager.writeConfig(config);

				DeleteItemComboBox.removeItemAt(selectedIdx);
			}
		}


	}

}
