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

	@Column(name="artikel_nummer", nullable = false)
	private String artikelNummer;

	@Column(nullable = false)
	private Integer menge;

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
                                       String artikelNummer,
                                       Integer menge,
                                       Float preis) {
    	super();
        this.produkt = produkt;
        this.artikelNummer = artikelNummer;
        this.menge = menge;
        this.preis = preis;
    }

    public KostenvoranschlagItemEntity(UUID id,
                                       String produkt,
                                       String artikelNummer,
                                       Integer menge,
                                       Float preis,
                                       LocalDateTime updated) {
    	super();
        this.id = id;
        this.produkt = produkt;
        this.artikelNummer = artikelNummer;
        this.menge = menge;
        this.preis = preis;
        this.updated = updated;
   }


	public String getProdukt() {
        return produkt;
    }

    public void setProdukt(String produkt) {
        this.produkt = produkt;
    }

    public Integer getMenge() {
        return menge;
    }

    public void setMenge(Integer menge) {
        this.menge = menge;
    }

    public Float getPreis() {
        return preis;
    }

    public void setPreis(Float preis) {
        this.preis = preis;
    }

    public Float getGesmt() {
        return preis * menge;
    }

    public String getArtikelNummer() {
        return artikelNummer;
    }

    public void setArtikelNummer(String artikelNummer) {
        this.artikelNummer = artikelNummer;
    }

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
