package com.automatedworkspace.inventorymanagement.ui;

import javax.swing.*;

public class InventoryManagementUI extends JDialog{
    private JPanel InventoryManagementPanel;

    public InventoryManagementUI(JFrame parent){
        super(parent);
	    setVisible(true);
	    setSize(400, 300);
	    setContentPane(InventoryManagementPanel);
    }

}
