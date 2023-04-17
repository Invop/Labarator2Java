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


/**
 * The type Inventory statistics.
 */
public class InventoryStatistics extends JDialog {


	/**
	 * The Panel main.
	 */
	private JPanel panelMain;
	/**
	 * The Button home.
	 */
	private JButton buttonHome;
	/**
	 * The Radio button by group.
	 */
	private JRadioButton radioButtonByGroup;
	/**
	 * The Supplier statistics radio button.
	 */
	private JRadioButton supplierStatisticsRadioButton;
	/**
	 * The Radio button all statistic.
	 */
	private JRadioButton radioButtonAllStatistic;
	/**
	 * The Combo box group supplier.
	 */
	private JComboBox<String> comboBoxGroupSupplier;
	/**
	 * The Label sum.
	 */
	private JLabel labelSum;
	/**
	 * The Table.
	 */
	private JTable table;
	/**
	 * The Label sales.
	 */
	private JLabel labelSales;
	/**
	 * The Label purchases.
	 */
	private JLabel labelPurchases;

	/**
	 * Instantiates a new Inventory statistics.
	 *
	 * @param parent the parent
	 */
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

	/**
	 * Radio listeners.
	 */
	private void radioListeners() {
		radioButtonAllStatistic.addActionListener(e -> onRadioSelected(radioButtonAllStatistic));
		supplierStatisticsRadioButton.addActionListener(e -> onRadioSelected(supplierStatisticsRadioButton));
		radioButtonByGroup.addActionListener(e -> onRadioSelected(radioButtonByGroup));
	}

	/**
	 * On radio selected.
	 *
	 * @param selectedRadioButton the selected radio button
	 */
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

	/**
	 * Populate table.
	 *
	 * @param data the data
	 */
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

	/**
	 * Show stat group in object [ ] [ ].
	 *
	 * @return the object [ ] [ ]
	 * @throws IOException the io exception
	 */
	private Object[][] showStatGroupIn() throws IOException {
		DeliveryConfig config = ConfigManager.readInOut();
		Config configMain = ConfigManager.readConfig();
		int rows = config.getDeliveries().size();
		int indx = comboBoxGroupSupplier.getSelectedIndex();
		Object[][] data;
		data = new Object[rows][5];
		for (int i = 0; i < rows; i++) {
			if (config.getDeliveries().get(i).getGroupIndex() == indx) {
				data[i][0] = config.getDeliveries().get(i).getName();
				data[i][1] = "+" + config.getDeliveries().get(i).getSize();
				data[i][2] = config.getDeliveries().get(i).getDate();
				int groupIndex = config.getDeliveries().get(i).getGroupIndex();
				String group = null;
				if (groupIndex >= 0 && groupIndex < configMain.getGroupList().size()) {
					group = configMain.getGroupList().get(groupIndex);
				}
				data[i][3] = group;
				int suppIndex = config.getDeliveries().get(i).getSupplierIndex();
				String supp = null;
				if (suppIndex >= 0 && suppIndex < configMain.getGroupList().size()) {
					supp = configMain.getGroupList().get(suppIndex);
				}
				data[i][4] = supp;
			}
		}
		return data;
	}

	/**
	 * Show group stat out.
	 *
	 * @throws IOException the io exception
	 */
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
				int groupIndex = config.getDeliveries().get(i).getGroupIndex();
				String group = null;
				if (groupIndex >= 0 && groupIndex < configMain.getGroupList().size()) {
					group = configMain.getGroupList().get(groupIndex);
				}
				data[i][3] = group;
				int suppIndex = config.getDeliveries().get(i).getSupplierIndex();
				String supp = null;
				if (suppIndex >= 0 && suppIndex < configMain.getGroupList().size()) {
					supp = configMain.getGroupList().get(suppIndex);
				}
				data[i][4] = supp;
			}


		}
		Object[][] updatedData = new Object[dataIn.length + rows][5];
		System.arraycopy(dataIn, 0, updatedData, 0, dataIn.length);
		System.arraycopy(data, 0, updatedData, dataIn.length, data.length);
		populateTable(updatedData);
	}

	/**
	 * Show supplier stat in object [ ] [ ].
	 *
	 * @return the object [ ] [ ]
	 * @throws IOException the io exception
	 */
	private Object[][] showSupplierStatIn() throws IOException {
		DeliveryConfig config = ConfigManager.readInOut();
		Config configMain = ConfigManager.readConfig();
		int rows = config.getDeliveries().size();
		int indx = comboBoxGroupSupplier.getSelectedIndex();
		Object[][] data;
		data = new Object[rows][5];
		for (int i = 0; i < rows; i++) {
			if (config.getDeliveries().get(i).getSupplierIndex() == indx) {
				data[i][0] = config.getDeliveries().get(i).getName();
				data[i][1] = "+" + config.getDeliveries().get(i).getSize();
				data[i][2] = config.getDeliveries().get(i).getDate();
				int groupIndex = config.getDeliveries().get(i).getGroupIndex();
				String group = null;
				if (groupIndex >= 0 && groupIndex < configMain.getGroupList().size()) {
					group = configMain.getGroupList().get(groupIndex);
				}
				data[i][3] = group;
				int suppIndex = config.getDeliveries().get(i).getSupplierIndex();
				String supp = null;
				if (suppIndex >= 0 && suppIndex < configMain.getGroupList().size()) {
					supp = configMain.getGroupList().get(suppIndex);
				}
				data[i][4] = supp;
			}
		}
		return data;
	}

	/**
	 * Show supplier stat out.
	 *
	 * @throws IOException the io exception
	 */
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
				int groupIndex = config.getDeliveries().get(i).getGroupIndex();
				String group = null;
				if (groupIndex >= 0 && groupIndex < configMain.getGroupList().size()) {
					group = configMain.getGroupList().get(groupIndex);
				}
				data[i][3] = group;
				int suppIndex = config.getDeliveries().get(i).getSupplierIndex();
				String supp = null;
				if (suppIndex >= 0 && suppIndex < configMain.getGroupList().size()) {
					supp = configMain.getGroupList().get(suppIndex);
				}
				data[i][4] = supp;
			}
		}
		Object[][] updatedData = new Object[dataIn.length + rows][5];
		System.arraycopy(dataIn, 0, updatedData, 0, dataIn.length);
		System.arraycopy(data, 0, updatedData, dataIn.length, data.length);
		populateTable(updatedData);
	}

	/**
	 * Show all stat in object [ ] [ ].
	 *
	 * @return the object [ ] [ ]
	 * @throws IOException the io exception
	 */
	private Object[][] showAllStatIN() throws IOException {
		Config configMain = ConfigManager.readConfig();
		DeliveryConfig config = ConfigManager.readInOut();
		int rows = config.getDeliveries().size();
		Object[][] data;
		data = new Object[rows][5];
		for (int i = 0; i < rows; i++) {
			data[i][0] = config.getDeliveries().get(i).getName();
			data[i][1] = "+" + config.getDeliveries().get(i).getSize();
			data[i][2] = config.getDeliveries().get(i).getDate();
			int groupIndex = config.getDeliveries().get(i).getGroupIndex();
			String group = null;
			if (groupIndex >= 0 && groupIndex < configMain.getGroupList().size()) {
				group = configMain.getGroupList().get(groupIndex);
			}
			data[i][3] = group;
			int suppIndex = config.getDeliveries().get(i).getSupplierIndex();
			String supp = null;
			if (suppIndex >= 0 && suppIndex < configMain.getGroupList().size()) {
				supp = configMain.getGroupList().get(suppIndex);
			}
			data[i][4] = supp;
		}
		return data;
	}

	/**
	 * Show all stat out.
	 *
	 * @throws IOException the io exception
	 */
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
			int groupIndex = config.getDeliveries().get(i).getGroupIndex();
			String group = null;
			if (groupIndex >= 0 && groupIndex < configMain.getGroupList().size()) {
				group = configMain.getGroupList().get(groupIndex);
			}
			data[i][3] = group;
			int suppIndex = config.getDeliveries().get(i).getSupplierIndex();
			String supp = null;
			if (suppIndex >= 0 && suppIndex < configMain.getGroupList().size()) {
				supp = configMain.getGroupList().get(suppIndex);
			}
			data[i][4] = supp;
		}

		Object[][] updatedData = new Object[dataIn.length + rows][5];
		System.arraycopy(dataIn, 0, updatedData, 0, dataIn.length);
		System.arraycopy(data, 0, updatedData, dataIn.length, data.length);
		populateTable(updatedData);
	}

	/**
	 * Gets sales.
	 *
	 * @throws IOException the io exception
	 */
	private void getSales() throws IOException {
		DeliveryConfig config = ConfigManager.readInOut();
		int totalSales = 0;
		for (int i = 0; i < config.getDeliveriesOut().size(); i++) {
			totalSales += config.getDeliveriesOut().get(i).getSize();
		}
		labelSales.setText("Total Sales = " + totalSales);
	}

	/**
	 * Gets purchases.
	 *
	 * @throws IOException the io exception
	 */
	private void getPurchases() throws IOException {
		DeliveryConfig config = ConfigManager.readInOut();
		int totalPurchases = 0;
		for (int i = 0; i <= config.getDeliveries().size() - 1; i++) {
			totalPurchases += config.getDeliveries().get(i).getSize();
		}
		labelPurchases.setText("Total Purchases = " + totalPurchases);
	}

	/**
	 * Gets sum.
	 *
	 * @throws IOException the io exception
	 */
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