package com.automatedworkspace.inventorymanagement.statistics;

import com.automatedworkspace.inventorymanagement.ui.Nomenclature.Delivery;

import java.util.ArrayList;
import java.util.List;

public class DeliveryConfig {
	private List<Delivery> deliveriesIN = new ArrayList<>();

	public List<Delivery> getDeliveries() {
		return deliveriesIN;
	}

	public void setDeliveries(List<Delivery> deliveries) {
		this.deliveriesIN = deliveries;
	}
}
