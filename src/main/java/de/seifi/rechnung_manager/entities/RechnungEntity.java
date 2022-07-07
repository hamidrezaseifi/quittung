package de.seifi.rechnung_manager.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import de.seifi.rechnung_manager.models.RechnungModel;

@Entity
@Table(name = "rechnung")
public class RechnungEntity extends EntityBase {
	
	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

	private Integer nummer;
	   
	@Column(name="rechnung_create")
	private String rechnungCreate;
	   
	@Column(name="lifer_date")
	private String liferDate;
    
	private Timestamp created;
	
	private Timestamp updated;

	@OneToMany(mappedBy = "rechnung", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<RechnungItemEntity> items;
    
    public RechnungEntity() {
    	super();
    	items = new HashSet<>();
    }


	public RechnungEntity(Integer nummer, String rechnungCreate, String liferDate) {
		this();
		this.nummer = nummer;
		this.rechnungCreate = rechnungCreate;
		this.liferDate = liferDate;
	}


	public RechnungEntity(Integer id, 
			Integer nummer, 
			String rechnungCreate, 
			String liferDate,
			Timestamp created,
			Timestamp updated) {
		this();
		this.id = id;
		this.nummer = nummer;
		this.rechnungCreate = rechnungCreate;
		this.liferDate = liferDate;
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
		RechnungModel model = new RechnungModel(id, nummer, rechnungCreate, liferDate, created.toLocalDateTime(), updated.toLocalDateTime());
		for(RechnungItemEntity item: items) {
			model.getItems().add(item.toModel());
		}
		
		return model;
	}

    

}
