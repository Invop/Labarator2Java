package com.automatedworkspace.inventorymanagement.ui.DeleteItem;

import com.automatedworkspace.inventorymanagement.statistics.Config;
import com.automatedworkspace.inventorymanagement.statistics.ConfigManager;
import com.automatedworkspace.inventorymanagement.statistics.DeliveryConfig;
import com.automatedworkspace.inventorymanagement.ui.Nomenclature.Delivery;
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


/**
 * The type Delete item form.
 */
public class DeleteItemForm extends JDialog {
	/**
	 * The Delete item panel.
	 */
	private JPanel DeleteItemPanel;
	/**
	 * The Delete item combo box.
	 */
	private JComboBox<String> DeleteItemComboBox;
	/**
	 * The Ok delete item button.
	 */
	private JButton OKDeleteItemButton;
	/**
	 * The Cancel delete item button.
	 */
	private JButton CancelDeleteItemButton;

	/**
	 * Instantiates a new Delete item form.
	 *
	 * @param parent the parent
	 */
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

	/**
	 * If cancel pressed.
	 */
	private void IfCancelPressed() {
		CancelDeleteItemButton.addActionListener(e -> {
			dispose();
			new SelectionDeleteForm(null);
		});

	}

	/**
	 * Add to combo box.
	 *
	 * @throws IOException the io exception
	 */
	private void addToComboBox() throws IOException {
		Config config = ConfigManager.readConfig();
		List<String> nameList = config.getNamesList();
		DeleteItemComboBox.removeAllItems();
		for (String name : nameList) {
			DeleteItemComboBox.addItem(name);
		}
	}

	/**
	 * Delete item.
	 *
	 * @throws IOException the io exception
	 */
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

				DeliveryConfig configDel = ConfigManager.readInOut();
				List<Delivery> deliveriesIN = configDel.getDeliveries();
				List<Delivery> deliveriesOut = configDel.getDeliveries();
				for (Delivery delivery : deliveriesOut) {
					if (delivery.getName().equals(config.getNamesList().get(selectedIdx))) {
						delivery.setGroupIndex(-1);
						delivery.setSupplierIndex(-1);
						delivery.setName(config.getNamesList().get(selectedIdx) + " Removed");
					}
				}
				for (Delivery delivery : deliveriesIN) {
					if (delivery.getName().equals(config.getNamesList().get(selectedIdx))) {
						delivery.setGroupIndex(-1);
						delivery.setSupplierIndex(-1);
						delivery.setName(config.getNamesList().get(selectedIdx) + " Removed");
					}
				}
				configDel.setDeliveries(deliveriesIN);
				configDel.setDeliveriesOut(deliveriesOut);
				ConfigManager.writeInOut(configDel);
				// remove the item from config
				config.getNamesList().remove(selectedIdx);
				config.getLimitList().remove(selectedIdx);
				config.getIDList().remove(selectedIdx);
				config.getIntervalList().remove(selectedIdx);
				config.setNotNullRows(config.getNotNullRows() - 1);
				config.getItemGroupList().remove(selectedIdx);
				ConfigManager.writeConfig(config);

			}
		}


	}

}