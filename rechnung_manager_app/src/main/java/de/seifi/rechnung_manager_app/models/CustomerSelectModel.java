package de.seifi.rechnung_manager_app.models;

public class CustomerSelectModel {
    private Integer id;

	private String customerName;

	public CustomerSelectModel() {
		
	}

	public CustomerSelectModel(Integer id, String customerName) {
		super();
		this.id = id;
		this.customerName = customerName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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
