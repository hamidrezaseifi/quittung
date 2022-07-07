package de.seifi.rechnung_manager.entities;

import java.sql.Timestamp;

import javax.persistence.*;

import de.seifi.rechnung_manager.models.RechnungItemModel;

@Entity
@Table(name = "rechnung_item")
public class RechnungItemEntity extends EntityBase {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	  
	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
	//@JoinColumn(name = "rechnung_id")
	@JoinColumn(foreignKey = @ForeignKey(name = "rechnung_item_rechnung_fkey"))
	private RechnungEntity rechnung;
    
	//private Integer rechnung_id;

	private String produkt;
    
	@Column(name="artikel_nummer")
	private String artikelNummer;
    
	private int menge;
    
	private float preis;
    
	private Timestamp created;
	
	private Timestamp updated;

    public RechnungItemEntity() {
    	super();
    }

    public RechnungItemEntity(String produkt,
                             String artikelNummer,
                             int menge,
                             float preis) {
    	super();
        this.produkt = produkt;
        this.artikelNummer = artikelNummer;
        this.menge = menge;
        this.preis = preis;
    }

    public RechnungItemEntity(Integer id,
    						String produkt,
                            String artikelNummer,
                            int menge,
                            float preis,
                 			Timestamp created,
                			Timestamp updated) {
    	super();
        this.id = id;
        this.produkt = produkt;
        this.artikelNummer = artikelNummer;
        this.menge = menge;
        this.preis = preis;
        this.created = created;
        this.updated = updated;
   }


	public String getProdukt() {
        return produkt;
    }

    public void setProdukt(String produkt) {
        this.produkt = produkt;
    }

    public int getMenge() {
        return menge;
    }

    public void setMenge(int menge) {
        this.menge = menge;
    }

    public float getPreis() {
        return preis;
    }

    public void setPreis(float preis) {
        this.preis = preis;
    }

    public float getGesmt() {
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

	public RechnungItemModel toModel() {
		RechnungItemModel model = new RechnungItemModel(id, produkt, artikelNummer, menge, preis, created.toLocalDateTime(), updated.toLocalDateTime());
		
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
