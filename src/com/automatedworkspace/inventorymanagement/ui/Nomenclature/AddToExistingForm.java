package com.automatedworkspace.inventorymanagement.ui.Nomenclature;

import com.automatedworkspace.inventorymanagement.statistics.Config;
import com.automatedworkspace.inventorymanagement.statistics.ConfigManager;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Date;
import java.util.List;


public class AddToExistingForm extends JDialog{
    private JPanel PanelAddForm;
    private JLabel LabelChooseForm;
    private JComboBox<String> ChooseComboBox;
    private JLabel LabelNumber;
    private JTextField NumberField;
    private JLabel IntervalLabel;
    private JButton OKButton;
    private JButton CancelButton;
    private JPanel DatePanel;
    private JDateChooser DateChooser = new JDateChooser();

    public AddToExistingForm(JFrame parent) {
        super(parent);
        setVisible(true);
        setSize(900, 300);
        setContentPane(PanelAddForm);
        setLocationRelativeTo(parent);
        FieldsThatOnlyHandleNumbers();
        //calendar
        DatePanel.add(DateChooser);
        try {
            GetItemNames();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        checkFields();
        Cancel();
        CloseApp();

    }

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

    private void FieldsThatOnlyHandleNumbers() {
        NumberField.setDocument(new NumericFilter());
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

    private void Cancel() {
        CancelButton.addActionListener(e -> {
            dispose();
            //new SelectionAddForm(null);
        });

    }

    private void DocumentListener() {
        NumberField.getDocument().addDocumentListener(new MyDocumentListener());
        ChooseComboBox.addItemListener(new MyItemListener());
    }
    private void checkFields() {
        DocumentListener();
        boolean number = !NumberField.getText().isEmpty();
        boolean comboBox = ChooseComboBox.getSelectedIndex() != -1;
        boolean dateSelected = DateChooser.getDate() != null; // check if a date is selected in the DateChooser

        OKButton.setEnabled(number && comboBox && dateSelected);
    }


    //sub classes

    /**
     * The type My document listener.
     */
    private class MyDocumentListener implements DocumentListener {
        @Override
        public void changedUpdate(DocumentEvent e) {
            checkFields();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            checkFields();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            checkFields();
        }
    }

    /**
     * The type My item listener.
     */
    private class MyItemListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            checkFields();
        }
    }

    private static class NumericFilter extends PlainDocument {
        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (str == null) {
                return;
            }

            char[] chars = str.toCharArray();
            StringBuilder sb = new StringBuilder();
            for (char ch : chars) {
                if (Character.isDigit(ch)) {
                    sb.append(ch);
                }
            }
            super.insertString(offs, sb.toString(), a);
        }
    }
}
