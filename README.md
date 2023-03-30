# Labarator2Java

Основний пакет:

com.automatedworkspace.inventorymanagement
Пакети для різних модулів програми:
com.automatedworkspace.files
com.automatedworkspace.inventorymanagement
com.automatedworkspace.inventorymanagement.ui
com.automatedworkspace.inventorymanagement.filehandling
com.automatedworkspace.inventorymanagement.search
com.automatedworkspace.inventorymanagement.statistics
Кожен пакет містить ряд класів, призначених для виконання конкретних завдань. Наприклад:

У пакеті "ui":

InventoryManagementUI - основний клас для реалізації графічного інтерфейсу користувача (Вивід на екран табличку поставок вхід/вихід, виведення самої таблиці у різних варіантах (вибір групи/постачальника)?)
Можливо додамо два листи в табличку вхід/вихід,
Додати залежності до основного листа!!

У пакеті "ui.AddItem":
AddItemForm - клас для створення нового товару (Form done (Ok/Cancel Edits?))
AddSupplierForm - класс для створення нового постачальника (ім'я + Ok/Cancel)
AddGroupForm - класс для створення нової групи товарів (ім'я + Ok/Cancel)
SelectionAddForm - класс для вибору між варіантами створення/додавання (вибір із 4 + Cancel?)

У пакеті "ui.EditItem":
EditItemForm - клас для редагування існуючого товару (копія AddItemForm? + Ok/Cancel)
EditGroupForm - клас для редагування існуючої групи товарів(ім'я + Ok/Cancel)
EditSupplierForm - клас для редагування існуючих постачальників(ім'я + Ok/Cancel)
SelectionEditForm - класс для вибору між 4 варіантами  (вибір із 3 + Cancel?)

у пакеті "ui.DeleteItem":
DeleteItemForm - клас для видалення товару (ComboBox ?  + Ok/Cancel)
DeleteGroupForm - клас для видалення групи товарів (ComboBox ?  + Ok/Cancel)
DeleteSupplierForm - клас для видалення постачальника (ComboBox ?  + Ok/Cancel)
SelectionDeleteForm - класс для вибору між 4 варіантами  (вибір із 3 + Cancel?)


У пакеті "search":
ItemSearcher - клас для пошуку товару за різними критеріями (назва, виробник, група товарів)

У пакеті "statistics":
InventoryStatistics - клас для підрахунку різних статистичних даних, таких як загальна вартість товару на складі, загальна вартість товарів в групі товарів
Config - робота з json (поля get/set)
ConfigManager - робота з json (методи)

у пакеті "Nomenclature":
AddToExistingForm - клас Постачання товару на склад (Додати календ , типу  https://www.youtube.com/watch?v=qunxo1zqZ6M&ab_channel=MengIT , щоб зафіксувати час прибуття , Інтервал - delete ??)
WriteOffFromExistingForm - клас Відвантаження зі складу (поки хз , напевно такий же )

у пакеті "files":
Exel Таблиця з якою працює наша програма (Інтервал - delete ?)
config.json -файл який містить параметри та налаштування . Використовується для зберігання налаштувань, які можуть впливати на поведінку програми під час виконання.
Додати json файл для відстеження постачання ->?
Додати json файл для відстеження постачання <-?

у пакеті "inventorymanagement":
Main - клас запуску програми
