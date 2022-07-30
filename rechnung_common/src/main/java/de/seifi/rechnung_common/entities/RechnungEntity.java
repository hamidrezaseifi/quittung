package de.seifi.rechnung_common.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "rechnung")
public class RechnungEntity extends EntityBase {
	
	@Id	
	@GeneratedValue
    private UUID id;

	@Column(name="customer_id")
	private UUID customerId;

	@Column(nullable = false) 
	private Integer nummer;

	@Column(name="rechnung_create", nullable = false)
	private String rechnungCreate;
	   
	@Column(name="lifer_date", nullable = false)
	private String liferDate;

	@Column(name="rechnung_type", nullable = false)
	private Integer rechnungType;

	@Column(nullable = false) 
	private Integer status;

	@Column(name="user_id")
	private UUID userId;

	@CreationTimestamp
	@ColumnDefault("CURRENT_TIMESTAMP")
	private LocalDateTime created;
	
	@UpdateTimestamp
	@ColumnDefault("CURRENT_TIMESTAMP")
	private LocalDateTime updated;

	@OneToMany(mappedBy = "rechnung", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<RechnungItemEntity> items;
    
    public RechnungEntity() {
    	super();
    	items = new HashSet<>();
    }

	public RechnungEntity(UUID customerId,
						  Integer nummer,
						  String rechnungCreate,
						  String liferDate,
						  int rechnungType,
						  int status,
						  UUID userId) {
		this();
		this.customerId = customerId;
		this.nummer = nummer;
		this.rechnungCreate = rechnungCreate;
		this.liferDate = liferDate;
		this.status = status;
		this.userId = userId;
		this.rechnungType = rechnungType;
	}

	public RechnungEntity(UUID id,
						  UUID customerId,
						  Integer nummer,
						  String rechnungCreate,
						  String liferDate,
						  int rechnungType,
						  int status,
						  UUID userId,
						  LocalDateTime updated) {
		this();
		this.id = id;
		this.customerId = customerId;
		this.nummer = nummer;
		this.rechnungCreate = rechnungCreate;
		this.liferDate = liferDate;
		this.status = status;
		this.userId = userId;
		this.rechnungType = rechnungType;
		this.updated = updated;
	}

	public UUID getId() {
		return id;
	}

	@Override
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

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	@Override
	public LocalDateTime getCreated() {
		return created;
	}

	@Override
	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	@Override
	public LocalDateTime getUpdated() {
		return updated;
	}

	@Override
	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
	}


}
