package de.seifi.rechnung_manager.models;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import de.seifi.rechnung_manager.entities.ProduktEntity;

public class ProduktModel {
	

	private Integer id;
	
	private String produktName;
	
	private float lastPreis;
    
	private LocalDateTime created;
	
	private LocalDateTime updated;

	public ProduktModel() {
		super();
    	id = null;
	}

	public ProduktModel(String produktName, float lastPreis) {
		this();
		this.produktName = produktName;
		this.lastPreis = lastPreis;
	}
	

	public ProduktModel(Integer id, 
			String produktName, 
			float lastPreis,
            LocalDateTime created,
            LocalDateTime updated) {
		this();
		this.id = id;
		this.produktName = produktName;
		this.lastPreis = lastPreis;
        this.created = created;
        this.updated = updated;
	}

	
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}

	
	public String getProduktName() {
		return produktName;
	}

	public void setProduktName(String produktName) {
		this.produktName = produktName;
	}

	public float getLastPreis() {
		return lastPreis;
	}

	public void setLastPreis(float lastPreis) {
		this.lastPreis = lastPreis;
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
	

	public ProduktEntity toEntity() {
		ProduktEntity entity = null;
		if(id != null) {
			entity = new ProduktEntity(id, produktName, lastPreis, Timestamp.valueOf(this.created), Timestamp.valueOf(this.updated));
		} 
		else {
			entity = new ProduktEntity(produktName, lastPreis);
		}
		
		
		return entity;
	}


}
