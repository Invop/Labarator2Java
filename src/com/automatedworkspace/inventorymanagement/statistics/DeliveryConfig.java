package com.automatedworkspace.inventorymanagement.statistics;

import com.automatedworkspace.inventorymanagement.ui.Nomenclature.Delivery;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Delivery config.
 */
public class DeliveryConfig {
	/**
	 * The Deliveries in.
	 */
	private List<Delivery> deliveriesIN = new ArrayList<>();
	/**
	 * The Deliveries out.
	 */
	private List<Delivery> deliveriesOut = new ArrayList<>();

	/**
	 * Gets deliveries.
	 *
	 * @return the deliveries
	 */
	public List<Delivery> getDeliveries() {
		return deliveriesIN;
	}

	/**
	 * Sets deliveries.
	 *
	 * @param deliveries the deliveries
	 */
	public void setDeliveries(List<Delivery> deliveries) {
		this.deliveriesIN = deliveries;
	}

	/**
	 * Gets deliveries out.
	 *
	 * @return the deliveries out
	 */
	public List<Delivery> getDeliveriesOut() {
		return deliveriesOut;
	}

	/**
	 * Sets deliveries out.
	 *
	 * @param deliveriesOut the deliveries out
	 */
	public void setDeliveriesOut(List<Delivery> deliveriesOut) {
		this.deliveriesOut = deliveriesOut;
	}
}
