package de.seifi.rechnung_manager_module.models;

import java.util.UUID;

public class CustomerSelectModel {
    private UUID id;

	private String customerName;

	public CustomerSelectModel() {
		
	}

	public CustomerSelectModel(UUID id, String customerName) {
		super();
		this.id = id;
		this.customerName = customerName;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@Override
	public String toString() {
		
		return customerName;
	}
	

}
