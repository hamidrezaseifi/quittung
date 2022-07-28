package de.seifi.rechnung_common.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.*;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


@Entity
@Table(name = "produkt")
public class ProduktEntity extends EntityBase {
	
	@Id
	@GeneratedValue
	private UUID id;
	
	@Column(name="produkt_name", nullable = false)
	private String produktName;
	
	@Column(name="last_preis", nullable = false)
	private float lastPreis;
    
	@CreationTimestamp
	@ColumnDefault("CURRENT_TIMESTAMP")
	private LocalDateTime created;
	
	@UpdateTimestamp
	@ColumnDefault("CURRENT_TIMESTAMP")
	private LocalDateTime updated;

	public ProduktEntity() {
		super();
		
	}

	public ProduktEntity(String produktName, float lastPreis) {
		super();
		this.produktName = produktName;
		this.lastPreis = lastPreis;
	}
	

	public ProduktEntity(UUID id,
			String produktName, 
			float lastPreis,
			LocalDateTime updated) {
		super();
		this.id = id;
		this.produktName = produktName;
		this.lastPreis = lastPreis;
        this.updated = updated;
	}

	
	public UUID getId() {
		return id;
	}

	@Override
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
