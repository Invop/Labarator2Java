package com.automatedworkspace.inventorymanagement.ui.DeleteItem;

import com.automatedworkspace.inventorymanagement.statistics.Config;
import com.automatedworkspace.inventorymanagement.statistics.ConfigManager;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;

public class DeleteSupplierForm extends JDialog{
    private JPanel DeleteSuplplierPanel;
    private JLabel DeleteSupplierLabel;
    private JComboBox<String> DeleteSupplierComboBox;
    private JButton OKDeleteSupplierButton;
    private JButton CancelDeleteSupplierButton;

    public DeleteSupplierForm(JFrame parent) {
        super(parent);
        setSize(500,450);
        setVisible(true);
        setContentPane(DeleteSuplplierPanel);
        setLocationRelativeTo(parent);
        try {
            addToSupplierComboBox();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        IfOkPressed();
        IfCancelPressed();
        CloseApp();
    }

    private void addToSupplierComboBox() throws IOException {
        // Load the configuration file
        Config config = ConfigManager.readConfig();
        List<String> supplierList = config.getSupplierList();
        for (String supp : supplierList) {
            DeleteSupplierComboBox.addItem(supp);
        }
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
    private void IfOkPressed(){
        OKDeleteSupplierButton.addActionListener(e -> {
            try {
                deleteSupplier();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            dispose();
            new SelectionDeleteForm(null);
        });
    }
    private void IfCancelPressed() {
        CancelDeleteSupplierButton.addActionListener(e -> {
            dispose();
            new SelectionDeleteForm(null);
        });

    }
    private void deleteSupplier() throws IOException{
        Config config = ConfigManager.readConfig();
        List<String> suppList= config.getSupplierList();
        List<Integer> itemSuppList = config.getItemSupplierList();
        int selectedIndx = DeleteSupplierComboBox.getSelectedIndex();
        for (int i = itemSuppList.size() - 1; i >= 0; i--){
            if(itemSuppList.get(i)==selectedIndx){
                itemSuppList.set(i,null);
            }
        }
        suppList.remove(selectedIndx);
        ConfigManager.writeConfig(config);
    }




}
