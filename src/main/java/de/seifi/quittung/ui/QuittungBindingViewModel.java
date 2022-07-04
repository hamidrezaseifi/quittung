package de.seifi.quittung.ui;

import de.seifi.quittung.db.DbConnection;
import de.seifi.quittung.models.QuittungModel;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.SetChangeListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class QuittungBindingViewModel {
    private int INITIAL_ITEMS = 10;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyy");

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private ObservableList<QuittungItemProperty> quittungItems;

    private FloatProperty gesamtSumme;
    private FloatProperty nettoSumme;
    private FloatProperty mvstSumme;
    
    private float berechnenFaktorBasis = 1f;
    private float berechnenFaktorZiel = 1f;
    
    private QuittungModel savingModel = null;
    

    public QuittungBindingViewModel(float berechnenFaktorBasis, float berechnenFaktorZiel) {
    	this.berechnenFaktorBasis = berechnenFaktorBasis;
    	this.berechnenFaktorZiel = berechnenFaktorZiel;
    	
        gesamtSumme = new SimpleFloatProperty(0);
        nettoSumme = new SimpleFloatProperty(0);
        mvstSumme = new SimpleFloatProperty(0);

        quittungItems = FXCollections.observableArrayList();
        reset();

        
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

	public float calculateNettoPreis(Float value) {
		
		float netto = (value * 100) / 119;
		netto = (netto * berechnenFaktorZiel) / berechnenFaktorBasis;
		
		return netto;
	}

	public float getBerechnenFaktorBasis() {
		return berechnenFaktorBasis;
	}

	public void setBerechnenFaktorBasis(float berechnenFaktorBasis) {
		this.berechnenFaktorBasis = berechnenFaktorBasis;
	}

	public float getBerechnenFaktorZiel() {
		return berechnenFaktorZiel;
	}

	public void setBerechnenFaktorZiel(float berechnenFaktorZiel) {
		this.berechnenFaktorZiel = berechnenFaktorZiel;
	}

	public void reset() {
		quittungItems.clear();
        while (quittungItems.size() < INITIAL_ITEMS){
            quittungItems.add(new QuittungItemProperty());
        }
        calculateQuittungSumme();
        savingModel = null;
	}


    public boolean save() {
        if(savingModel == null){
            LocalDateTime ldt = LocalDateTime.now();

            String date = dateFormatter.format(ldt);
            String time = timeFormatter.format(ldt);

            int lastNummer = DbConnection.getLastQuittungNummer(date);

            savingModel = new QuittungModel(lastNummer + 1, date, time);
            savingModel.getItems().addAll(quittungItems.stream().filter(qi -> qi.isValid()).map(qi -> qi.toModel()).collect(Collectors.toList()));
            DbConnection.addQuittung(savingModel);

        }

        return true;
    }
}
