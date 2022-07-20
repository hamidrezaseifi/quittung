package de.seifi.rechnung_manager_app.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import de.seifi.rechnung_manager_app.models.RechnungModel;

@Entity
@Table(name = "rechnung")
public class RechnungEntity extends EntityBase {
	
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

	private Integer nummer;
	   
	@Column(name="rechnung_create")
	private String rechnungCreate;
	   
	@Column(name="lifer_date")
	private String liferDate;

	private Integer status;
    
	private Timestamp created;
	
	private Timestamp updated;

	@OneToMany(mappedBy = "rechnung", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<RechnungItemEntity> items;
    
    public RechnungEntity() {
    	super();
    	items = new HashSet<>();
    }

	public RechnungEntity(String customerName,
						  String street,
						  String houseNumber,
						  String address2,
						  String plz,
						  String city,
						  Integer nummer,
						  String rechnungCreate,
						  String liferDate,
						  int status) {
		this();
		this.customerName = customerName;
		this.street = street;
		this.houseNumber = houseNumber;
		this.address2 = address2;
		this.plz = plz;
		this.city = city;
		this.nummer = nummer;
		this.rechnungCreate = rechnungCreate;
		this.liferDate = liferDate;
		this.status = status;
	}

	public RechnungEntity(Integer id,
						  String customerName,
						  String street,
						  String houseNumber,
						  String address2,
						  String plz,
						  String city,
						  Integer nummer,
						  String rechnungCreate,
						  String liferDate,
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
		this.nummer = nummer;
		this.rechnungCreate = rechnungCreate;
		this.liferDate = liferDate;
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

	public Integer getNummer() {
		return nummer;
	}

	public void setNummer(Integer nummer) {
		this.nummer = nummer;
	}


	public String getRechnungCreate() {
		return rechnungCreate;
	}


	public void setRechnungCreate(String rechnungCreate) {
		this.rechnungCreate = rechnungCreate;
	}


	public String getLiferDate() {
		return liferDate;
	}


	public void setLiferDate(String liferDate) {
		this.liferDate = liferDate;
	}


	public Set<RechnungItemEntity> getItems() {
		return items;
	}

	public void setItems(List<RechnungItemEntity> items) {
		this.items.clear();
		if(items != null) {
			items.forEach(i -> i.setRechnung(this));
			this.items.addAll(items);
		}
		
	}

	public void addItem(RechnungItemEntity item) {
		item.setRechnung(this);
		this.items.add(item);
		
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


	public RechnungModel toModel() {
		RechnungModel model = new RechnungModel(id, customerName, street, houseNumber, address2, plz, city, nummer,
												rechnungCreate, liferDate, status, created.toLocalDateTime(),
												updated.toLocalDateTime());
		for(RechnungItemEntity item: items) {
			model.getItems().add(item.toModel());
		}
		
		return model;
	}

    

}
