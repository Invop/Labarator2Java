package com.automatedworkspace.inventorymanagement.ui.EditItem;

import com.automatedworkspace.inventorymanagement.ui.InventoryManagementUI;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The type Selection edit form.
 */
public class SelectionEditForm extends JDialog {
	/**
	 * The Edit selection panel.
	 */
	private JPanel EditSelectionPanel;
	/**
	 * The Edit item button.
	 */
	private JButton EditItemButton;
	/**
	 * The Edit group button.
	 */
	private JButton EditGroupButton;
	/**
	 * The Edit supplier button.
	 */
	private JButton EditSupplierButton;
	/**
	 * The Home button.
	 */
	private JButton HomeButton;

	/**
	 * Instantiates a new Selection edit form.
	 *
	 * @param parent the parent
	 */
	public SelectionEditForm(JFrame parent) {
		super(parent);
		setVisible(true);
		setSize(400, 300);
		setContentPane(EditSelectionPanel);
		setLocationRelativeTo(parent);
		IfEditItemBtnPressed();
		IfEditSupplierBtnPressed();
		IfEditGroupBtnPressed();
		IfhomeBtnPressed();
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
	 * Ifhome btn pressed.
	 */
	private void IfhomeBtnPressed() {
		HomeButton.addActionListener(e -> {
			dispose();
			new InventoryManagementUI(null);
		});
	}

	/**
	 * If edit group btn pressed.
	 */
	private void IfEditGroupBtnPressed() {
		EditGroupButton.addActionListener(e -> {
			dispose();
			new EditGroupForm(null);
		});
	}

	/**
	 * If edit supplier btn pressed.
	 */
	private void IfEditSupplierBtnPressed() {
		EditSupplierButton.addActionListener(e -> {
			dispose();
			new EditSupplierForm(null);
		});
	}

	/**
	 * If edit item btn pressed.
	 */
	private void IfEditItemBtnPressed() {
		EditItemButton.addActionListener(e -> {
			dispose();
			new EditItemForm(null);
		});
	}
}
