package de.seifi.quittung.models;

public class QuittungItemModel {
	private int id;
	private int quittungId;
    private String produkt;
    private String artikelNummer;
    private int menge;
    private float preis;

    public QuittungItemModel() {
    	id = 0;
    }

    public QuittungItemModel(String produkt,
                             String artikelNummer,
                             int menge,
                             float preis) {
    	this();
        this.produkt = produkt;
        this.artikelNummer = artikelNummer;
        this.menge = menge;
        this.preis = preis;
    }

    public QuittungItemModel(int id,
    						String produkt,
                             String artikelNummer,
                             int menge,
                             float preis) {
    	this();
        this.id = id;
        this.produkt = produkt;
        this.artikelNummer = artikelNummer;
        this.menge = menge;
        this.preis = preis;
    }

    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getQuittungId() {
		return quittungId;
	}

	public void setQuittungId(int quittungId) {
		this.quittungId = quittungId;
	}
    
    
}
