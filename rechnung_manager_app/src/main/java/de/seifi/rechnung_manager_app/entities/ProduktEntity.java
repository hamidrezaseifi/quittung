package de.seifi.rechnung_manager_app.entities;

import java.sql.Timestamp;

import javax.persistence.*;

import de.seifi.rechnung_manager_app.models.ProduktModel;


@Entity
@Table(name = "produkt_last_preis")
public class ProduktEntity extends EntityBase {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="produkt_name")
	private String produktName;
	
	@Column(name="last_preis")
	private float lastPreis;
    
	private Timestamp created;
	
	private Timestamp updated;

	public ProduktEntity() {
		super();
		
	}

	public ProduktEntity(String produktName, float lastPreis) {
		super();
		this.produktName = produktName;
		this.lastPreis = lastPreis;
	}
	

	public ProduktEntity(Integer id, 
			String produktName, 
			float lastPreis,
            Timestamp created,
            Timestamp updated) {
		super();
		this.id = id;
		this.produktName = produktName;
		this.lastPreis = lastPreis;
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
	
	public ProduktModel toModel() {
		ProduktModel model = new ProduktModel(id, produktName, lastPreis, created.toLocalDateTime(), updated.toLocalDateTime());
		
		return model;
	}

}
