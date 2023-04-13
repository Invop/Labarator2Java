package com.automatedworkspace.inventorymanagement.ui.AddItem;

import com.automatedworkspace.inventorymanagement.ui.InventoryManagementUI;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The type Selection add form.
 */
public class SelectionAddForm extends JDialog {
	/**
	 * The Home btn.
	 */
	private JButton homeBtn;
	/**
	 * The Selection add panel.
	 */
	private JPanel SelectionAddPanel;
	/**
	 * The Add group btn.
	 */
	private JButton addGroupBtn;
	/**
	 * The Add supplier btn.
	 */
	private JButton addSupplierBtn;
	/**
	 * The Add item btn.
	 */
	private JButton addItemBtn;

	/**
	 * Instantiates a new Selection add form.
	 *
	 * @param parent the parent
	 */
	public SelectionAddForm(JFrame parent) {
		super(parent);
		setVisible(true);
		setSize(400, 300);
		setContentPane(SelectionAddPanel);
		setLocationRelativeTo(parent);
		IfAddItemBtnPressed();
		IfAddSupplierBtnPressed();
		IfAddGroupBtnPressed();
		IfhomeBtnPressed();
		CloseApp();
	}

	/**
	 * If add item btn pressed.
	 */
	private void IfAddItemBtnPressed() {
		addItemBtn.addActionListener(e -> {
			dispose();
			new AddItemForm(null);
		});
	}

	/**
	 * If add supplier btn pressed.
	 */
	private void IfAddSupplierBtnPressed() {
		addSupplierBtn.addActionListener(e -> {
			dispose();
			new AddSupplierForm(null);
		});
	}

	/**
	 * If add group btn pressed.
	 */
	private void IfAddGroupBtnPressed() {
		addGroupBtn.addActionListener(e -> {
			dispose();
			new AddGroupForm(null);
		});
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
		homeBtn.addActionListener(e -> {
			dispose();
			new InventoryManagementUI(null);
		});
	}


}
