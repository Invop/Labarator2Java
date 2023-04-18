package com.automatedworkspace.inventorymanagement.ui.DeleteItem;

import com.automatedworkspace.inventorymanagement.statistics.Config;
import com.automatedworkspace.inventorymanagement.statistics.ConfigManager;
import org.apache.poi.ss.usermodel.*;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.automatedworkspace.inventorymanagement.ui.AddItem.AddItemForm.EXEL_FILE_PATH;

/**
 * The type Delete supplier form.
 */
public class DeleteSupplierForm extends JDialog {
	/**
	 * The Delete suplplier panel.
	 */
	private JPanel DeleteSuplplierPanel;
	/**
	 * The Delete supplier combo box.
	 */
	private JComboBox<String> DeleteSupplierComboBox;
	/**
	 * The Ok delete supplier button.
	 */
	private JButton OKDeleteSupplierButton;
	/**
	 * The Cancel delete supplier button.
	 */
	private JButton CancelDeleteSupplierButton;

	/**
	 * Instantiates a new Delete supplier form.
	 *
	 * @param parent the parent
	 */
	public DeleteSupplierForm(JFrame parent) {
		super(parent);
		setSize(500, 450);
		setVisible(true);
		setContentPane(DeleteSuplplierPanel);
		setLocationRelativeTo(parent);
		try {
			addToSupplierComboBox();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		IfOkPressed();
		IfCancelPressed();
		CloseApp();
	}

	/**
	 * Add to supplier combo box.
	 *
	 * @throws IOException the io exception
	 */
	private void addToSupplierComboBox() throws IOException {
		// Load the configuration file
		Config config = ConfigManager.readConfig();
		List<String> supplierList = config.getSupplierList();
		for (String supp : supplierList) {
			DeleteSupplierComboBox.addItem(supp);
		}
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
		OKDeleteSupplierButton.addActionListener(e -> {
			try {
				deleteSupplier();
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
		CancelDeleteSupplierButton.addActionListener(e -> {
			dispose();
			new SelectionDeleteForm(null);
		});

	}

	/**
	 * Delete supplier.
	 *
	 * @throws IOException the io exception
	 */
	private void deleteSupplier() throws IOException {
		FileInputStream filePath = new FileInputStream(EXEL_FILE_PATH);
		Workbook workbook = WorkbookFactory.create(filePath);
		Sheet sheet = workbook.getSheetAt(0);

		Config config = ConfigManager.readConfig();
		List<String> suppList = config.getSupplierList();
		List<Integer> itemSuppList = config.getItemSupplierList();
		int selectedIndx = DeleteSupplierComboBox.getSelectedIndex();
		for (int i = itemSuppList.size() - 1; i >= 0; i--) {
			if (itemSuppList.get(i) != -1 && itemSuppList.get(i) > selectedIndx) {
				if (itemSuppList.get(i) != 0) {
					itemSuppList.set(i, itemSuppList.get(i) - 1);
				}
			} else if (itemSuppList.get(i) != -1 && itemSuppList.get(i) == selectedIndx) {
				itemSuppList.set(i, -1);
				for (int j = 3; j <= config.getNotNullRows(); j++) {
					Row row = sheet.getRow(j);
					Cell cell = row.getCell(4);
					if (cell.getStringCellValue().equals(suppList.get(selectedIndx))) {
						cell.setCellValue("null");
					}
				}

			}

		}
		FileOutputStream out = new FileOutputStream(EXEL_FILE_PATH);
		workbook.write(out);
		out.close();
		workbook.close();
		suppList.remove(selectedIndx);
		ConfigManager.writeConfig(config);

	}


}
