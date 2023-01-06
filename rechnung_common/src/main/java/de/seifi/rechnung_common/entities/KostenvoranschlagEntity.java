package de.seifi.rechnung_common.entities;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "kostenvoranschlag")
public class KostenvoranschlagEntity extends EntityBase {

	@Id
	@GeneratedValue
    private UUID id;

	@Column(name="customer_id", nullable = true)
	private UUID customerId;

	@Column(name="reference_id", nullable = true)
	private UUID referenceId;

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

	@Column(name="rechnung_version", nullable = false)
	@ColumnDefault("1")
	private Integer rechnungVersion;

	@Column(name="payment_type", nullable = false)
	@ColumnDefault("1")
	private Integer paymentType;

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

    public KostenvoranschlagEntity() {
    	super();
    	items = new HashSet<>();
		this.rechnungVersion = 1;
		this.paymentType = 1;
		this.referenceId = null;
		this.customerId = null;
    }

	public KostenvoranschlagEntity(UUID customerId,
                                   UUID referenceId,
                                   Integer nummer,
                                   String rechnungCreate,
                                   String liferDate,
                                   int rechnungVersion,
                                   int paymentType,
                                   int rechnungType,
                                   int status,
                                   UUID userId) {
		this();
		this.customerId = customerId;
		this.referenceId = referenceId;
		this.nummer = nummer;
		this.rechnungCreate = rechnungCreate;
		this.liferDate = liferDate;
		this.status = status;
		this.userId = userId;
		this.rechnungType = rechnungType;
		this.rechnungVersion = rechnungVersion;
		this.paymentType = paymentType;
	}

	public KostenvoranschlagEntity(UUID id,
                                   UUID referenceId,
                                   UUID customerId,
                                   Integer nummer,
                                   String rechnungCreate,
                                   String liferDate,
                                   int rechnungVersion,
                                   int paymentType,
                                   int rechnungType,
                                   int status,
                                   UUID userId,
                                   LocalDateTime updated) {
		this();
		this.id = id;
		this.customerId = customerId;
		this.referenceId = referenceId;
		this.nummer = nummer;
		this.rechnungCreate = rechnungCreate;
		this.liferDate = liferDate;
		this.status = status;
		this.userId = userId;
		this.rechnungType = rechnungType;
		this.rechnungVersion = rechnungVersion;
		this.paymentType = paymentType;
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
	
	public UUID getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(UUID referenceId) {
		this.referenceId = referenceId;
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

	public Integer getRechnungVersion() {
		return rechnungVersion;
	}

	public void setRechnungVersion(Integer rechnungVersion) {
		this.rechnungVersion = rechnungVersion;
	}

	public Integer getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
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
