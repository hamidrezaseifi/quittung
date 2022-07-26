package de.seifi.rechnung_manager_app.entities;

import de.seifi.rechnung_manager_app.models.CustomerModel;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.*;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


@Entity
@Table(name = "customer")
public class CustomerEntity extends EntityBase {

	@Id
	@GeneratedValue
    private UUID id;

	@Column(name="customer_name")
	private String customerName;

	@Column(nullable = false) 
	@ColumnDefault("''")
	private String street;

	@Column(name="house_number")
	private String houseNumber;

	@Column(nullable = false) 
	@ColumnDefault("''")
	private String address2;

	@Column(nullable = false) 
	@ColumnDefault("''")
	private String plz;

	@Column(nullable = false) 
	@ColumnDefault("''")
	private String city;

	@Column(nullable = false) 
	private Integer status;

	@CreationTimestamp
	@ColumnDefault("CURRENT_TIMESTAMP")
	private LocalDateTime created;
	
	@UpdateTimestamp
	@ColumnDefault("CURRENT_TIMESTAMP")
	private LocalDateTime updated;

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

	public CustomerEntity(UUID id,
                          String customerName,
                          String street,
                          String houseNumber,
                          String address2,
                          String plz,
                          String city,
                          int status,
                          LocalDateTime updated) {
		this();
		this.id = id;
		this.customerName = customerName;
		this.street = street;
		this.houseNumber = houseNumber;
		this.address2 = address2;
		this.plz = plz;
		this.city = city;
		this.status = status;
		this.updated = updated;
	}

	public UUID getId() {
		return id;
	}

	@Override
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}


	@Override
	public LocalDateTime getCreated() {
		return created;
	}

	@Override
	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	@Override
	public LocalDateTime getUpdated() {
		return updated;
	}

	@Override
	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
	}


	public CustomerModel toModel() {
		CustomerModel model = new CustomerModel(id, customerName, street, houseNumber, address2, plz, city, status,
				created, updated);
		return model;
	}

    

}
