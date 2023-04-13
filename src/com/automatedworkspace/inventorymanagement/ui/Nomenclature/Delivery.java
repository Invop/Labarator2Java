package com.automatedworkspace.inventorymanagement.ui.Nomenclature;

import java.util.Date;

/**
 * The type Delivery.
 */
public class Delivery {
	/**
	 * The Name.
	 */
	private String name;
	/**
	 * The Size.
	 */
	private int size;
	/**
	 * The Date.
	 */
	private Date date;

	/**
	 * The Group index.
	 */
	private int groupIndex;
	/**
	 * The Supplier index.
	 */
	private int supplierIndex;

	/**
	 * Instantiates a new Delivery.
	 *
	 * @param name          the name
	 * @param size          the size
	 * @param date          the date
	 * @param groupIndex    the group index
	 * @param supplierIndex the supplier index
	 */
	public Delivery(String name, int size, Date date, int groupIndex, int supplierIndex) {
		this.name = name;
		this.size = size;
		this.date = date;
		this.groupIndex = groupIndex;
		this.supplierIndex = supplierIndex;
	}

	/**
	 * Gets name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets size.
	 *
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Gets date.
	 *
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Sets name.
	 *
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets size.
	 *
	 * @param size the size
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * Sets date.
	 *
	 * @param date the date
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Name: " + name + "\nSize: " + size + "\nDate: " + date.toString();
	}

	/**
	 * Gets group index.
	 *
	 * @return the group index
	 */
	public int getGroupIndex() {
		return groupIndex;
	}

	/**
	 * Sets group index.
	 *
	 * @param groupIndex the group index
	 */
	public void setGroupIndex(int groupIndex) {
		this.groupIndex = groupIndex;
	}

	/**
	 * Gets supplier index.
	 *
	 * @return the supplier index
	 */
	public int getSupplierIndex() {
		return supplierIndex;
	}

	/**
	 * Sets supplier index.
	 *
	 * @param supplierIndex the supplier index
	 */
	public void setSupplierIndex(int supplierIndex) {
		this.supplierIndex = supplierIndex;
	}
}
