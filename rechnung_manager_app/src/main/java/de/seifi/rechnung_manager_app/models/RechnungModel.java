package de.seifi.rechnung_manager_app.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.seifi.rechnung_manager_app.enums.RechnungStatus;
import de.seifi.rechnung_manager_app.enums.RechnungType;

public class RechnungModel {

	public static final UUID QUITTUNG_CUSTOMER_ID = null;

    private UUID id;

	private UUID customerId;

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
		customerId = null;
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

	public RechnungModel(UUID customerId,
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

	public RechnungModel(UUID customerId,
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


	public RechnungModel(UUID id,
						 UUID customerId,
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

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getCustomerId() {
		return customerId;
	}

	public void setCustomerId(UUID customerId) {
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
    

}
