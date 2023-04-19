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
 * The type Delete group form.
 */
public class DeleteGroupForm extends JDialog {
	/**
	 * The Delete group panel.
	 */
	private JPanel DeleteGroupPanel;
	/**
	 * The Delete group combo box.
	 */
	private JComboBox<String> DeleteGroupComboBox;
	/**
	 * The Ok delete group button 1.
	 */
	private JButton OkDeleteGroupButton1;
	/**
	 * The Cancel delete group button.
	 */
	private JButton CancelDeleteGroupButton;

	/**
	 * Instantiates a new Delete group form.
	 *
	 * @param parent the parent
	 */
	public DeleteGroupForm(JFrame parent) {
		super(parent);
		setSize(500, 450);
		setVisible(true);
		setContentPane(DeleteGroupPanel);
		setLocationRelativeTo(parent);
		try {
			addGroupToComboBox();
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
		OkDeleteGroupButton1.addActionListener(e -> {
			try {
				deleteGroup();
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
		CancelDeleteGroupButton.addActionListener(e -> {
			dispose();
			new SelectionDeleteForm(null);
		});

	}

	/**
	 * Add group to combo box.
	 *
	 * @throws IOException the io exception
	 */
	private void addGroupToComboBox() throws IOException {
		// Load the configuration file
		Config config = ConfigManager.readConfig();
		List<String> groupList = config.getGroupList();

		// Add the groups to the JComboBox
		for (String group : groupList) {
			DeleteGroupComboBox.addItem(group);
		}
	}

	/**
	 * Delete group.
	 *
	 * @throws IOException the io exception
	 */
	private void deleteGroup() throws IOException {
		Config config = ConfigManager.readConfig();
		List<String> GroupList = config.getGroupList();
		int selectedIdx = DeleteGroupComboBox.getSelectedIndex();
		List<Integer> itemGroupList = config.getItemGroupList();
		DeliveryConfig deliveryConfig = ConfigManager.readInOut();
		List<Delivery> deliveriesIn = deliveryConfig.getDeliveries();
		List<Delivery> deliveriesOut = deliveryConfig.getDeliveriesOut();
		// Remove row from Excel table
		FileInputStream filePath = new FileInputStream(EXEL_FILE_PATH);
		Workbook workbook = WorkbookFactory.create(filePath);
		XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
		for (int i = itemGroupList.size() - 1; i >= 0; i--) {
			if (itemGroupList.get(i) == selectedIdx) {
				int index = i + 3;
				Row row = sheet.getRow(index);
				sheet.removeRow(row);
				sheet.shiftRows(index + 1, sheet.getLastRowNum(), -1);
				// remove the item from config
				config.getNamesList().remove(i);
				config.getLimitList().remove(i);
				config.getIDList().remove(i);
				config.getIntervalList().remove(i);
				config.setNotNullRows(config.getNotNullRows() - 1);
				config.getItemGroupList().remove(i);
				config.getItemSupplierList().remove(i);

			}
		}
		for (Delivery delivery : deliveriesIn) {
			if (delivery.getGroupIndex() == selectedIdx) {
				delivery.setGroupIndex(-1);
				delivery.setName(config.getNamesList().get(selectedIdx) + " Removed");
			}
		}
		for (Delivery delivery : deliveriesOut) {
			if (delivery.getGroupIndex() == selectedIdx) {
				delivery.setGroupIndex(-1);
				delivery.setName(config.getNamesList().get(selectedIdx) + " Removed");
			}
		}
		deliveryConfig.setDeliveriesOut(deliveriesOut);
		deliveryConfig.setDeliveries(deliveriesIn);
		ConfigManager.writeInOut(deliveryConfig);
		workbook.setForceFormulaRecalculation(true);
		filePath.close();
		GroupList.remove(selectedIdx);
		ConfigManager.writeConfig(config);
		FileOutputStream out = new FileOutputStream(EXEL_FILE_PATH);
		workbook.write(out);
		out.close();
		workbook.close();
		ConfigManager.writeConfig(config);
	}
}