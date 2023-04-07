package com.automatedworkspace.inventorymanagement.search;

import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.automatedworkspace.inventorymanagement.ui.AddItem.AddItemForm.EXEL_FILE_PATH;

public class ItemSearcher {

	public void search() throws IOException {
		// Open the Excel workbook
		FileInputStream filePath = new FileInputStream(EXEL_FILE_PATH);
		Workbook workbook = WorkbookFactory.create(filePath);
		Sheet sheet = workbook.getSheetAt(0);

		String searchQuery = "b2";
		for (int i = 3; i < 12; i++) {
			Row row = sheet.getRow(i);
			Cell cell = row.getCell(3); // клітина з назвою товару
			if(cell.getStringCellValue().equals(searchQuery)) {
				// товар знайдено
				// додаткові дії з товаром, наприклад, отримати інформацію про ціну
				Cell priceCell = row.getCell(5); // клітина з ціною товару
				double price = priceCell.getNumericCellValue();
				System.out.println(price);
				break;
			}
		}

		workbook.close();
		filePath.close();
	}
}
