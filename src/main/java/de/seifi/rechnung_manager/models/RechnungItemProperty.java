package de.seifi.rechnung_manager.models;

import de.seifi.rechnung_manager.models.RechnungItemModel;
import javafx.beans.property.*;

public class RechnungItemProperty {
    private StringProperty produkt;
    private StringProperty artikelNummer;
    private IntegerProperty menge;
    private FloatProperty brutoPreis;
    private FloatProperty preis;
    private FloatProperty gesamt;

    public RechnungItemProperty() {
    	
        this.produkt = new SimpleStringProperty();
        this.artikelNummer = new SimpleStringProperty();
        this.menge = new SimpleIntegerProperty(0);
        this.brutoPreis = new SimpleFloatProperty(0f);
        this.preis = new SimpleFloatProperty(0f);
        this.gesamt = new SimpleFloatProperty(0f);
        
    }

    public RechnungItemProperty(RechnungItemModel model) {
        this();
        this.produkt.set(model.getProdukt());
        this.artikelNummer.set(model.getArtikelNummer());
        this.menge.set(model.getMenge());
        this.preis.set(model.getPreis());
        this.gesamt.set(model.getGesmt());
    }

    public RechnungItemProperty(
            String produkt,
            String artikelNummer,
            int menge,
            float preis) {
        this();
        this.produkt.set(produkt);
        this.artikelNummer.set(artikelNummer);
        this.menge.set(menge);
        this.preis.set(preis);
        this.gesamt.set(preis * menge);
    }


    public String getProdukt() {
        return produkt.get();
    }

    public StringProperty produktProperty() {
        return produkt;
    }

    public void setProdukt(String produkt) {
        this.produkt.set(produkt);
    }

    public String getArtikelNummer() {
        return artikelNummer.get();
    }

    public StringProperty artikelNummerProperty() {
        return artikelNummer;
    }

    public void setArtikelNummer(String artikelNummer) {
        this.artikelNummer.set(artikelNummer);
    }

    public int getMenge() {
        return menge.get();
    }

    public IntegerProperty mengeProperty() {
        return menge;
    }

    public void setMenge(int menge) {
        this.menge.set(menge);
        this.calculateGesamt();
    }

    public float getPreis() {
        return preis.get();
    }

    public FloatProperty preisProperty() {
        return preis;
    }

    public void setPreis(float preis) {
        this.preis.set(preis);
        this.calculateGesamt();
    }

    public float getGesamt() {
        return gesamt.get();
    }

    public FloatProperty gesamtProperty() {
        return gesamt;
    }

    public void setGesamt(float gesamt) {
        this.gesamt.set(gesamt);
    }

	public Float getBrutoPreis() {
		return brutoPreis.get();
	}

	public void setBrutoPreis(float brutoPreis) {
		this.brutoPreis.set(brutoPreis);
	}

    public RechnungItemModel toModel(){
        return new RechnungItemModel(this.produkt.get(), this.artikelNummer.get(), this.menge.get(), this.preis.get());
    }

    public boolean isValid(){
    	if(isEmpty()){
            return true;
        }

        return !isPropertyEmpty(produkt) && !isPropertyEmpty(artikelNummer) && !isPropertyEmpty(menge) && 
        		!isPropertyEmpty(brutoPreis) && !isPropertyEmpty(preis) && !isPropertyEmpty(gesamt);
    }

    public boolean isEmpty(){

        return isPropertyEmpty(produkt) && isPropertyEmpty(artikelNummer) && isPropertyEmpty(menge) && isPropertyEmpty(brutoPreis) && isPropertyEmpty(preis) && isPropertyEmpty(gesamt);
    }

    public boolean canSaved(){

        return !isEmpty() && isValid();
    }

    private boolean isPropertyEmpty(StringProperty prop) {
    	return prop.getValue() == null || prop.getValue().toString().trim().isEmpty() || prop.getValue().toString().trim().isBlank();
    }

    
    private boolean isPropertyEmpty(FloatProperty prop) {
    	return prop.getValue() == null || prop.getValue() == 0.0;
    }

    private boolean isPropertyEmpty(IntegerProperty prop) {
    	return prop.getValue() == null || prop.getValue() == 0;
    }
    
	public FloatProperty brutoPreisProperty() {
		return brutoPreis;
	}
	
	public void calculateGesamt() {
		this.setGesamt(this.getPreis() * this.getMenge());
	}
    
}
