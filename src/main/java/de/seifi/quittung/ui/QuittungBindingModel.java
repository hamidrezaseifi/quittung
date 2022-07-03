package de.seifi.quittung.ui;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.SetChangeListener;

public class QuittungBindingModel {
    private int INITIAL_ITEMS = 10;

    private ObservableList<QuittungItemProperty> quittungItems;

    //private ObjectProperty<ObservableList<QuittungItemProperty>> quittungItemsProperty;

    private FloatProperty gesamtSumme;
    private FloatProperty nettoSumme;
    private FloatProperty mvstSumme;

    public QuittungBindingModel() {
        gesamtSumme = new SimpleFloatProperty(0);
        nettoSumme = new SimpleFloatProperty(0);
        mvstSumme = new SimpleFloatProperty(0);

        quittungItems = FXCollections.observableArrayList();
        while (quittungItems.size() < INITIAL_ITEMS){
            quittungItems.add(new QuittungItemProperty());
        }

        calculateQuittungSumme();
    }

    public void calculateQuittungSumme() {
        float netto = 0;

        for(QuittungItemProperty i:quittungItems){
            netto += i.getGesamt();
        }

        nettoSumme.set(netto);
        mvstSumme.set(netto * 19 / 100);
        gesamtSumme.set(nettoSumme.getValue() + mvstSumme.getValue());
    }

    public ObservableList<QuittungItemProperty> getQuittungItems() {
        return quittungItems;
    }

    public void setQuittungItems(ObservableList<QuittungItemProperty> quittungItems) {
        this.quittungItems = quittungItems;
    }

    /*public ObservableList<QuittungItemProperty> getQuittungItemsProperty() {
        return quittungItemsProperty.get();
    }

    public ObjectProperty<ObservableList<QuittungItemProperty>> quittungItemsPropertyProperty() {
        return quittungItemsProperty;
    }

    public void setQuittungItemsProperty(ObservableList<QuittungItemProperty> quittungItemsProperty) {
        this.quittungItemsProperty.set(quittungItemsProperty);
    }*/

    public float getGesamtSumme() {
        return gesamtSumme.get();
    }

    public FloatProperty gesamtSummeProperty() {
        return gesamtSumme;
    }

    public void setGesamtSumme(float gesamtSumme) {
        this.gesamtSumme.set(gesamtSumme);
    }

    public float getNettoSumme() {
        return nettoSumme.get();
    }

    public FloatProperty nettoSummeProperty() {
        return nettoSumme;
    }

    public void setNettoSumme(float nettoSumme) {
        this.nettoSumme.set(nettoSumme);
    }

    public float getMvstSumme() {
        return mvstSumme.get();
    }

    public FloatProperty mvstSummeProperty() {
        return mvstSumme;
    }

    public void setMvstSumme(float mvstSumme) {
        this.mvstSumme.set(mvstSumme);
    }
}
