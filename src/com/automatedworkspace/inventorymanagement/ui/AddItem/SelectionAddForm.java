package com.automatedworkspace.inventorymanagement.ui.AddItem;

import com.automatedworkspace.inventorymanagement.ui.InventoryManagementUI;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SelectionAddForm extends JDialog{
	private JButton homeBtn;
	private JPanel SelectionAddPanel;
	private JButton addGroupBtn;
	private JButton addSupplierBtn;
	private JButton addItemBtn;

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
	private void IfAddItemBtnPressed(){
		addItemBtn.addActionListener(e -> {
			dispose();
			new AddItemForm(null);
		});
	}
	private void IfAddSupplierBtnPressed() {
		addSupplierBtn.addActionListener(e -> {
			dispose();
			new AddSupplierForm(null);
		});
	}
	private void IfAddGroupBtnPressed() {
		addGroupBtn.addActionListener(e -> {
			dispose();
			new AddGroupForm(null);
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
		homeBtn.addActionListener(e -> {
			dispose();
			new InventoryManagementUI(null);
		});
	}


}
