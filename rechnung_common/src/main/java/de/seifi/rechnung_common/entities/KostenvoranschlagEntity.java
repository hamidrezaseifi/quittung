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

	@Column(nullable = false)
	private String nummer;

	@Column(name="schluessel_nummer", nullable = true)
	private String schluesselNummer;

	@Column(name="fahrgestell_uummer", nullable = true)
	private String fahrgestellNummer;

	@Column(name="fahrzeug_schein", nullable = true)
	private UUID fahrzeugSchein;

	@ColumnDefault("1")
	@Column(nullable = false)
	private Integer status;

	@CreationTimestamp
	@ColumnDefault("CURRENT_TIMESTAMP")
	private LocalDateTime created;

	@UpdateTimestamp
	@ColumnDefault("CURRENT_TIMESTAMP")
	private LocalDateTime updated;

	@OneToMany(mappedBy = "kostenvoranschlag", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<KostenvoranschlagItemEntity> items;

    public KostenvoranschlagEntity() {
    	super();
    	items = new HashSet<>();
		this.customerId = null;
    }

	public KostenvoranschlagEntity(UUID id,
								   UUID customerId,
								   String nummer,
								   String schluesselNummer,
								   String fahrgestellNummer,
								   UUID fahrzeugSchein,
								   Integer status,
								   LocalDateTime created,
								   LocalDateTime updated) {
		this();
		this.id = id;
		this.customerId = customerId;
		this.nummer = nummer;
		this.schluesselNummer = schluesselNummer;
		this.fahrgestellNummer = fahrgestellNummer;
		this.fahrzeugSchein = fahrzeugSchein;
		this.status = status;
		this.created = created;
		this.updated = updated;
	}

	public KostenvoranschlagEntity(UUID customerId,
								   String nummer,
								   String schluesselNummer,
								   String fahrgestellNummer,
								   UUID fahrzeugSchein,
								   Integer status) {
		this();
		this.customerId = customerId;
		this.nummer = nummer;
		this.schluesselNummer = schluesselNummer;
		this.fahrgestellNummer = fahrgestellNummer;
		this.fahrzeugSchein = fahrzeugSchein;
		this.status = status;
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

	public String getNummer() {
		return nummer;
	}

	public void setNummer(String nummer) {
		this.nummer = nummer;
	}

	public String getSchluesselNummer() {
		return schluesselNummer;
	}

	public void setSchluesselNummer(String schluesselNummer) {
		this.schluesselNummer = schluesselNummer;
	}

	public String getFahrgestellNummer() {
		return fahrgestellNummer;
	}

	public void setFahrgestellNummer(String fahrgestellNummer) {
		this.fahrgestellNummer = fahrgestellNummer;
	}

	public UUID getFahrzeugSchein() {
		return fahrzeugSchein;
	}

	public void setFahrzeugSchein(UUID fahrzeugSchein) {
		this.fahrzeugSchein = fahrzeugSchein;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setItems(Set<KostenvoranschlagItemEntity> items) {
		this.items = items;
	}

	public Set<KostenvoranschlagItemEntity> getItems() {
		return items;
	}

	public void setItems(List<KostenvoranschlagItemEntity> items) {
		this.items.clear();
		if(items != null) {
			items.forEach(i -> i.setKostenvoranschlag(this));
			this.items.addAll(items);
		}
		
	}

	public void addItem(KostenvoranschlagItemEntity item) {
		item.setKostenvoranschlag(this);
		this.items.add(item);
		
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
