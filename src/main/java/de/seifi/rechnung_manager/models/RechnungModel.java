package de.seifi.rechnung_manager.models;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import de.seifi.rechnung_manager.entities.RechnungEntity;


public class RechnungModel {
	
    private Integer id;

	private Integer nummer;
	   
	private String rechnungCreate;
	   
	private String liferDate;
    
	private LocalDateTime created;
	
	private LocalDateTime updated;

	private List<RechnungItemModel> items;
    
    public RechnungModel() {
    	super();
    	id = null;
    	items = new ArrayList<>();
    }


	public RechnungModel(Integer nummer, String rechnungCreate, String liferDate) {
		this();
		this.nummer = nummer;
		this.rechnungCreate = rechnungCreate;
		this.liferDate = liferDate;
	}


	public RechnungModel(Integer id, 
			Integer nummer, 
			String rechnungCreate, 
			String liferDate,
            LocalDateTime created,
            LocalDateTime updated) {
		this();
		this.id = id;
		this.nummer = nummer;
		this.rechnungCreate = rechnungCreate;
		this.liferDate = liferDate;
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


	public RechnungEntity toEntity() {
		RechnungEntity entity = null;
		if(id != null) {
			entity = new RechnungEntity(id, nummer, rechnungCreate, liferDate, Timestamp.valueOf(this.created), Timestamp.valueOf(this.updated));
		} 
		else {
			entity = new RechnungEntity(nummer, rechnungCreate, liferDate);
		}
		
		for(RechnungItemModel item: items) {
			entity.addItem(item.toEntity());
		}
		return entity;
	}

    

}
