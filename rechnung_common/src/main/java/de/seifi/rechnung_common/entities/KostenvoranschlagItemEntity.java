package de.seifi.rechnung_common.entities;

import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Table;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "kostenvoranschlag_item")
public class KostenvoranschlagItemEntity extends EntityBase {

	@Id
	@GeneratedValue
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(foreignKey = @ForeignKey(name = "kostenvoranschlag_item_kostenvoranschlag_fkey"))
	private KostenvoranschlagEntity kostenvoranschlag;

	@Column(nullable = false)
	private String produkt;

	@Column(name="original_nummer", nullable = false)
	private String originalNummer;

	@Column(name="teil_nummer", nullable = false)
	private String teilNummer;

	@Column(name="marke", nullable = false)
	private String marke;

	@Column(nullable = false)
	private Boolean bestellt;

	@Column(nullable = false)
	private Float preis;

	@CreationTimestamp
	@ColumnDefault("CURRENT_TIMESTAMP")
	private LocalDateTime created;

	@UpdateTimestamp
	@ColumnDefault("CURRENT_TIMESTAMP")
	private LocalDateTime updated;

    public KostenvoranschlagItemEntity() {
    	super();
    }

	public KostenvoranschlagItemEntity(String produkt,
									   String originalNummer,
									   String teilNummer,
									   String marke,
									   Boolean bestellt,
									   Float preis) {
		this.produkt = produkt;
		this.originalNummer = originalNummer;
		this.teilNummer = teilNummer;
		this.marke = marke;
		this.bestellt = bestellt;
		this.preis = preis;
	}

	public KostenvoranschlagItemEntity(UUID id,
									   String produkt,
									   String originalNummer,
									   String teilNummer,
									   String marke,
									   Boolean bestellt,
									   Float preis,
									   LocalDateTime created,
									   LocalDateTime updated) {
		this.id = id;
		this.produkt = produkt;
		this.originalNummer = originalNummer;
		this.teilNummer = teilNummer;
		this.marke = marke;
		this.bestellt = bestellt;
		this.preis = preis;
		this.created = created;
		this.updated = updated;
	}

	@Override
	public UUID getId() {
		return id;
	}

	@Override
	public void setId(UUID id) {
		this.id = id;
	}

	public KostenvoranschlagEntity getKostenvoranschlag() {
		return kostenvoranschlag;
	}

	public void setKostenvoranschlag(KostenvoranschlagEntity kostenvoranschlag) {
		this.kostenvoranschlag = kostenvoranschlag;
	}

	public String getProdukt() {
		return produkt;
	}

	public void setProdukt(String produkt) {
		this.produkt = produkt;
	}

	public String getOriginalNummer() {
		return originalNummer;
	}

	public void setOriginalNummer(String originalNummer) {
		this.originalNummer = originalNummer;
	}

	public String getTeilNummer() {
		return teilNummer;
	}

	public void setTeilNummer(String teilNummer) {
		this.teilNummer = teilNummer;
	}

	public String getMarke() {
		return marke;
	}

	public void setMarke(String marke) {
		this.marke = marke;
	}

	public Boolean getBestellt() {
		return bestellt;
	}

	public void setBestellt(Boolean bestellt) {
		this.bestellt = bestellt;
	}

	public Float getPreis() {
		return preis;
	}

	public void setPreis(Float preis) {
		this.preis = preis;
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

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KostenvoranschlagItemEntity)) return false;
        return id != null && id.equals(((KostenvoranschlagItemEntity) o).getId());
    }
	
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    
}
