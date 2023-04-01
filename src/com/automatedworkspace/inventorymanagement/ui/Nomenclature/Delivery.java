package com.automatedworkspace.inventorymanagement.ui.Nomenclature;
import java.util.Date;

public class Delivery {
	private String name;
	private int size;
	private Date date;

	public Delivery(String name, int size, Date date) {
		this.name = name;
		this.size = size;
		this.date = date;
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
}
