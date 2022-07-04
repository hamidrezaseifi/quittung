package de.seifi.quittung.ui;

import de.seifi.quittung.models.QuittungItemModel;
import javafx.beans.property.*;

public class QuittungItemProperty {
    private StringProperty bezeichnung;
    private IntegerProperty menge;
    private FloatProperty brutoPreis;
    private FloatProperty preis;
    private FloatProperty gesamt;

    public QuittungItemProperty() {
        this.bezeichnung = new SimpleStringProperty();
        this.menge = new SimpleIntegerProperty(0);
        this.brutoPreis = new SimpleFloatProperty(0f);
        this.preis = new SimpleFloatProperty(0f);
        this.gesamt = new SimpleFloatProperty(0f);

    }

    public QuittungItemProperty(QuittungItemModel model) {
        this();
        this.bezeichnung.set(model.getBezeichnung());
        this.menge.set(model.getMenge());
        this.preis.set(model.getPreis());
        this.gesamt.set(model.getGesmt());
    }

    public QuittungItemProperty(
            String bezeichnung,
            int menge,
            float preis) {
        this();
        this.bezeichnung.set(bezeichnung);
        this.menge.set(menge);
        this.preis.set(preis);
        this.gesamt.set(preis * menge);
    }


    public String getBezeichnung() {
        return bezeichnung.get();
    }

    public StringProperty bezeichnungProperty() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung.set(bezeichnung);
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

    public QuittungItemModel toModel(){
        return new QuittungItemModel(this.bezeichnung.get(), this.menge.get(), this.preis.get());
    }

    public boolean isValid(){
        if(bezeichnung.get() == null){
            return false;
        }

        return (!bezeichnung.get().trim().isEmpty() ) && (menge.get() > 0) && (preis.get() > 0);
    }
	public FloatProperty brutoPreisProperty() {
		return brutoPreis;
	}
	
	public void calculateGesamt() {
		this.setGesamt(this.getPreis() * this.getMenge());
	}
    
}
