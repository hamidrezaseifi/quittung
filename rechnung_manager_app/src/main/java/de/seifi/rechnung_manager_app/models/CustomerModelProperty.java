package de.seifi.rechnung_manager_app.models;

import de.seifi.rechnung_manager_app.entities.CustomerEntity;
import de.seifi.rechnung_manager_app.entities.RechnungEntity;
import de.seifi.rechnung_manager_app.enums.CustomerStatus;
import de.seifi.rechnung_manager_app.enums.RechnungStatus;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CustomerModelProperty {

    private StringProperty customerName;

	private StringProperty street;

	private StringProperty houseNumber;

	private StringProperty address2;

	private StringProperty plz;

	private StringProperty city;


    public CustomerModelProperty() {
       	this.customerName = new SimpleStringProperty("");
       	this.street = new SimpleStringProperty("");
       	this.houseNumber = new SimpleStringProperty("");
       	this.address2 = new SimpleStringProperty("");
       	this.plz = new SimpleStringProperty("");
       	this.city = new SimpleStringProperty("");

    }

	public CustomerModelProperty(CustomerModel model) {
		this();
		this.customerName.set(model.getCustomerName());
		this.street.set(model.getStreet());
		this.houseNumber.set(model.getHouseNumber());
		this.address2.set(model.getAddress2());
		this.plz.set(model.getPlz());
		this.city.set(model.getCity());

	}

	public CustomerModelProperty(CustomerEntity entity) {
		this();
		this.customerName.set(entity.getCustomerName());
		this.street.set(entity.getStreet());
		this.houseNumber.set(entity.getHouseNumber());
		this.address2.set(entity.getAddress2());
		this.plz.set(entity.getPlz());
		this.city.set(entity.getCity());
	}
	
	

	public StringProperty getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName.set(customerName);
	}

	public StringProperty getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street.set(street);
	}

	public StringProperty getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber.set(houseNumber);
	}

	public StringProperty getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2.set(address2);
	}

	public StringProperty getPlz() {
		return plz;
	}

	public void setPlz(String plz) {
		this.plz.set(plz);
	}

	public StringProperty getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city.set(city);
	}

	public CustomerEntity toEntity() {
		CustomerEntity entity = new CustomerEntity(customerName.get(), street.get(), houseNumber.get(), address2.get(), 
				plz.get(), city.get(), CustomerStatus.ACTIVE.getValue());
		

		return entity;
	}

	public CustomerModel toModel() {
		CustomerModel model = new CustomerModel(customerName.get(), street.get(), houseNumber.get(), address2.get(), 
				plz.get(), city.get(), CustomerStatus.ACTIVE);
		

		return model;
	}

	public void setModel(CustomerModel customerModel) {
		this.customerName.set(customerModel.getCustomerName());
		this.street.set(customerModel.getStreet());
		this.houseNumber.set(customerModel.getHouseNumber());
		this.address2.set(customerModel.getAddress2());
		this.plz.set(customerModel.getPlz());
		this.city.set(customerModel.getCity());

	}

    

}
