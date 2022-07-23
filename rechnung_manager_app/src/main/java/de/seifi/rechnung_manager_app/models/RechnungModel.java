package de.seifi.rechnung_manager_app.models;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import de.seifi.rechnung_manager_app.entities.EntityBase;
import de.seifi.rechnung_manager_app.entities.RechnungEntity;
import de.seifi.rechnung_manager_app.enums.RechnungStatus;
import de.seifi.rechnung_manager_app.enums.RechnungType;

public class RechnungModel {
	
    private Integer id;

	private Integer customerId;

	private Integer nummer;
	   
	private String rechnungCreate;
	   
	private String liferDate;

	private RechnungType rechnungType;

	private RechnungStatus status;

	private LocalDateTime created;
	
	private LocalDateTime updated;

	private List<RechnungItemModel> items;
    
    public RechnungModel() {
    	super();
    	id = null;
		customerId = -1;
    	items = new ArrayList<>();

    }

	public RechnungModel(Integer nummer,
						 String rechnungCreate,
						 String liferDate,
						 RechnungType rechnungType,
						 RechnungStatus status) {
		this();
		this.nummer = nummer;
		this.rechnungCreate = rechnungCreate;
		this.liferDate = liferDate;
		this.rechnungType = rechnungType;
		this.status = status;
	}

	public RechnungModel(Integer customerId,
						 Integer nummer,
						 String rechnungCreate,
						 String liferDate,
						 int rechnungType,
						 int status) {
		this();
		this.customerId = customerId;
		this.nummer = nummer;
		this.rechnungCreate = rechnungCreate;
		this.liferDate = liferDate;
		this.rechnungType = RechnungType.ofValue(rechnungType);
		this.status = RechnungStatus.ofValue(status);
	}

	public RechnungModel(Integer customerId,
						 Integer nummer,
						 String rechnungCreate,
						 String liferDate,
						 RechnungType rechnungType,
						 RechnungStatus status) {
		this();
		this.customerId = customerId;
		this.nummer = nummer;
		this.rechnungCreate = rechnungCreate;
		this.liferDate = liferDate;
		this.rechnungType = rechnungType;
		this.status = status;
	}


	public RechnungModel(Integer id,
						 Integer customerId,
						 Integer nummer,
						 String rechnungCreate,
						 String liferDate,
						 int rechnungType,
						 int status,
						 LocalDateTime created,
						 LocalDateTime updated) {
		this();
		this.id = id;
		this.customerId = customerId;
		this.nummer = nummer;
		this.rechnungCreate = rechnungCreate;
		this.liferDate = liferDate;
		this.rechnungType = RechnungType.ofValue(rechnungType);
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

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
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

	public RechnungType getRechnungType() {
		return rechnungType;
	}

	public void setRechnungType(RechnungType rechnungType) {
		this.rechnungType = rechnungType;
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
			entity = new RechnungEntity(id, customerId, nummer, rechnungCreate, liferDate,
										rechnungType.getValue(), status.getValue(), this.updated);
		} 
		else {
			entity = new RechnungEntity(customerId, nummer, rechnungCreate, liferDate,
										rechnungType.getValue(), status.getValue());
		}
		
		for(RechnungItemModel item: items) {
			entity.addItem(item.toEntity());
		}
		return entity;
	}

    

}
