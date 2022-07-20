package de.seifi.rechnung_manager_app.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import de.seifi.rechnung_manager_app.models.RechnungModel;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "rechnung")
public class RechnungEntity extends EntityBase {
	
	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

	@Column(name="customer_id")
	private Integer customerId;

	private Integer nummer;

	@Column(name="rechnung_create")
	private String rechnungCreate;
	   
	@Column(name="lifer_date")
	private String liferDate;

	@Column(name="rechnung_type")
	private Integer rechnungType;

	private Integer status;

	private Timestamp created;
	
	private Timestamp updated;

	@OneToMany(mappedBy = "rechnung", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<RechnungItemEntity> items;
    
    public RechnungEntity() {
    	super();
    	items = new HashSet<>();
    }

	public RechnungEntity(Integer customerId,
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
		this.status = status;
		this.rechnungType = rechnungType;
	}

	public RechnungEntity(Integer id,
						  Integer customerId,
						  Integer nummer,
						  String rechnungCreate,
						  String liferDate,
						  int rechnungType,
						  int status,
						  Timestamp created,
						  Timestamp updated) {
		this();
		this.id = id;
		this.customerId = customerId;
		this.nummer = nummer;
		this.rechnungCreate = rechnungCreate;
		this.liferDate = liferDate;
		this.status = status;
		this.rechnungType = rechnungType;
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

	public Integer getRechnungType() {
		return rechnungType;
	}

	public void setRechnungType(Integer rechnungType) {
		this.rechnungType = rechnungType;
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
		RechnungModel model = new RechnungModel(id, customerId, nummer, rechnungCreate, liferDate,
												rechnungType, status, created.toLocalDateTime(),
												updated.toLocalDateTime());
		for(RechnungItemEntity item: items) {
			model.getItems().add(item.toModel());
		}
		
		return model;
	}

    

}
