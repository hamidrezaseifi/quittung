package de.seifi.rechnung_manager.fx_services;


import de.seifi.rechnung_manager.entities.ProduktEntity;
import de.seifi.rechnung_manager.entities.RechnungEntity;
import de.seifi.rechnung_manager.models.ProduktModel;
import de.seifi.rechnung_manager.models.RechnungModel;
import de.seifi.rechnung_manager.repositories.ProduktRepository;
import de.seifi.rechnung_manager.repositories.RechnungRepository;
import de.seifi.rechnung_manager.ui.QuittungItemProperty;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;

public class QuittungBindingService {
    private int INITIAL_ITEMS = 10;
    
    private final ProduktRepository produktRepository;
    
    private final RechnungRepository rechnungRepository;
    
	    
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
    
    private RechnungModel savingModel = new RechnungModel();
        
    private boolean isDirty;
    
    public QuittungBindingService(float berechnenFaktorBasis, 
    		float berechnenFaktorZiel, 
    		ProduktRepository produktRepository,
    		final RechnungRepository rechnungRepository) {
    	this.berechnenFaktorBasis = berechnenFaktorBasis;
    	this.berechnenFaktorZiel = berechnenFaktorZiel;
    	this.produktRepository = produktRepository;
    	this.rechnungRepository = rechnungRepository;
    	
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
    	List<ProduktEntity> entityList = produktRepository.findAll(Sort.by(Sort.Direction.ASC, "produktName"));
    	List<ProduktModel> produktList = entityList.stream().map(e -> e.toModel()).collect(Collectors.toList());
    	
    	produktMap = produktList.stream().collect(Collectors.toMap(p -> p.getProduktName(), p -> p));
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
        
        Optional<RechnungEntity> lasstNummerRechnungOptional = rechnungRepository.findTopByOrderByNummerDesc();
        if(lasstNummerRechnungOptional.isPresent()) {
        	lastNummer = lasstNummerRechnungOptional.get().getNummer();
        }
        
        /*try {
            lastNummer = quittungRepository.getLastQuittungNummer(date);
        } catch (DataSqlException e) {
            UiUtils.showError("Extrahieren der letzten Quittung-Nummer ...", e.getMessage());
        }*/

        savingModel = new RechnungModel(lastNummer + 1, date, time);

        quittungNummer.set(String.valueOf(lastNummer + 1));
        quittungDatum.set(date);
        liferDatum.set(date);
        
        isDirty = false;
	}

	public int getCurrentQuittungNummer() {
		return savingModel.getNummer();
	}

    public RechnungModel getSavingModel() {
        return savingModel;
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
        if(savingModel.isNew()){
        	savingModel.getItems().clear();
            savingModel.getItems().addAll(quittungItems.stream().filter(qi -> qi.isValid()).map(qi -> qi.toModel()).collect(Collectors.toList()));

        	RechnungEntity savingEntity = savingModel.toEntity();
        	
        	rechnungRepository.save(savingEntity);
        	Optional<RechnungEntity> savedEntityOptional = rechnungRepository.findById(savingEntity.getId());
        	if(savedEntityOptional.isPresent()) {
        		savingModel = savedEntityOptional.get().toModel();
        		List<QuittungItemProperty> items = quittungItems.stream().filter(qi -> qi.isValid()).collect(Collectors.toList());
        		
        		for(QuittungItemProperty item: items) {
        			Optional<String> foundProdukt = produktMap.keySet().stream().filter(k -> k.toLowerCase().equals(item.getProdukt().toLowerCase())).findAny();
        			ProduktEntity produktEntity = null;
        			if(foundProdukt.isPresent()) {
        				produktEntity = produktMap.get(foundProdukt.get()).toEntity();
        				produktEntity.setLastPreis(item.getBrutoPreis());
        			}
        			else {
        				produktEntity = new ProduktEntity(item.getProdukt(), item.getBrutoPreis());
        			}
        			produktRepository.save(produktEntity);
        			
        		}
        		
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
