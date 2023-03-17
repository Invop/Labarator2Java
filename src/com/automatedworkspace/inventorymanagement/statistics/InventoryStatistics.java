package com.automatedworkspace.inventorymanagement.statistics;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


import java.io.*;


public class InventoryStatistics {
    private static final String filePath = "src/com/automatedworkspace/files/Inventory.xlsx";


    public static void main(String[] args) throws Exception {


        Workbook workbook = WorkbookFactory.create(new File(filePath));

        Sheet sheet = workbook.getSheetAt(0);


        for (int row = 1; row <= 5; row++) {
            for (int col = 2; col <= 4; col++) {
                Cell cell = sheet.getRow(row).getCell(col);
                if (cell != null) {
                    System.out.print(cell.getStringCellValue() + "\t");
                } else {
                    System.out.print("\t");
                }
            }
            System.out.println();
        }



        workbook.close();
    }

}
