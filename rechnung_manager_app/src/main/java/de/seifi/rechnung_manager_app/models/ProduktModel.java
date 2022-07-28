package de.seifi.rechnung_manager_app.models;

import java.time.LocalDateTime;
import java.util.UUID;


public class ProduktModel {
	

	private UUID id;
	
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
	

	public ProduktModel(UUID id,
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

	
	public UUID getId() {
		return id;
	}


	public void setId(UUID id) {
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

	public boolean isNew() {
		return id == null;
	}


}
