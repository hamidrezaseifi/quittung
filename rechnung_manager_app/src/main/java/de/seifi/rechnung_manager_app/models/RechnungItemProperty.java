package de.seifi.rechnung_manager_app.models;

import javafx.beans.property.*;

public class RechnungItemProperty {
    private StringProperty produkt;
    private StringProperty artikelNummer;
    private IntegerProperty menge;
    private FloatProperty brutoPreis;
    private FloatProperty preis;
    private FloatProperty gesamt;
    private BooleanProperty markedAsDeleted;
    private boolean newItem;

    public RechnungItemProperty(boolean newItem) {
    	
        this.produkt = new SimpleStringProperty();
        this.artikelNummer = new SimpleStringProperty();
        this.menge = new SimpleIntegerProperty(0);
        this.brutoPreis = new SimpleFloatProperty(0f);
        this.preis = new SimpleFloatProperty(0f);
        this.gesamt = new SimpleFloatProperty(0f);
        this.markedAsDeleted = new SimpleBooleanProperty(false);
        this.newItem = newItem;
        
    }

    public RechnungItemProperty(RechnungItemModel model) {
        this(model.isNew());
        this.produkt.set(model.getProdukt());
        this.artikelNummer.set(model.getArtikelNummer());
        this.menge.set(model.getMenge());
        this.preis.set(model.getPreis());
        this.gesamt.set(model.getGesmt());
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

	public Boolean getIsMarkedAsDeleted() {
		return markedAsDeleted.get();
	}

	public BooleanProperty getIsMarkedAsDeletedProperty() {
		return markedAsDeleted;
	}

	public void setIsMarkedAsDeleted(Boolean value) {
		this.markedAsDeleted.set(value);
	}
	
	
    public boolean isNewItem() {
		return newItem;
	}

	public RechnungItemModel toModel(){
        return new RechnungItemModel(this.produkt.get(), this.artikelNummer.get(), this.menge.get(), this.preis.get());
    }

    public boolean isValid(){
    	if(isEmpty()){
            return true;
        }

        return !isPropertyEmpty(produkt) && !isPropertyEmpty(artikelNummer) && !isPropertyEmpty(menge) && 
        		!isBrutoPreisEmpty() && !isPropertyEmpty(preis) && !isPropertyEmpty(gesamt);
    }

    public boolean isEmpty(){

        return isPropertyEmpty(produkt) && isPropertyEmpty(artikelNummer) && isPropertyEmpty(menge) && isBrutoPreisEmpty() && isPropertyEmpty(preis) && isPropertyEmpty(gesamt);
    }

    public boolean canSaved(){

        return !isEmpty() && isValid() && !getIsMarkedAsDeleted();
    }

    private boolean isPropertyEmpty(StringProperty prop) {
    	return prop.getValue() == null || prop.getValue().toString().trim().isEmpty() || prop.getValue().toString().trim().isBlank();
    }

    private boolean isPropertyEmpty(FloatProperty prop) {
        return prop.getValue() == null || prop.getValue() == 0.0;
    }

    private boolean isBrutoPreisEmpty() {
        return newItem && (brutoPreis.getValue() == null || brutoPreis.getValue() == 0.0);
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
