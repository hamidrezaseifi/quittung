package de.seifi.quittung.models;

public class ProduktModel {
	
	private int id;
	
	private String produkt;
	
	private float lastPreis;

	public ProduktModel() {
		super();
		this.produkt = "";
		this.lastPreis = -1;
	}

	public ProduktModel(String produkt, float lastPreis) {
		super();
		this.produkt = produkt;
		this.lastPreis = lastPreis;
	}
	

	public ProduktModel(int id, String produkt, float lastPreis) {
		super();
		this.id = id;
		this.produkt = produkt;
		this.lastPreis = lastPreis;
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

	public float getLastPreis() {
		return lastPreis;
	}

	public void setLastPreis(float lastPreis) {
		this.lastPreis = lastPreis;
	}
	
	@Override
	public String toString() {
		return produkt;
	}
	

}
