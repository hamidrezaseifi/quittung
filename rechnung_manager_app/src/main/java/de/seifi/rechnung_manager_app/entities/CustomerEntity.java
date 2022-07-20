package de.seifi.rechnung_manager_app.entities;

import de.seifi.rechnung_manager_app.models.CustomerModel;
import de.seifi.rechnung_manager_app.models.RechnungModel;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "customer")
public class CustomerEntity extends EntityBase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

	@Column(name="customer_name")
	private String customerName;

	private String street;

	@Column(name="house_number")
	private String houseNumber;

	private String address2;

	private String plz;

	private String city;

	private Integer status;

	private Timestamp created;

	private Timestamp updated;

	public CustomerEntity() {
    	super();

    }

	public CustomerEntity(String customerName,
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
		this.status = status;
	}

	public CustomerEntity(Integer id,
                          String customerName,
                          String street,
                          String houseNumber,
                          String address2,
                          String plz,
                          String city,
                          int status,
                          Timestamp created,
                          Timestamp updated) {
		this();
		this.id = id;
		this.customerName = customerName;
		this.street = street;
		this.houseNumber = houseNumber;
		this.address2 = address2;
		this.plz = plz;
		this.city = city;
		this.status = status;
		this.created = created;
		this.updated = updated;
	}

	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}


	@Override
	public Timestamp getCreated() {
		return created;
	}

	@Override
	public void setCreated(Timestamp created) {
		this.created = created;
	}

	@Override
	public Timestamp getUpdated() {
		return updated;
	}

	@Override
	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}


	public CustomerModel toModel() {
		CustomerModel model = new CustomerModel(id, customerName, street, houseNumber, address2, plz, city, status,
												created.toLocalDateTime(), updated.toLocalDateTime());
		return model;
	}

    

}
