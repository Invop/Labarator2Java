package com.automatedworkspace.inventorymanagement.ui.DeleteItem;

import com.automatedworkspace.inventorymanagement.ui.InventoryManagementUI;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SelectionDeleteForm extends JDialog {
	private JPanel SelectionDeletePanel;
	private JButton DeleteItemButton;
	private JButton DeleteGroupButton;
	private JButton DeleteSupplierButton;
	private JButton HomeButton;


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

	private void IfDeleteItemButton() {
		DeleteItemButton.addActionListener(e -> {
			dispose();
			new DeleteItemForm(null);
		});
	}

	private void IfDeleteGroupButton() {
		DeleteGroupButton.addActionListener(e -> {
			dispose();
			new DeleteGroupForm(null);
		});
	}

	private void IfDeleteSupplierButton() {
		DeleteSupplierButton.addActionListener(e -> {
			dispose();
			new DeleteSupplierForm(null);
		});
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

	private void IfhomeBtnPressed() {
		HomeButton.addActionListener(e -> {
			dispose();
			new InventoryManagementUI(null);
		});
	}

}
