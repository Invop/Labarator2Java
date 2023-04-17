package com.automatedworkspace.inventorymanagement.ui.Nomenclature;

import com.automatedworkspace.inventorymanagement.FiledFilter.NumericFilter;
import com.automatedworkspace.inventorymanagement.statistics.Config;
import com.automatedworkspace.inventorymanagement.statistics.ConfigManager;
import com.automatedworkspace.inventorymanagement.statistics.DeliveryConfig;
import com.automatedworkspace.inventorymanagement.ui.AddItem.SelectionAddForm;
import com.automatedworkspace.inventorymanagement.ui.InventoryManagementUI;
import com.toedter.calendar.JDateChooser;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.automatedworkspace.inventorymanagement.ui.AddItem.AddItemForm.EXEL_FILE_PATH;


/**
 * The type Add to existing form.
 */
public class AddToExistingForm extends JDialog {
	/**
	 * The Panel add form.
	 */
	private JPanel PanelAddForm;
	/**
	 * The Choose combo box.
	 */
	private JComboBox<String> ChooseComboBox;
	/**
	 * The Number field.
	 */
	private JTextField NumberField;
	/**
	 * The Ok button.
	 */
	private JButton OKButton;
	/**
	 * The Cancel button.
	 */
	private JButton CancelButton;
	/**
	 * The Date panel.
	 */
	private JPanel DatePanel;
	/**
	 * The Date chooser.
	 */
	private final JDateChooser DateChooser = new JDateChooser();

	/**
	 * Instantiates a new Add to existing form.
	 *
	 * @param parent the parent
	 */
	public AddToExistingForm(JFrame parent) {
		super(parent);
		setVisible(true);
		setSize(400, 250);
		setContentPane(PanelAddForm);
		setLocationRelativeTo(parent);
		FieldsThatOnlyHandleNumbers();
		OKButton.setEnabled(false);
		//calendar
		DatePanel.add(DateChooser);
		DateChooser.setMinSelectableDate(new Date()); // Set the minimum date to today's date
		try {
			GetItemNames();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		Listener();
		IfOkPressed();
		IfCancelPressed();
		CloseApp();

	}

	/**
	 * Get item names.
	 *
	 * @throws IOException the io exception
	 */
	private void GetItemNames() throws IOException {
		// Read the config file
		Config config = ConfigManager.readConfig();
		// Check if the name already exists in the config file
		List<String> nameList = config.getNamesList();

		if (nameList.isEmpty()) {
			// No items in the list
			JOptionPane.showMessageDialog(null, "No items in the list");
		} else {
			// Add items to the ChooseComboBox
			for (String name : nameList) {
				ChooseComboBox.addItem(name);
			}
		}
	}

	/**
	 * Fields that only handle numbers.
	 */
	private void FieldsThatOnlyHandleNumbers() {
		NumberField.setDocument(new NumericFilter());
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
		OKButton.addActionListener(e -> {
			try {
				checkLimits();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}

			if (OKButton.isEnabled() && NumberField.getText() != null) dispose();
			new InventoryManagementUI(null);
		});
	}

	/**
	 * If cancel pressed.
	 */
	private void IfCancelPressed() {
		CancelButton.addActionListener(e -> {
			dispose();
			new InventoryManagementUI(null);
		});

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
		NumberField.getDocument().addDocumentListener(documentListener);
		ChooseComboBox.addActionListener((e) -> checkFields());
		OKButton.addActionListener(e -> {
			// remove document listener from each text field
			NumberField.getDocument().removeDocumentListener(documentListener);
		});
	}

	/**
	 * Check fields.
	 */
	private void checkFields() {
		boolean number = !NumberField.getText().isEmpty();
		boolean comboBox = ChooseComboBox.getSelectedIndex() != -1;
		boolean dateSelected = DateChooser.getDate() != null; // check if a date is selected in the DateChooser
		OKButton.setEnabled(number && comboBox && dateSelected);
	}

	/**
	 * Check limits.
	 *
	 * @throws IOException the io exception
	 */
	private void checkLimits() throws IOException {
		Config config = ConfigManager.readConfig();
		List<Integer> limitList = config.getLimitList();
		List<String> nameList = config.getNamesList();
		List<Integer> itemSuppList = config.getItemSupplierList();
		int selectedIdx = ChooseComboBox.getSelectedIndex();
		int limit = limitList.get(selectedIdx);
		int inputNum;
		int response = 0;
		if (itemSuppList.get(selectedIdx) == -1) {
			JOptionPane.showMessageDialog(this, "Постачальника більше не існує, створіть нового та відредагуйте вибраний товар");
			dispose();
			new SelectionAddForm(null);
			return;
		}
		try {
			inputNum = Integer.parseInt(NumberField.getText());
		} catch (NumberFormatException e) {
			inputNum = 0;
		}
		if (inputNum < limit) {
			String name = nameList.get(selectedIdx);
			response = JOptionPane.showConfirmDialog(this, "Ви впевнені, що хочете додати менше " + limit + " одиниць товару '" + name + "'?", "Увага", JOptionPane.YES_NO_OPTION);
		}
		if (response == JOptionPane.YES_OPTION) {
			Date selectedDate = DateChooser.getDate();
			if (!checkDateInterval(config, selectedDate)) {
				return;
			}
			saveDeliveryIn(nameList.get(selectedIdx), inputNum, selectedDate, config.getItemGroupList().get(selectedIdx), config.getItemSupplierList().get(selectedIdx));

			// Open the Excel workbook
			FileInputStream filePath = new FileInputStream(EXEL_FILE_PATH);
			Workbook workbook = WorkbookFactory.create(filePath);
			XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
			Row newRow = sheet.getRow(selectedIdx + 3);
			Cell cell = newRow.getCell(10);
			if (cell == null) {
				cell = newRow.createCell(10);
			}
			cell.setCellValue(inputNum);
			cell = newRow.getCell(6);
			if (cell == null) {
				cell = newRow.createCell(6);
			}
			int prevValue = (int) cell.getNumericCellValue();
			cell.setCellValue(prevValue + inputNum);
			workbook.setForceFormulaRecalculation(true);
			// Save the workbook
			FileOutputStream out = new FileOutputStream(EXEL_FILE_PATH);
			workbook.write(out);
			out.close();
			workbook.close();
		} else {
			NumberField.setText(""); // Очищаем поле ввода числа
		}
	}

	/**
	 * Check date interval boolean.
	 *
	 * @param config       the config
	 * @param selectedDate the selected date
	 * @return the boolean
	 */
	private boolean checkDateInterval(Config config, Date selectedDate) {
		List<Integer> intervalList = config.getIntervalList();
		int selectedIdx = ChooseComboBox.getSelectedIndex();
		int interval = intervalList.get(selectedIdx);
		Date currentDate = new Date();
		long diff = selectedDate.getTime() - currentDate.getTime();
		long diffDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		if (diffDays < interval) {
			JOptionPane.showMessageDialog(this, "Не можна додати товар раніше, ніж через " + interval + " днів", "Помилка", JOptionPane.ERROR_MESSAGE);
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Save delivery in.
	 *
	 * @param name     the name
	 * @param size     the size
	 * @param date     the date
	 * @param group    the group
	 * @param supplier the supplier
	 * @throws IOException the io exception
	 */
	private void saveDeliveryIn(String name, int size, Date date, int group, int supplier) throws IOException {
		DeliveryConfig config = ConfigManager.readInOut();
		List<Delivery> deliveries = new ArrayList<>();
		if (config != null && config.getDeliveries() != null) {
			deliveries = config.getDeliveries();
		}
		Delivery delivery = new Delivery(name, size, date, group, supplier);
		deliveries.add(delivery);
		assert config != null;
		config.setDeliveries(deliveries);
		ConfigManager.writeInOut(config);
	}

}
