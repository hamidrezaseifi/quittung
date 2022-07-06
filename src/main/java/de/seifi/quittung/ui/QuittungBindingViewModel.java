package de.seifi.quittung.ui;

import de.seifi.quittung.db.DbConnection;
import de.seifi.quittung.db.ProduktRepository;
import de.seifi.quittung.db.QuittungRepository;
import de.seifi.quittung.exception.DataSqlException;
import de.seifi.quittung.models.ProduktModel;
import de.seifi.quittung.models.QuittungItemModel;
import de.seifi.quittung.models.QuittungModel;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class QuittungBindingViewModel {
    private int INITIAL_ITEMS = 10;
    
    private final QuittungRepository quittungRepository = new QuittungRepository();
    private final ProduktRepository produktRepository = new ProduktRepository();
    
	    
    Map<String, ProduktModel> produktMap;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyy");

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private ObservableList<QuittungItemProperty> quittungItems;

    private FloatProperty gesamtSumme;
    private FloatProperty nettoSumme;
    private FloatProperty mvstSumme;
    
    private StringProperty quittungNummer;
    private StringProperty quittungDatum;
    private StringProperty liferDatum;
    
    private BooleanProperty disableSave;
    private BooleanProperty disablePrint;
    
    
    private float berechnenFaktorBasis = 1f;
    private float berechnenFaktorZiel = 1f;
    
    private QuittungModel savingModel = new QuittungModel();
        
    private boolean isDirty;
    
    public QuittungBindingViewModel(float berechnenFaktorBasis, float berechnenFaktorZiel) {
    	this.berechnenFaktorBasis = berechnenFaktorBasis;
    	this.berechnenFaktorZiel = berechnenFaktorZiel;
    	
        gesamtSumme = new SimpleFloatProperty(0);
        nettoSumme = new SimpleFloatProperty(0);
        mvstSumme = new SimpleFloatProperty(0);
        
        quittungNummer = new SimpleStringProperty();
        quittungDatum = new SimpleStringProperty();
        liferDatum = new SimpleStringProperty();
        
        disableSave = new SimpleBooleanProperty(true);
        disablePrint = new SimpleBooleanProperty(false);
        
        quittungItems = FXCollections.observableArrayList();
        
        retreiveProduktMap();
        
        reset();

    }

    private void retreiveProduktMap() {
    	List<ProduktModel> produktList = new ArrayList<ProduktModel>();
    	try {
			produktList = produktRepository.getAll();
		} catch (DataSqlException e) {
			
			UiUtils.showError("Produkt-Liste extrahieren ...", "Fehler bei Produkt-Liste extrahieren: " + e.getLocalizedMessage());
		}
    	
    	produktMap = produktList.stream().collect(Collectors.toMap(p -> p.getProdukt(), p -> p));
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
        

        LocalDateTime ldt = LocalDateTime.now();

        String date = dateFormatter.format(ldt);
        String time = timeFormatter.format(ldt);

        int lastNummer = 0;
        try {
            lastNummer = quittungRepository.getLastQuittungNummer(date);
        } catch (DataSqlException e) {
            UiUtils.showError("Extrahieren der letzten Quittung-Nummer ...", e.getMessage());
        }

        savingModel = new QuittungModel(0, lastNummer + 1, date, time);

        quittungNummer.set(String.valueOf(lastNummer + 1));
        quittungDatum.set(date);
        liferDatum.set(date);
        
        isDirty = false;
	}

	public int getCurrentQuittungNummer() {
		return savingModel.getNummer();
	}
	
	
	
    public StringProperty getQuittungNummerProperty() {
		return quittungNummer;
	}

	public StringProperty getQuittungDatumProperty() {
		return quittungDatum;
	}

	public StringProperty getLiferDatumProperty() {
		return liferDatum;
	}

	public boolean isDirty() {
		return isDirty;
	}

	
	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
		
		if(isDirty) {
			if(quittungItems.stream().anyMatch(pi -> pi.isValid())) {
				this.disableSave.set(false);
			}
		}
		
		
		this.disablePrint.set(isDirty);
	}
	
	

	public BooleanProperty getDisableSaveProperty() {
		return disableSave;
	}

	public BooleanProperty getDisablePrintProperty() {
		return disablePrint;
	}

	public boolean save() {
        if(savingModel.getId() == 0){

            savingModel.getItems().addAll(quittungItems.stream().filter(qi -> qi.isValid()).map(qi -> qi.toModel()).collect(Collectors.toList()));
            try {
            	Optional<QuittungModel> savedModelOptional = quittungRepository.create(savingModel);
            	if(savedModelOptional.isPresent()) {
            		savingModel = savedModelOptional.get();
            		List<QuittungItemProperty> items = quittungItems.stream().filter(qi -> qi.isValid()).collect(Collectors.toList());
            		
            		for(QuittungItemProperty item: items) {
            			ProduktModel produktModel = new ProduktModel(item.getProdukt(), item.getBrutoPreis());
            			produktRepository.updateOrCreate(produktModel);
            			
            		}
            		
            	}

            } catch (DataSqlException e) {
                UiUtils.showError("Speichern der Quittung ...", e.getMessage());
            }

            setDirty(false);

        }

        return true;
    }
	
	public void setNewMengeValue(int row, Integer value) {
		QuittungItemProperty prop = quittungItems.get(row);
        if(prop.getMenge() != value) {
        	prop.setMenge(value);

            calculateQuittungSumme();
            setDirty(true);
        }
	}

	public void setNewBrutoPreisValue(int row, Float value) {
		
		QuittungItemProperty prop = quittungItems.get(row);
        
        if(prop.getBrutoPreis() != value) {
        	
        	prop.setBrutoPreis(value);
            prop.setPreis(calculateNettoPreis(value));
            
            calculateQuittungSumme();
            setDirty(true);
        }
		
	}

	public void setNewProduktValue(int row, String value) {
		
		QuittungItemProperty prop = quittungItems.get(row);
        if(prop.getProdukt() != value) {
            prop.setProdukt(value);
            calculateQuittungSumme();
            setDirty(true);

            Optional<String> foundProdukt = produktMap.keySet().stream().filter(k -> k.toLowerCase().equals(value.toLowerCase())).findAny();
            
            if(foundProdukt.isPresent()) {
            	
            	ProduktModel produktModel = produktMap.get(foundProdukt.get());
            	prop.setBrutoPreis(produktModel.getLastPreis());
            }
        }
		
	}

	public void setNewArtikelNummerValue(int row, String value) {
		
		QuittungItemProperty prop = quittungItems.get(row);
        if(prop.getArtikelNummer() != value) {
            prop.setArtikelNummer(value);
            calculateQuittungSumme();
            setDirty(true);

        }
		
	}
}
