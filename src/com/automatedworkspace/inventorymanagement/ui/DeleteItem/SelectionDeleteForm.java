package com.automatedworkspace.inventorymanagement.ui.DeleteItem;

import com.automatedworkspace.inventorymanagement.ui.InventoryManagementUI;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The type Selection delete form.
 */
public class SelectionDeleteForm extends JDialog {
	/**
	 * The Selection delete panel.
	 */
	private JPanel SelectionDeletePanel;
	/**
	 * The Delete item button.
	 */
	private JButton DeleteItemButton;
	/**
	 * The Delete group button.
	 */
	private JButton DeleteGroupButton;
	/**
	 * The Delete supplier button.
	 */
	private JButton DeleteSupplierButton;
	/**
	 * The Home button.
	 */
	private JButton HomeButton;


	/**
	 * Instantiates a new Selection delete form.
	 *
	 * @param parent the parent
	 */
	public SelectionDeleteForm(JFrame parent) {
		super(parent);
		setVisible(true);
		setSize(400, 300);
		setContentPane(SelectionDeletePanel);
		setLocationRelativeTo(parent);
		IfDeleteItemButton();
		IfDeleteGroupButton();
		IfDeleteSupplierButton();
		IfhomeBtnPressed();
		CloseApp();
	}

	/**
	 * If delete item button.
	 */
	private void IfDeleteItemButton() {
		DeleteItemButton.addActionListener(e -> {
			dispose();
			new DeleteItemForm(null);
		});
	}

	/**
	 * If delete group button.
	 */
	private void IfDeleteGroupButton() {
		DeleteGroupButton.addActionListener(e -> {
			dispose();
			new DeleteGroupForm(null);
		});
	}

	/**
	 * If delete supplier button.
	 */
	private void IfDeleteSupplierButton() {
		DeleteSupplierButton.addActionListener(e -> {
			dispose();
			new DeleteSupplierForm(null);
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
		HomeButton.addActionListener(e -> {
			dispose();
			new InventoryManagementUI(null);
		});
	}

}
