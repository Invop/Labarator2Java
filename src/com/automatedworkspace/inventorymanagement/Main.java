package com.automatedworkspace.inventorymanagement;

import com.automatedworkspace.inventorymanagement.search.ItemSearcher;
import com.automatedworkspace.inventorymanagement.ui.AddItem.AddGroupForm;
import com.automatedworkspace.inventorymanagement.ui.AddItem.AddItemForm;
import com.automatedworkspace.inventorymanagement.ui.AddItem.AddSupplierForm;
import com.automatedworkspace.inventorymanagement.ui.AddItem.SelectionAddForm;
import com.automatedworkspace.inventorymanagement.ui.DeleteItem.DeleteGroupForm;
import com.automatedworkspace.inventorymanagement.ui.DeleteItem.DeleteItemForm;
import com.automatedworkspace.inventorymanagement.ui.DeleteItem.DeleteSupplierForm;
import com.automatedworkspace.inventorymanagement.ui.DeleteItem.SelectionDeleteForm;
import com.automatedworkspace.inventorymanagement.ui.Nomenclature.AddToExistingForm;
import com.automatedworkspace.inventorymanagement.ui.Nomenclature.WriteOffFromExistingForm;

import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		ItemSearcher searcher = new ItemSearcher();
		try {
			searcher.search();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
