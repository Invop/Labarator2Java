package com.automatedworkspace.inventorymanagement.statistics;

import com.automatedworkspace.inventorymanagement.ui.Nomenclature.Delivery;

import java.util.ArrayList;
import java.util.List;

public class Config {

	private int notNullRows;
	private List<String> namesList = new ArrayList<>();
	private List<String> IDsList = new ArrayList<>();
	private List<String> GroupList = new ArrayList<>();
	private List<String> SupplierList = new ArrayList<>();
	private List<Integer> LimitList = new ArrayList<>();
	private List<Integer> IntervalList = new ArrayList<>();
	private List<Integer> itemGroupList = new ArrayList<>();

	public int getNotNullRows() {
		return notNullRows;
	}

	public void setNotNullRows(int notNullRows) {
		this.notNullRows = notNullRows;
	}

	public List<String> getNamesList() {
		return namesList;
	}

	public void setNamesList(List<String> namesList) {
		this.namesList = namesList;
	}

	public List<String> getIDList() {
		return IDsList;
	}

	public void setIDList(List<String> idList) {
		this.IDsList = idList;
	}


	public List<String> getGroupList() {
		return GroupList;
	}

	public void setGroupList(List<String> groupList) {
		GroupList = groupList;
	}


	public List<String> getSupplierList() {
		return SupplierList;
	}

	public void setSupplierList(List<String> supplierList) {
		SupplierList = supplierList;
	}

	public List<Integer> getLimitList() {
		return LimitList;
	}

	public void setLimitList(List<Integer> limitList) {
		LimitList = limitList;
	}

	public List<Integer> getIntervalList() {
		return IntervalList;
	}

	public void setIntervalList(List<Integer> intervalList) {
		IntervalList = intervalList;
	}


	public List<Integer> getItemGroupList() {
		return itemGroupList;
	}

	public void setItemGroupList(List<Integer> itemGroupList) {
		this.itemGroupList = itemGroupList;
	}
}
