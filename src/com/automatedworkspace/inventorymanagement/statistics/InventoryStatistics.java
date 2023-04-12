package com.automatedworkspace.inventorymanagement.statistics;


import com.automatedworkspace.inventorymanagement.ui.InventoryManagementUI;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.automatedworkspace.inventorymanagement.ui.AddItem.AddItemForm.EXEL_FILE_PATH;


public class InventoryStatistics extends JDialog {


	private JPanel panelMain;
	private JButton buttonHome;
	private JRadioButton radioButtonByGroup;
	private JRadioButton supplierStatisticsRadioButton;
	private JRadioButton radioButtonAllStatistic;
	private JComboBox comboBoxGroupSupplier;
	private JLabel labelSum;
	private JTable table;
	private JLabel labelSales;
	private JLabel labelPurchases;

	public InventoryStatistics(JFrame parent) {
		super(parent);
		setVisible(true);
		setSize(1280, 720);
		setContentPane(panelMain);
		setLocationRelativeTo(parent);
		radioListeners();

		ExecutorService executorService = Executors.newCachedThreadPool();
		executorService.submit(() -> {
			try {
				getSales();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		executorService.submit(() -> {
			try {
				getPurchases();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		executorService.submit(() -> {
			try {
				getSum();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		executorService.shutdown();
		buttonHome.addActionListener(e -> {
			dispose();
			new InventoryManagementUI(null);
		});
	}

	private void radioListeners() {
		radioButtonAllStatistic.addActionListener(e -> onRadioSelected(radioButtonAllStatistic));
		supplierStatisticsRadioButton.addActionListener(e -> onRadioSelected(supplierStatisticsRadioButton));
		radioButtonByGroup.addActionListener(e -> onRadioSelected(radioButtonByGroup));
	}

	private void onRadioSelected(JRadioButton selectedRadioButton) {
		List<JRadioButton> radioButtons = Arrays.asList(radioButtonAllStatistic, supplierStatisticsRadioButton, radioButtonByGroup);
		radioButtons.stream().filter(r -> !r.equals(selectedRadioButton)).forEach(r -> r.setSelected(false));

		if (selectedRadioButton.equals(radioButtonAllStatistic)) {
			System.gc();
			comboBoxGroupSupplier.removeAllItems();
			try {
				showAllStatOut();

			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else if (selectedRadioButton.equals(supplierStatisticsRadioButton) || selectedRadioButton.equals(radioButtonByGroup)) {
			comboBoxGroupSupplier.removeAllItems();
			Config config;
			try {
				config = ConfigManager.readConfig();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			if (selectedRadioButton.equals(radioButtonByGroup)) {

				System.gc();
				List<String> groupList = config.getGroupList();
				ActionListener[] listeners = comboBoxGroupSupplier.getActionListeners();
				for (ActionListener listener : listeners) {
					comboBoxGroupSupplier.removeActionListener(listener);
				}
				for (String group : groupList) {
					comboBoxGroupSupplier.addItem(group);
				}
				comboBoxGroupSupplier.addActionListener(e -> {
					try {
						showGroupStatOut();

					} catch (IOException ex) {
						throw new RuntimeException(ex);
					}
				});
			} else {
				System.gc();
				List<String> supplierList = config.getSupplierList();
				ActionListener[] listeners = comboBoxGroupSupplier.getActionListeners();
				for (ActionListener listener : listeners) {
					comboBoxGroupSupplier.removeActionListener(listener);
				}
				for (String supp : supplierList) {
					comboBoxGroupSupplier.addItem(supp);
				}
				comboBoxGroupSupplier.addActionListener(e -> {
					try {
						showSupplierStatOut();

					} catch (IOException ex) {
						throw new RuntimeException(ex);
					}
				});
			}

		}
	}

	private void populateTable(Object[][] data) {
		DefaultTableModel model = new DefaultTableModel(data, new Object[]{"Item", "Size", "Date", "Group", "Supplier"});

		table.setModel(model);
		for (int i = model.getRowCount() - 1; i >= 0; i--) {
			boolean isEmpty = true;
			for (int j = 0; j < model.getColumnCount(); j++) {
				if (model.getValueAt(i, j) != null && !model.getValueAt(i, j).toString().isEmpty()) {
					isEmpty = false;
					break;
				}
			}
			if (isEmpty) {
				model.removeRow(i);
			}
		}


	}

	private Object[][] showStatGroupIn() throws IOException {
		DeliveryConfig config = ConfigManager.readInOut();
		Config configMain = ConfigManager.readConfig();
		int rows = config.getDeliveries().size();
		int indx = comboBoxGroupSupplier.getSelectedIndex();
		Object[][] data = null;
		data = new Object[rows][5];
		for (int i = 0; i < rows; i++) {
			if (config.getDeliveries().get(i).getGroupIndex() == indx) {
				data[i][0] = config.getDeliveries().get(i).getName();
				data[i][1] = "+" + config.getDeliveries().get(i).getSize();
				data[i][2] = config.getDeliveries().get(i).getDate();
				data[i][3] = configMain.getGroupList().get(config.getDeliveries().get(i).getGroupIndex());
				data[i][4] = configMain.getSupplierList().get(config.getDeliveries().get(i).getSupplierIndex());
			}
		}
		return data;
	}

	private void showGroupStatOut() throws IOException {
		DeliveryConfig config = ConfigManager.readInOut();
		Config configMain = ConfigManager.readConfig();
		int rows = config.getDeliveriesOut().size();
		int indx = comboBoxGroupSupplier.getSelectedIndex();
		Object[][] dataIn = showStatGroupIn();
		System.out.println(indx);
		Object[][] data = new Object[rows][5];

		for (int i = 0; i < rows; i++) {
			if (config.getDeliveriesOut().get(i).getGroupIndex() == indx) {
				data[i][0] = config.getDeliveriesOut().get(i).getName();
				data[i][1] = "-" + config.getDeliveriesOut().get(i).getSize();
				data[i][2] = config.getDeliveriesOut().get(i).getDate();
				data[i][3] = configMain.getGroupList().get(config.getDeliveriesOut().get(i).getGroupIndex());
				data[i][4] = configMain.getSupplierList().get(config.getDeliveriesOut().get(i).getSupplierIndex());
			}


		}
		Object[][] updatedData = new Object[dataIn.length + rows][5];
		System.arraycopy(dataIn, 0, updatedData, 0, dataIn.length);
		for (int i = 0; i < data.length; i++) {
			updatedData[dataIn.length + i] = data[i];
		}
		populateTable(updatedData);
	}

	private Object[][] showSupplierStatIn() throws IOException {
		DeliveryConfig config = ConfigManager.readInOut();
		Config configMain = ConfigManager.readConfig();
		int rows = config.getDeliveries().size();
		int indx = comboBoxGroupSupplier.getSelectedIndex();
		Object[][] data = null;
		data = new Object[rows][5];
		for (int i = 0; i < rows; i++) {
			if (config.getDeliveries().get(i).getSupplierIndex() == indx) {
				data[i][0] = config.getDeliveries().get(i).getName();
				data[i][1] = "+" + config.getDeliveries().get(i).getSize();
				data[i][2] = config.getDeliveries().get(i).getDate();
				data[i][3] = configMain.getGroupList().get(config.getDeliveries().get(i).getGroupIndex());
				data[i][4] = configMain.getSupplierList().get(config.getDeliveries().get(i).getSupplierIndex());
			}
		}
		return data;
	}

	private void showSupplierStatOut() throws IOException {
		DeliveryConfig config = ConfigManager.readInOut();
		Config configMain = ConfigManager.readConfig();
		int rows = config.getDeliveriesOut().size();
		int indx = comboBoxGroupSupplier.getSelectedIndex();
		Object[][] dataIn = showSupplierStatIn();

		Object[][] data = new Object[rows][5];
		for (int i = 0; i < rows; i++) {
			if (config.getDeliveriesOut().get(i).getSupplierIndex() == indx) {
				data[i][0] = config.getDeliveriesOut().get(i).getName();
				data[i][1] = "-" + config.getDeliveriesOut().get(i).getSize();
				data[i][2] = config.getDeliveriesOut().get(i).getDate();
				data[i][3] = configMain.getGroupList().get(config.getDeliveriesOut().get(i).getGroupIndex());
				data[i][4] = configMain.getSupplierList().get(config.getDeliveriesOut().get(i).getSupplierIndex());
			}
		}
		Object[][] updatedData = new Object[dataIn.length + rows][5];
		System.arraycopy(dataIn, 0, updatedData, 0, dataIn.length);
		for (int i = 0; i < data.length; i++) {
			updatedData[dataIn.length + i] = data[i];
		}
		populateTable(updatedData);
	}

	private Object[][] showAllStatIN() throws IOException {
		Config configMain = ConfigManager.readConfig();
		DeliveryConfig config = ConfigManager.readInOut();
		int rows = config.getDeliveries().size();
		Object[][] data = null;
		data = new Object[rows][5];
		for (int i = 0; i < rows; i++) {
			data[i][0] = config.getDeliveries().get(i).getName();
			data[i][1] = "+" + config.getDeliveries().get(i).getSize();
			data[i][2] = config.getDeliveries().get(i).getDate();
			data[i][3] = configMain.getGroupList().get(config.getDeliveries().get(i).getGroupIndex());
			data[i][4] = configMain.getSupplierList().get(config.getDeliveries().get(i).getSupplierIndex());
		}
		return data;
	}

	private void showAllStatOut() throws IOException {
		Config configMain = ConfigManager.readConfig();
		DeliveryConfig config = ConfigManager.readInOut();
		int rows = config.getDeliveriesOut().size();
		Object[][] dataIn = showAllStatIN();

		Object[][] data = new Object[rows][5];
		for (int i = 0; i < rows; i++) {
			System.out.println(1);
			data[i][0] = config.getDeliveriesOut().get(i).getName();
			data[i][1] = "-" + config.getDeliveriesOut().get(i).getSize();
			data[i][2] = config.getDeliveriesOut().get(i).getDate();
			data[i][3] = configMain.getGroupList().get(config.getDeliveriesOut().get(i).getGroupIndex());
			data[i][4] = configMain.getSupplierList().get(config.getDeliveriesOut().get(i).getSupplierIndex());
		}

		Object[][] updatedData = new Object[dataIn.length + rows][5];
		System.arraycopy(dataIn, 0, updatedData, 0, dataIn.length);
		for (int i = 0; i < data.length; i++) {
			updatedData[dataIn.length + i] = data[i];
		}
		populateTable(updatedData);
	}

	private void getSales() throws IOException {
		DeliveryConfig config = ConfigManager.readInOut();
		int totalSales = 0;
		for (int i = 0; i < config.getDeliveriesOut().size(); i++) {
			totalSales += config.getDeliveriesOut().get(i).getSize();
		}
		labelSales.setText("Total Sales = " + totalSales);
	}

	private void getPurchases() throws IOException {
		DeliveryConfig config = ConfigManager.readInOut();
		int totalPurchases = 0;
		for (int i = 0; i <= config.getDeliveries().size() - 1; i++) {
			totalPurchases += config.getDeliveries().get(i).getSize();
		}
		labelPurchases.setText("Total Purchases = " + totalPurchases);
	}

	private void getSum() throws IOException {
		Config config = ConfigManager.readConfig();
		// Open the Excel workbook
		FileInputStream filePath = new FileInputStream(EXEL_FILE_PATH);
		Workbook workbook = WorkbookFactory.create(filePath);
		Sheet sheet = workbook.getSheetAt(0);

		int sum = 0;
		for (int i = 3; i < config.getNotNullRows(); i++) {
			Row row = sheet.getRow(i);
			sum += row.getCell(5).getNumericCellValue() * row.getCell(6).getNumericCellValue();
		}
		labelSum.setText("Sum = " + sum);
		filePath.close();
		workbook.close();
	}

}