package de.seifi.quittung.ui;

import de.seifi.quittung.models.QuittungItemModel;
import javafx.beans.property.*;

public class QuittungItemProperty {
    private StringProperty bezeichnung;
    private IntegerProperty menge;
    private FloatProperty preis;
    private FloatProperty gesamt;

    public QuittungItemProperty() {
        this.bezeichnung = new SimpleStringProperty();
        this.menge = new SimpleIntegerProperty();
        this.preis = new SimpleFloatProperty();
        this.gesamt = new SimpleFloatProperty();

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
    }

    public float getPreis() {
        return preis.get();
    }

    public FloatProperty preisProperty() {
        return preis;
    }

    public void setPreis(float preis) {
        this.preis.set(preis);
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
}
