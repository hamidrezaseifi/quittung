package de.seifi.rechnung_manager_app.models;

import de.seifi.rechnung_manager_app.entities.QuittungEntity;
import de.seifi.rechnung_manager_app.entities.RechnungEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class QuittungModel {

    private Integer id;

	private Integer nummer;

	private String rechnungCreate;

	private String liferDate;

	private LocalDateTime created;

	private LocalDateTime updated;

	private List<QuittungItemModel> items;

    public QuittungModel() {
    	super();
    	id = null;
    	items = new ArrayList<>();
    }


	public QuittungModel(Integer nummer, String rechnungCreate, String liferDate) {
		this();
		this.nummer = nummer;
		this.rechnungCreate = rechnungCreate;
		this.liferDate = liferDate;
	}


	public QuittungModel(Integer id,
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


	public List<QuittungItemModel> getItems() {
		return items;
	}

	public void setItems(List<QuittungItemModel> items) {
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

	public float getGesamt(){
		float gesamt = 0.0f;

		for(QuittungItemModel item: items) {
			gesamt += item.getGesmt();
		}

		return gesamt;
	}

	public QuittungEntity toEntity() {
		QuittungEntity entity = null;
		if(id != null) {
			entity = new QuittungEntity(id, nummer, rechnungCreate, liferDate, Timestamp.valueOf(this.created), Timestamp.valueOf(this.updated));
		} 
		else {
			entity = new QuittungEntity(nummer, rechnungCreate, liferDate);
		}
		
		for(QuittungItemModel item: items) {
			entity.addItem(item.toEntity());
		}
		return entity;
	}

    

}
