package com.automatedworkspace.inventorymanagement.ui.EditItem;

import com.automatedworkspace.inventorymanagement.ui.DeleteItem.DeleteGroupForm;
import com.automatedworkspace.inventorymanagement.ui.DeleteItem.DeleteItemForm;
import com.automatedworkspace.inventorymanagement.ui.DeleteItem.DeleteSupplierForm;
import com.automatedworkspace.inventorymanagement.ui.InventoryManagementUI;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SelectionEditForm extends JDialog {
	private JPanel EditSelectionPanel;
	private JButton EditItemButton;
	private JButton EditGroupButton;
	private JButton EditSupplierButton;
	private JButton HomeButton;
	public SelectionEditForm(JFrame parent) {
		super(parent);
		setVisible(true);
		setSize(400, 300);
		setContentPane(EditSelectionPanel);
		setLocationRelativeTo(parent);
		IfEditItemButton();
		IfEditGroupButton();
		IfDeleteSupplierButton();
		IfhomeBtnPressed();
		CloseApp();
	}
	private void IfEditItemButton(){
		EditItemButton.addActionListener(e -> {
			dispose();
		});
	}
	private void IfEditGroupButton() {
		EditGroupButton.addActionListener(e -> {
			dispose();
			new EditGroupForm(null);
		});
	}
	private void IfDeleteSupplierButton() {
		EditSupplierButton.addActionListener(e -> {
			dispose();
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
