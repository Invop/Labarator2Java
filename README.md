# Labarator2Java

Основний пакет:
com.automatedworkspace.inventorymanagement
Пакети для різних модулів програми:
com.automatedworkspace.files
com.automatedworkspace.inventorymanagement
com.automatedworkspace.inventorymanagement.ui
com.automatedworkspace.inventorymanagement.statistics
Кожен пакет містить ряд класів, призначених для виконання конкретних завдань. Наприклад:

У пакеті "ui":

InventoryManagementUI - основний клас для реалізації графічного інтерфейсу користувача , пошуку товару та взаємодії з рештою програми

У пакеті "ui.AddItem":
AddItemForm - клас для створення нового товару 
AddSupplierForm - класс для створення нового постачальника
AddGroupForm - класс для створення нової групи товарів 
SelectionAddForm - класс для вибору між варіантами створення/додавання

У пакеті "ui.EditItem":
EditItemForm - клас для редагування існуючого товару 
EditGroupForm - клас для редагування існуючої групи товарів
EditSupplierForm - клас для редагування існуючих постачальників
SelectionEditForm - класс для вибору між 4 варіантами  

у пакеті "ui.DeleteItem":
DeleteItemForm - клас для видалення товару 
DeleteGroupForm - клас для видалення групи товарів 
DeleteSupplierForm - клас для видалення постачальника 
SelectionDeleteForm - класс для вибору між 4 варіантами  

У пакеті "statistics":
InventoryStatistics - клас для підрахунку різних статистичних даних, таких як загальна вартість товару на складі, загальна вартість товарів в групі товарів
Config - робота з json 
ConfigManager - робота з json
DeliveryConfig - робота з json

у пакеті "Nomenclature":
AddToExistingForm - клас Постачання товару на склад 
WriteOffFromExistingForm - клас Відвантаження зі складу

у пакеті "files":
Exel Таблиця з якою працює наша програма 
config.json -файл який містить параметри та налаштування . Використовується для зберігання налаштувань, які можуть впливати на поведінку програми під час виконання.
InAndOut.json - файл який містить інформацію про імпорт та експорт товару
stiker1-5.png + hamster.jpg - прикол

у пакеті "inventorymanagement":
Main - клас запуску програми
