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

InventoryManagementUI - основний клас для реалізації графічного інтерфейсу користувача (Soon....)


У пакеті "ui.AddItem":
AddItemForm - клас для створення нового товару (Form done (Ok/Cancel Edits?))
AddToExistingForm - клас для додавання товару до існуючого (Form done (Ok/Cancel Edits?)) 
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
GroupFileHandler - клас для збереження/завантаження файлів з назвами груп товарів/постачальників
ItemSearcher - клас для пошуку товару за різними критеріями (назва, виробник, група товарів)

У пакеті "statistics":
InventoryStatistics - клас для підрахунку різних статистичних даних, таких як загальна вартість товару на складі, загальна вартість товарів в групі товарів

у пакеті "files":
Exel Таблиця з якою працює наша програма

у пакеті "inventorymanagement":
Main - клас запуску програми
