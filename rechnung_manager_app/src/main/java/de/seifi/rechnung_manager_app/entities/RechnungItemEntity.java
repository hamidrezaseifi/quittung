package de.seifi.rechnung_manager_app.entities;

import java.time.LocalDateTime;

import javax.persistence.*;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import de.seifi.rechnung_manager_app.models.RechnungItemModel;

@Entity
@Table(name = "rechnung_item")
public class RechnungItemEntity extends EntityBase {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	  
	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(foreignKey = @ForeignKey(name = "rechnung_item_rechnung_fkey"))
	private RechnungEntity rechnung;

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

    public RechnungItemEntity() {
    	super();
    }

    public RechnungItemEntity(String produkt,
                             String artikelNummer,
                             Integer menge,
                             Float preis) {
    	super();
        this.produkt = produkt;
        this.artikelNummer = artikelNummer;
        this.menge = menge;
        this.preis = preis;
    }

    public RechnungItemEntity(Integer id,
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

	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public RechnungEntity getRechnung() {
		return rechnung;
	}

	public void setRechnung(RechnungEntity rechnung) {
		this.rechnung = rechnung;

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

	public RechnungItemModel toModel() {
		RechnungItemModel model = new RechnungItemModel(id, produkt, artikelNummer, menge, preis, created, updated);
		
		return model;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RechnungItemEntity )) return false;
        return id != null && id.equals(((RechnungItemEntity) o).getId());
    }
	
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    
}
