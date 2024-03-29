package de.seifi.rechnung_manager_app.models;

import de.seifi.rechnung_manager_app.enums.CustomerStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class CustomerModel {

    private UUID id;

	private String customerName;

	private String street;

	private String houseNumber;

	private String address2;

	private String plz;

	private String city;

	private CustomerStatus status;

	private LocalDateTime created;

	private LocalDateTime updated;

    public CustomerModel() {
    	super();
    	id = null;
		this.customerName = "";
		this.street = "";
		this.houseNumber = "";
		this.address2 = "";
		this.plz = "";
		this.city = "";
		this.status = CustomerStatus.ACTIVE;
    }

	public CustomerModel(String customerName,
						 String street,
						 String houseNumber,
						 String address2,
						 String plz,
						 String city,
						 int status) {
		this();
		this.customerName = customerName;
		this.street = street;
		this.houseNumber = houseNumber;
		this.address2 = address2;
		this.plz = plz;
		this.city = city;
		this.status = CustomerStatus.ofValue(status);
	}

	public CustomerModel(String customerName) {
		this();
		this.customerName = customerName;
	}

	public CustomerModel(String customerName,
                         String street,
                         String houseNumber,
                         String address2,
                         String plz,
                         String city,
						 CustomerStatus status) {
		this();
		this.customerName = customerName;
		this.street = street;
		this.houseNumber = houseNumber;
		this.address2 = address2;
		this.plz = plz;
		this.city = city;
		this.status = status;
	}


	public CustomerModel(UUID id,
                         String customerName,
                         String street,
                         String houseNumber,
                         String address2,
                         String plz,
                         String city,
                         int status,
                         LocalDateTime created,
                         LocalDateTime updated) {
		this();
		this.id = id;
		this.customerName = customerName;
		this.street = street;
		this.houseNumber = houseNumber;
		this.address2 = address2;
		this.plz = plz;
		this.city = city;
		this.status = CustomerStatus.ofValue(status);
        this.created = created;
        this.updated = updated;
	}

	public boolean isNew() {
		return id == null;
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

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}


	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	
	public String getPlz() {
		return plz;
	}

	public void setPlz(String plz) {
		this.plz = plz;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public CustomerStatus getStatus() {
		return status;
	}

	public void setStatus(CustomerStatus status) {
		this.status = status;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
	}

	public String getStreetHouseNumber() {
		return street + " " + houseNumber;
	}

	public String getPlzCity() {
		return plz + " " + city;
	}

	public String getAddress() {
		String addr2 = address2 == null || address2.isBlank() ? "" : address2 + ", ";
		String address = String.format("%s %s, %s%s %s", street, houseNumber, addr2, plz, city);
		return address;
	}

	@Override
	public String toString(){
		return customerName + (status == CustomerStatus.ACTIVE ? ": aktiv" : ": inaktive");
	}

	public String getPrintLine1() {
		return this.street + " "+ this.houseNumber;
	}

	public String getPrintLine2() {
		return this.plz + " "+ this.city;
	}

}
