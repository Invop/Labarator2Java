package com.automatedworkspace.inventorymanagement.statistics;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Config.
 */
public class Config {

	/**
	 * The Not null rows.
	 */
	private int notNullRows;
	/**
	 * The Names list.
	 */
	private List<String> namesList = new ArrayList<>();
	/**
	 * The Ds list.
	 */
	private List<String> IDsList = new ArrayList<>();
	/**
	 * The Group list.
	 */
	private List<String> GroupList = new ArrayList<>();
	/**
	 * The Supplier list.
	 */
	private List<String> SupplierList = new ArrayList<>();
	/**
	 * The Limit list.
	 */
	private List<Integer> LimitList = new ArrayList<>();
	/**
	 * The Interval list.
	 */
	private List<Integer> IntervalList = new ArrayList<>();
	/**
	 * The Item group list.
	 */
	private List<Integer> itemGroupList = new ArrayList<>();
	/**
	 * The Item supplier list.
	 */
	private List<Integer> ItemSupplierList = new ArrayList<>();

	/**
	 * Gets not null rows.
	 *
	 * @return the not null rows
	 */
	public int getNotNullRows() {
		return notNullRows;
	}

	/**
	 * Sets not null rows.
	 *
	 * @param notNullRows the not null rows
	 */
	public void setNotNullRows(int notNullRows) {
		this.notNullRows = notNullRows;
	}

	/**
	 * Gets names list.
	 *
	 * @return the names list
	 */
	public List<String> getNamesList() {
		return namesList;
	}

	/**
	 * Sets names list.
	 *
	 * @param namesList the names list
	 */
	public void setNamesList(List<String> namesList) {
		this.namesList = namesList;
	}

	/**
	 * Gets id list.
	 *
	 * @return the id list
	 */
	public List<String> getIDList() {
		return IDsList;
	}

	/**
	 * Sets id list.
	 *
	 * @param idList the id list
	 */
	public void setIDList(List<String> idList) {
		this.IDsList = idList;
	}

	/**
	 * Gets group list.
	 *
	 * @return the group list
	 */
	public List<String> getGroupList() {
		return GroupList;
	}

	/**
	 * Sets group list.
	 *
	 * @param groupList the group list
	 */
	public void setGroupList(List<String> groupList) {
		GroupList = groupList;
	}

	/**
	 * Gets supplier list.
	 *
	 * @return the supplier list
	 */
	public List<String> getSupplierList() {
		return SupplierList;
	}

	/**
	 * Sets supplier list.
	 *
	 * @param supplierList the supplier list
	 */
	public void setSupplierList(List<String> supplierList) {
		SupplierList = supplierList;
	}

	/**
	 * Gets limit list.
	 *
	 * @return the limit list
	 */
	public List<Integer> getLimitList() {
		return LimitList;
	}

	/**
	 * Sets limit list.
	 *
	 * @param limitList the limit list
	 */
	public void setLimitList(List<Integer> limitList) {
		LimitList = limitList;
	}

	/**
	 * Gets interval list.
	 *
	 * @return the interval list
	 */
	public List<Integer> getIntervalList() {
		return IntervalList;
	}

	/**
	 * Sets interval list.
	 *
	 * @param intervalList the interval list
	 */
	public void setIntervalList(List<Integer> intervalList) {
		IntervalList = intervalList;
	}

	/**
	 * Gets item group list.
	 *
	 * @return the item group list
	 */
	public List<Integer> getItemGroupList() {
		return itemGroupList;
	}

	/**
	 * Sets item group list.
	 *
	 * @param itemGroupList the item group list
	 */
	public void setItemGroupList(List<Integer> itemGroupList) {
		this.itemGroupList = itemGroupList;
	}

	/**
	 * Gets item supplier list.
	 *
	 * @return the item supplier list
	 */
	public List<Integer> getItemSupplierList() {
		return ItemSupplierList;
	}

	/**
	 * Sets item supplier list.
	 *
	 * @param itemSupplierList the item supplier list
	 */
	public void setItemSupplierList(List<Integer> itemSupplierList) {
		ItemSupplierList = itemSupplierList;
	}
}
