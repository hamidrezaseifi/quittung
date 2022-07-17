package de.seifi.rechnung_manager_app.entities;

import de.seifi.rechnung_manager_app.models.QuittungItemModel;
import de.seifi.rechnung_manager_app.models.RechnungItemModel;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "quittung_item")
public class QuittungItemEntity extends EntityBase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;


	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
	//@JoinColumn(name = "rechnung_id")
	@JoinColumn(foreignKey = @ForeignKey(name = "quittung_item_rechnung_fkey"))
	private QuittungEntity quittung;

	//private Integer rechnung_id;

	private String produkt;

	@Column(name="artikel_nummer")
	private String artikelNummer;

	private int menge;

	private float preis;

	private Timestamp created;

	private Timestamp updated;

    public QuittungItemEntity() {
    	super();
    }

    public QuittungItemEntity(String produkt,
                              String artikelNummer,
                              int menge,
                              float preis) {
    	super();
        this.produkt = produkt;
        this.artikelNummer = artikelNummer;
        this.menge = menge;
        this.preis = preis;
    }

    public QuittungItemEntity(Integer id,
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

    public QuittungEntity getQuittung() {
        return quittung;
    }

    public void setQuittung(QuittungEntity quittung) {
        this.quittung = quittung;
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

	public QuittungItemModel toModel() {
        QuittungItemModel model = new QuittungItemModel(id, produkt, artikelNummer, menge, preis, created.toLocalDateTime(), updated.toLocalDateTime());
		
		return model;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuittungItemEntity)) return false;
        return id != null && id.equals(((QuittungItemEntity) o).getId());
    }
	
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    
}
