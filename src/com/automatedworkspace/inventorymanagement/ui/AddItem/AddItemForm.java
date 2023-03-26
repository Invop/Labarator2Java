package com.automatedworkspace.inventorymanagement.ui.AddItem;

import javax.swing.*;

/**
 *Потрібно зробити 4 панелі, 1 Додати Постачальника, 2 Створити товар, 3 Додати Товар до існуючого, 4 вибір між ними
 *

 *Постачальник :
 * Отримати назву + лейбл
 * лейбл для виведення статусу Доданий/Існує
 * OK/Cancel buttons?????
 *
 * Створити товар:
 * Отримати ID+лейбл
 * Отримати Назву+лейбл
 * Отримати Постачальника +лейбл (JComboBox)
 * Отримати Ціну за 1 +лейбл
 * Отримати Нижню межу поповнення + лебл
 * Отримати групу? + лейбл (JComboBox)
 * OK/Cancel buttons????? ok
 *
 * Додати товар :
 * Отримати Кількість + лейбл
 * Отримати Інтервал поставок + лейб
 * OK/Cancel buttons?????
 *
 * Вибір:
 * 4 кнопки , остання вихід
 * Лейб(якщо список Постачальників пустий)
 * якщо придумаєш щось прикольне додавай
 *
 *
 * назви полів поміняй на чітко описуючі 2-3 слова, не скорочувати
 * (Перший пункт при створенні будь-якого об'єкта "Field name")
 *
 * OK/Cancel buttons?????
 * Розміри панелей 1280x720 (Основні)
 * для 4панелі хз, щоб 4 кнопки було+лейбл
 */
public class AddItemForm extends JDialog{


    private JPanel CreateFormBrand;
    private JLabel AddBrandLabel;
    private JLabel AddIDLabel;
    private JTextField AddIDField;
    private JLabel NameLabel;
    private JTextField AddNameField;
    private JLabel ManufacturerLabel;
    private JComboBox AddManufacturerBox;
    private JLabel PriceLabel;
    private JTextField AddPriceField;
    private JLabel LimitLabel;
    private JTextField AddLimitField;
    private JLabel GroupLabel;
    private JComboBox AddGroupBox;
    private JButton OkButton;
    private JButton CancelButton;

    public AddItemForm(JFrame parent) {
        super(parent);
        setVisible(true);
        setSize(900,720);
        setContentPane(CreateFormBrand);
        setLocationRelativeTo(parent);

    }

    public static void main(String[] args) {
        AddItemForm addItemForm = new AddItemForm(null);
    }
}
