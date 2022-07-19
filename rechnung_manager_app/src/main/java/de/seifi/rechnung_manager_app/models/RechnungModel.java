package de.seifi.rechnung_manager_app.models;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import de.seifi.rechnung_manager_app.entities.RechnungEntity;
import de.seifi.rechnung_manager_app.enums.RechnungStatus;

public class RechnungModel {
	
    private Integer id;

	private String customerName;

	private String streetHouseNumber;

	private String plz;

	private String city;

	private Integer nummer;
	   
	private String rechnungCreate;
	   
	private String liferDate;

	private RechnungStatus status;

	private LocalDateTime created;
	
	private LocalDateTime updated;

	private List<RechnungItemModel> items;
    
    public RechnungModel() {
    	super();
    	id = null;
    	items = new ArrayList<>();

    }

	public RechnungModel(String customerName,
						 String streetHouseNumber,
						 String plz,
						 String city,
						 Integer nummer,
						 String rechnungCreate,
						 String liferDate,
						 int status) {
		this();
		this.customerName = customerName;
		this.streetHouseNumber = streetHouseNumber;
		this.plz = plz;
		this.city = city;
		this.nummer = nummer;
		this.rechnungCreate = rechnungCreate;
		this.liferDate = liferDate;
		this.status = RechnungStatus.ofValue(status);
	}

	public RechnungModel(String customerName,
						 String streetHouseNumber,
						 String plz,
						 String city,
						 Integer nummer,
						 String rechnungCreate,
						 String liferDate,
						 RechnungStatus status) {
		this();
		this.customerName = customerName;
		this.streetHouseNumber = streetHouseNumber;
		this.plz = plz;
		this.city = city;
		this.nummer = nummer;
		this.rechnungCreate = rechnungCreate;
		this.liferDate = liferDate;
		this.status = status;
	}


	public RechnungModel(Integer id,
						 String customerName,
						 String streetHouseNumber,
						 String plz,
						 String city,
						 Integer nummer,
						 String rechnungCreate,
						 String liferDate,
						 int status,
						 LocalDateTime created,
						 LocalDateTime updated) {
		this();
		this.id = id;
		this.customerName = customerName;
		this.streetHouseNumber = streetHouseNumber;
		this.plz = plz;
		this.city = city;
		this.nummer = nummer;
		this.rechnungCreate = rechnungCreate;
		this.liferDate = liferDate;
		this.status = RechnungStatus.ofValue(status);
        this.created = created;
        this.updated = updated;
	}

	
	public boolean isNew() {
		return id == null;
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

	public String getStreetHouseNumber() {
		return streetHouseNumber;
	}

	public void setStreetHouseNumber(String streetHouseNumber) {
		this.streetHouseNumber = streetHouseNumber;
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


	public List<RechnungItemModel> getItems() {
		return items;
	}

	public void setItems(List<RechnungItemModel> items) {
		this.items = items;
	}

	public RechnungStatus getStatus() {
		return status;
	}

	public void setStatus(RechnungStatus status) {
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

	public float getGesamt(){
		float gesamt = 0.0f;

		for(RechnungItemModel item: items) {
			gesamt += item.getGesmt();
		}

		return gesamt;
	}

	public RechnungEntity toEntity() {
		RechnungEntity entity = null;
		if(id != null) {
			entity = new RechnungEntity(id, customerName, streetHouseNumber, plz, city, nummer, rechnungCreate,
										liferDate, status.getValue(), Timestamp.valueOf(this.created), Timestamp.valueOf(this.updated));
		} 
		else {
			entity = new RechnungEntity(customerName, streetHouseNumber, plz, city, nummer, rechnungCreate, liferDate, status.getValue());
		}
		
		for(RechnungItemModel item: items) {
			entity.addItem(item.toEntity());
		}
		return entity;
	}

    

}
