package de.seifi.rechnung_manager_app.models;

import javafx.beans.property.*;

import java.time.LocalDateTime;
import java.util.UUID;

public class KostenvoranschlagItemProperty {
    private StringProperty produkt;
    private StringProperty originalNummer;
    private StringProperty teilNummer;
    private StringProperty marke;
    private FloatProperty preis;

    private BooleanProperty bestellt;
    private boolean newItem;

    private KostenvoranschlagItemModel existingItem = null;

    public KostenvoranschlagItemProperty(boolean newItem) {

        this.produkt = new SimpleStringProperty();
        this.originalNummer = new SimpleStringProperty();
        this.teilNummer = new SimpleStringProperty();
        this.marke = new SimpleStringProperty();
        this.preis = new SimpleFloatProperty(0f);
        this.bestellt = new SimpleBooleanProperty(false);
        this.newItem = newItem;
        this.existingItem = null;

    }

    public KostenvoranschlagItemProperty(KostenvoranschlagItemModel model) {
        this(model.isNew());
        this.produkt.set(model.getProdukt());
        this.originalNummer.set(model.getOriginalNummer());
        this.teilNummer.set(model.getTeilNummer());
        this.marke.set(model.getMarke());
        this.bestellt.set(model.isBestellt());
        this.preis.set(model.getPreis());
        this.existingItem = model;

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

    public float getPreis() {
        return preis.get();
    }

    public FloatProperty preisProperty() {
        return preis;
    }

    public void setPreis(float preis) {
        this.preis.set(preis);
    }

    public String getOriginalNummer() {
        return originalNummer.get();
    }

    public StringProperty originalNummerProperty() {
        return originalNummer;
    }

    public void setOriginalNummer(String originalNummer) {
        this.originalNummer.set(originalNummer);
    }

    public String getTeilNummer() {
        return teilNummer.get();
    }

    public StringProperty teilNummerProperty() {
        return teilNummer;
    }

    public void setTeilNummer(String teilNummer) {
        this.teilNummer.set(teilNummer);
    }

    public String getMarke() {
        return marke.get();
    }

    public StringProperty markeProperty() {
        return marke;
    }

    public void setMarke(String marke) {
        this.marke.set(marke);
    }

    public boolean isBestellt() {
        return bestellt.get();
    }

    public BooleanProperty bestelltProperty() {
        return bestellt;
    }

    public void setBestellt(boolean bestellt) {
        this.bestellt.set(bestellt);
    }

    public void setNewItem(boolean newItem) {
        this.newItem = newItem;
    }

    public boolean isNewItem() {
		return newItem;
	}

	public KostenvoranschlagItemModel toModel(){
        UUID existingId = this.existingItem != null ? this.existingItem.getId() : null;
        LocalDateTime created = this.existingItem != null ? this.existingItem.getCreated() : null;
        LocalDateTime updated = this.existingItem != null ? this.existingItem.getUpdated() : null;

        return new KostenvoranschlagItemModel(existingId,
                                              this.produkt.get(),
                                              this.originalNummer.get(),
                                              this.teilNummer.get(),
                                              this.marke.get(),
                                              this.bestellt.get(),
                                              this.preis.get(),
                                              created,
                                              updated);
    }

    public boolean isValid(){
    	if(isEmpty()){
            return true;
        }

        return !isPropertyEmpty(produkt) && !isPropertyEmpty(originalNummer) && !isPropertyEmpty(teilNummer) &&
        		!isPropertyEmpty(preis) && !isPropertyEmpty(marke);
    }

    public boolean isEmpty(){

        return isPropertyEmpty(produkt) && isPropertyEmpty(originalNummer) && isPropertyEmpty(teilNummer) && isPropertyEmpty(preis) && isPropertyEmpty(marke);
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

    
}
