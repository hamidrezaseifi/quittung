package de.seifi.rechnung_manager.fx_services;


import de.seifi.rechnung_manager.entities.ProduktEntity;
import de.seifi.rechnung_manager.entities.RechnungEntity;
import de.seifi.rechnung_manager.models.ProduktModel;
import de.seifi.rechnung_manager.models.RechnungModel;
import de.seifi.rechnung_manager.repositories.ProduktRepository;
import de.seifi.rechnung_manager.repositories.RechnungRepository;
import de.seifi.rechnung_manager.ui.RechnungItemProperty;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;

public class RechnungBindingService {
	
	public static RechnungBindingService CURRENT_INSTANCE = null;
	
    private int INITIAL_ITEMS = 10;
    
    private final ProduktRepository produktRepository;

    private final RechnungRepository rechnungRepository;
    
	    
    private Map<String, ProduktModel> produktMap;
    private List<ProduktModel> produktList;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyy");

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private ObservableList<RechnungItemProperty> rechnungItems;

    private FloatProperty gesamtSumme;
    private FloatProperty nettoSumme;
    private FloatProperty mvstSumme;
    
    private StringProperty rechnungNummer;
    private StringProperty rechnungDatum;
    private StringProperty liferDatum;
    
    private BooleanProperty disableSave;
    private BooleanProperty disablePrint;

    private int activeBerechnenZiel;

    private float berechnenFaktorBasis = 1.4f;

    private List<Float> berechnenFaktorZielList = Arrays.asList(1.4f, 1.2f);

    private List<String> berechnenFaktorZielColorList = Arrays.asList("-fx-background-color: white", "-fx-background-color: #f7fbff");
    private StringProperty bannerBackColor;
    
    private boolean cancelEditing;
    

    
    private RechnungModel savingModel = new RechnungModel();
        
    private boolean isDirty;
    
    public RechnungBindingService(ProduktRepository produktRepository,
    		final RechnungRepository rechnungRepository) {
    	
    	CURRENT_INSTANCE = this;
        this.activeBerechnenZiel = 0;
        this.cancelEditing = false;
        this.bannerBackColor = new SimpleStringProperty(this.berechnenFaktorZielColorList.get(this.activeBerechnenZiel));

    	this.produktRepository = produktRepository;
    	this.rechnungRepository = rechnungRepository;
    	
        gesamtSumme = new SimpleFloatProperty(0);
        nettoSumme = new SimpleFloatProperty(0);
        mvstSumme = new SimpleFloatProperty(0);
        
        rechnungNummer = new SimpleStringProperty();
        rechnungDatum = new SimpleStringProperty();
        liferDatum = new SimpleStringProperty();
        
        disableSave = new SimpleBooleanProperty(true);
        disablePrint = new SimpleBooleanProperty(false);
        
        rechnungItems = FXCollections.observableArrayList();
        
        retreiveProduktMap();
        
        reset();

    }
    
    public boolean isCancelEditing() {
    	return this.cancelEditing;
    }

    public void doCancelEditing() {
    	this.cancelEditing = true;
    }
    
    public void cancelCancelEditing() {
    	this.cancelEditing = false;
    }

    public void toggleActiveBerechnenZiel(){
        this.activeBerechnenZiel = this.activeBerechnenZiel == 0 ? 1 : 0;
        this.bannerBackColor.set(this.berechnenFaktorZielColorList.get(this.activeBerechnenZiel));

    }

    public String getBannerBackColor() {
        return bannerBackColor.get();
    }

    public StringProperty bannerBackColorProperty() {
        return bannerBackColor;
    }
    
    public void calculateRechnungSumme() {
        float netto = 0;

        for(RechnungItemProperty i:rechnungItems){
            netto += i.getGesamt();
        }
        
        nettoSumme.set(netto);
        mvstSumme.set(netto * 19 / 100);
        gesamtSumme.set(nettoSumme.getValue() + mvstSumme.getValue());
    }

    public ObservableList<RechnungItemProperty> getRechnungItems() {
        return rechnungItems;
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
		netto = (netto * getBerechnenFaktorZiel()) / berechnenFaktorBasis;
		
		return netto;
	}

	public float getBerechnenFaktorBasis() {
		return berechnenFaktorBasis;
	}

	public void setBerechnenFaktorBasis(float berechnenFaktorBasis) {
		this.berechnenFaktorBasis = berechnenFaktorBasis;
	}

	public float getBerechnenFaktorZiel() {
        return this.berechnenFaktorZielList.get(this.activeBerechnenZiel);
	}

	public void reset() {
		rechnungItems.clear();
        while (rechnungItems.size() < INITIAL_ITEMS){
            rechnungItems.add(new RechnungItemProperty());
        }
        calculateRechnungSumme();
        

        LocalDateTime ldt = LocalDateTime.now();

        String date = dateFormatter.format(ldt);
        String time = timeFormatter.format(ldt);

        int lastNummer = 0;
        
        Optional<RechnungEntity> lasstNummerRechnungOptional = rechnungRepository.findTopByOrderByNummerDesc();
        if(lasstNummerRechnungOptional.isPresent()) {
        	lastNummer = lasstNummerRechnungOptional.get().getNummer();
        }

        savingModel = new RechnungModel(lastNummer + 1, date, time);

        rechnungNummer.set(String.valueOf(lastNummer + 1));
        rechnungDatum.set(date);
        liferDatum.set(date);
        
        isDirty = false;
	}

	public int getCurrentRechnungNummer() {
		return savingModel.getNummer();
	}

    public RechnungModel getSavingModel() {
        return savingModel;
    }

    public StringProperty getRechnungNummerProperty() {
		return rechnungNummer;
	}

	public StringProperty getRechnungDatumProperty() {
		return rechnungDatum;
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
			if(rechnungItems.stream().anyMatch(pi -> pi.isValid())) {
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
            savingModel.getItems().addAll(rechnungItems.stream().filter(qi -> qi.isValid()).map(qi -> qi.toModel()).collect(Collectors.toList()));

        	RechnungEntity savingEntity = savingModel.toEntity();
        	
        	rechnungRepository.save(savingEntity);
        	Optional<RechnungEntity> savedEntityOptional = rechnungRepository.findById(savingEntity.getId());
        	if(savedEntityOptional.isPresent()) {
        		savingModel = savedEntityOptional.get().toModel();
        		List<RechnungItemProperty> items = rechnungItems.stream().filter(qi -> qi.isValid()).collect(Collectors.toList());
        		
        		for(RechnungItemProperty item: items) {
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
        		retreiveProduktMap();
        	}

            
            setDirty(false);

        }

        return true;
    }

    private void retreiveProduktMap() {
    	List<ProduktEntity> entityList = produktRepository.findAll(Sort.by(Sort.Direction.ASC, "produktName"));
    	produktList = entityList.stream().map(e -> e.toModel()).collect(Collectors.toList());
    	
    	produktMap = produktList.stream().collect(Collectors.toMap(p -> p.getProduktName(), p -> p));
    }

	public void setNewMengeValue(int row, Integer value) {
		RechnungItemProperty prop = rechnungItems.get(row);
        if(prop.getMenge() != value) {
        	prop.setMenge(value);

            calculateRechnungSumme();
            setDirty(true);
        }
	}

	public void setNewBrutoPreisValue(int row, Float value) {
		
		RechnungItemProperty prop = rechnungItems.get(row);
        
        if(prop.getBrutoPreis() != value) {
        	
        	prop.setBrutoPreis(value);
            prop.setPreis(calculateNettoPreis(value));
            
            calculateRechnungSumme();
            setDirty(true);
        }
		
	}

	public void setNewProduktValue(int row, String value) {
		
		RechnungItemProperty prop = rechnungItems.get(row);
        if(prop.getProdukt() != value) {
            prop.setProdukt(value);
            calculateRechnungSumme();
            setDirty(true);

            Optional<String> foundProdukt = produktMap.keySet().stream().filter(k -> k.toLowerCase().equals(value.toLowerCase())).findAny();
            
            if(foundProdukt.isPresent()) {
            	
            	ProduktModel produktModel = produktMap.get(foundProdukt.get());
            	prop.setBrutoPreis(produktModel.getLastPreis());
            }
        }
		
	}

	public void setNewArtikelNummerValue(int row, String value) {
		
		RechnungItemProperty prop = rechnungItems.get(row);
        if(prop.getArtikelNummer() != value) {
            prop.setArtikelNummer(value);
            calculateRechnungSumme();
            setDirty(true);

        }
		
	}

	public List<ProduktModel> getProduktList() {
		return produktList;
	}

	public void addNewRow() {
		rechnungItems.add(new RechnungItemProperty());
	}
	
	
}
