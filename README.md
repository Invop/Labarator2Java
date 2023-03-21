# Labarator2Java

Основний пакет:

com.automatedworkspace.inventorymanagement
Пакети для різних модулів програми:

com.automatedworkspace.inventorymanagement.ui
com.automatedworkspace.inventorymanagement.filehandling
com.automatedworkspace.inventorymanagement.search
com.automatedworkspace.inventorymanagement.statistics
Кожен пакет містить ряд класів, призначених для виконання конкретних завдань. Наприклад:

У пакеті "ui":

InventoryManagementUI - основний клас для реалізації графічного інтерфейсу користувача
AddItemForm - клас для додавання нового товару
EditItemForm - клас для редагування існуючого товару
DeleteItemForm - клас для видалення товару
У пакеті "filehandling":

ItemFileHandler - клас для збереження/завантаження файлів з товаровою інформацією
У пакеті "search":
GroupFileHandler - клас для збереження/завантаження файлів з назвами груп товарів
ItemSearcher - клас для пошуку товару за різними критеріями (назва, виробник, група товарів)

У пакеті "statistics":
InventoryStatistics - клас для підрахунку різних статистичних даних, таких як загальна вартість товару на складі, загальна вартість товарів в групі товарів
