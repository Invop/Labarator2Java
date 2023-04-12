package com.automatedworkspace.inventorymanagement.ui.Nomenclature;

import java.util.Date;

public class Delivery {
	private String name;
	private int size;
	private Date date;

	private int groupIndex;
	private int supplierIndex;

	public Delivery(String name, int size, Date date, int groupIndex, int supplierIndex) {
		this.name = name;
		this.size = size;
		this.date = date;
		this.groupIndex = groupIndex;
		this.supplierIndex = supplierIndex;
	}

	public String getName() {
		return name;
	}

	public int getSize() {
		return size;
	}

	public Date getDate() {
		return date;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Name: " + name + "\nSize: " + size + "\nDate: " + date.toString();
	}

	public int getGroupIndex() {
		return groupIndex;
	}

	public void setGroupIndex(int groupIndex) {
		this.groupIndex = groupIndex;
	}

	public int getSupplierIndex() {
		return supplierIndex;
	}

	public void setSupplierIndex(int supplierIndex) {
		this.supplierIndex = supplierIndex;
	}
}
