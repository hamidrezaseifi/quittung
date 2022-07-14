package de.seifi.rechnung_manager_app.models;

import javafx.beans.property.*;

public class RechnungItemPrintProperty {
    private IntegerProperty index;
    private StringProperty produkt;
    private StringProperty artikelNummer;
    private IntegerProperty menge;
    private FloatProperty einzelpreis;
    private FloatProperty gesamtpreis;

    public RechnungItemPrintProperty() {

        this.index = new SimpleIntegerProperty(0);
        this.produkt = new SimpleStringProperty();
        this.artikelNummer = new SimpleStringProperty();
        this.menge = new SimpleIntegerProperty(0);
        this.einzelpreis = new SimpleFloatProperty(0f);
        this.gesamtpreis = new SimpleFloatProperty(0f);

    }

    public RechnungItemPrintProperty(int index, RechnungItemModel model) {
        this();
        this.index.set(index);
        this.produkt.set(model.getProdukt());
        this.artikelNummer.set(model.getArtikelNummer());
        this.menge.set(model.getMenge());
        this.einzelpreis.set(model.getPreis());
        this.gesamtpreis.set(model.getGesmt());
    }

    public int getIndex() {
        return index.get();
    }

    public void setIndex(int index) {
        this.index.set(index);
    }

    public IntegerProperty indexProperty() {
        return index;
    }

    public String getProdukt() {
        return produkt.get();
    }

    public StringProperty produktProperty() {
        return produkt;
    }

    public String getArtikelNummer() {
        return artikelNummer.get();
    }

    public StringProperty artikelNummerProperty() {
        return artikelNummer;
    }

    public int getMenge() {
        return menge.get();
    }

    public IntegerProperty mengeProperty() {
        return menge;
    }

    public float getEinzelpreis() {
        return einzelpreis.get();
    }

    public FloatProperty einzelpreisProperty() {
        return einzelpreis;
    }

    public float getGesamtpreis() {
        return gesamtpreis.get();
    }

    public FloatProperty gesamtpreisProperty() {
        return gesamtpreis;
    }
}
