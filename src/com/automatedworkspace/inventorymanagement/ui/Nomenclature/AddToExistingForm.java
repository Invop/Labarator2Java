package com.automatedworkspace.inventorymanagement.ui.Nomenclature;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AddToExistingForm extends JDialog{
    private JPanel PanelAddForm;
    private JLabel LabelAddForm;
    private JLabel LabelChooseForm;
    private JComboBox ChooseComboBox;
    private JLabel LabelNumber;
    private JTextField NumberField;
    private JLabel IntervalLabel;
    private JTextField IntervalField;
    private JButton OKButton;
    private JButton CancelButton;

    public AddToExistingForm(JFrame parent) {
        super(parent);

        setVisible(true);
        setSize(900, 720);
        setContentPane(PanelAddForm);
        setLocationRelativeTo(parent);
        FieldsThatOnlyHandleNumbers();
        checkFields();

        Cancel();
        CloseApp();
    }

    private void FieldsThatOnlyHandleNumbers() {
        NumberField.setDocument(new NumericFilter());
        IntervalField.setDocument(new NumericFilter());
    }
    /**
     * System.exit(0)
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

    private void Cancel() {
        CancelButton.addActionListener(e -> {
            dispose();
            //new SelectionAddForm(null);
        });

    }

    private void DocumentListener() {
        NumberField.getDocument().addDocumentListener(new MyDocumentListener());
        IntervalField.getDocument().addDocumentListener(new MyDocumentListener());
        ChooseComboBox.addItemListener(new MyItemListener());
    }
    private void checkFields() {
        DocumentListener();
        boolean number = !NumberField.getText().isEmpty();
        boolean interval = !IntervalField.getText().isEmpty();
        boolean comboBox = ChooseComboBox.getSelectedIndex() != -1;

        OKButton.setEnabled(number && interval && comboBox);
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
