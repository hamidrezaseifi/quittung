package de.seifi.rechnung_manager_app.models;

import java.time.LocalDateTime;
import java.util.UUID;


public class RechnungItemModel {
	
	private UUID id;
    
	private String produkt;
    
	private String artikelNummer;
    
	private int menge;
    
	private float preis;
    
	private LocalDateTime created;
	
	private LocalDateTime updated;

    public RechnungItemModel() {
    	super();
    	id = null;
    }

    public RechnungItemModel(String produkt,
                             String artikelNummer,
                             int menge,
                             float preis) {
    	this();
        this.produkt = produkt;
        this.artikelNummer = artikelNummer;
        this.menge = menge;
        this.preis = preis;
    }

    public RechnungItemModel(UUID id,
    						String produkt,
                             String artikelNummer,
                             int menge,
                             float preis,
                             LocalDateTime created,
                             LocalDateTime updated) {
    	this();
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

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
	}

	public boolean isNew() {
		return id == null;
	}

    
}
