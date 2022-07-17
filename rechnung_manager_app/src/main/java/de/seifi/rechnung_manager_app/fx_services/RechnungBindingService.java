package de.seifi.rechnung_manager_app.fx_services;


import de.seifi.rechnung_manager_app.entities.ProduktEntity;
import de.seifi.rechnung_manager_app.entities.RechnungEntity;
import de.seifi.rechnung_manager_app.models.ProduktModel;
import de.seifi.rechnung_manager_app.models.RechnungModel;
import de.seifi.rechnung_manager_app.repositories.ProduktRepository;
import de.seifi.rechnung_manager_app.repositories.RechnungRepository;
import de.seifi.rechnung_manager_app.utils.GeneralUtils;
import de.seifi.rechnung_manager_app.utils.GerldCalculator;
import de.seifi.rechnung_manager_app.models.RechnungItemProperty;
import de.seifi.rechnung_manager_app.utils.ProduktUtils;
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

    private final RechnungRepository rechnungRepository;

    //private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyy");

    //private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

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
    

    
    private RechnungModel savingModel = new RechnungModel();
        
    private boolean isDirty;
    private boolean isView = false;

    public RechnungBindingService(final RechnungRepository rechnungRepository) {
    	
    	CURRENT_INSTANCE = this;
        this.activeBerechnenZiel = 0;
        this.bannerBackColor = new SimpleStringProperty(this.berechnenFaktorZielColorList.get(this.activeBerechnenZiel));

    	this.rechnungRepository = rechnungRepository;
    	
        gesamtSumme = new SimpleFloatProperty(0);
        nettoSumme = new SimpleFloatProperty(0);
        mvstSumme = new SimpleFloatProperty(0);
        
        rechnungNummer = new SimpleStringProperty();
        rechnungDatum = new SimpleStringProperty();
        liferDatum = new SimpleStringProperty();
        
        disableSave = new SimpleBooleanProperty(true);
        disablePrint = new SimpleBooleanProperty(false);
        
        this.rechnungItems = FXCollections.observableArrayList();

        reset();

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

        for(RechnungItemProperty i:this.rechnungItems){
            netto += i.getGesamt();
        }
        
        nettoSumme.set(netto);
        mvstSumme.set(netto * 19 / 100);
        gesamtSumme.set(nettoSumme.getValue() + mvstSumme.getValue());
    }

    public ObservableList<RechnungItemProperty> getRechnungItems() {
        return this.rechnungItems;
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
		
		float netto = GerldCalculator.bruttoToNetto(value);
		
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
		this.rechnungItems.clear();
        while (this.rechnungItems.size() < INITIAL_ITEMS){
        	addNewRowIntern(new RechnungItemProperty());
        }
        calculateRechnungSumme();
        

        LocalDateTime ldt = LocalDateTime.now();

        String date = GeneralUtils.formatDate(ldt);
        String time = GeneralUtils.formatTime(ldt);

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
			if(this.rechnungItems.stream().anyMatch(pi -> !pi.isValid())) {
				this.disableSave.set(true);
			}
			else {
				this.disableSave.set(!isAnyValidArtikelToSave());
			}
		}
		
		this.disablePrint.set(isDirty);
	}
	
	private boolean isAnyValidArtikelToSave() {
		return this.rechnungItems.stream().anyMatch(pi -> pi.isValid() & !pi.isEmpty());
	}

	public BooleanProperty getDisableSaveProperty() {
		return disableSave;
	}

	public BooleanProperty getDisablePrintProperty() {
		return disablePrint;
	}

	public boolean save() {

        savingModel.getItems().clear();
        savingModel.getItems().addAll(this.rechnungItems.stream().filter(qi -> qi.canSaved()).map(qi -> qi.toModel()).collect(Collectors.toList()));

        RechnungEntity savingEntity = savingModel.toEntity();

        rechnungRepository.save(savingEntity);
        Optional<RechnungEntity> savedEntityOptional = rechnungRepository.findById(savingEntity.getId());
        if(savedEntityOptional.isPresent()) {
            savingModel = savedEntityOptional.get().toModel();
            List<RechnungItemProperty> items = this.rechnungItems.stream().filter(qi -> qi.canSaved()).collect(Collectors.toList());

            for(RechnungItemProperty item: items) {
                ProduktUtils.add(item.getProdukt(), item.getBrutoPreis());

            }
            ProduktUtils.retreiveProduktList();
        }


        setDirty(false);



        return true;
    }

	public void setNewMengeValue(int row, Integer value) {
		RechnungItemProperty prop = this.rechnungItems.get(row);
        if(prop.getMenge() != value) {
        	prop.setMenge(value);

            calculateRechnungSumme();
            setDirty(true);

        }
	}

	public void setNewBrutoPreisValue(int row, Float value) {
		
		RechnungItemProperty prop = this.rechnungItems.get(row);
        
        if(prop.getBrutoPreis() != value) {
        	
        	prop.setBrutoPreis(value);
            prop.setPreis(calculateNettoPreis(value));
            
            calculateRechnungSumme();
            setDirty(true);
 
        }
		
	}

	public void setNewProduktValue(int row, String value) {
		
		RechnungItemProperty prop = this.rechnungItems.get(row);
        if(prop.getProdukt() != value) {
            prop.setProdukt(value);
            calculateRechnungSumme();
            setDirty(true);

            Optional<String> foundProdukt = ProduktUtils.getProduktMap().keySet().stream().filter(k -> k.toLowerCase().equals(value.toLowerCase())).findAny();
            
            if(foundProdukt.isPresent()) {
            	
            	ProduktModel produktModel = ProduktUtils.getProduktMap().get(foundProdukt.get());
            	prop.setBrutoPreis(produktModel.getLastPreis());
            }

        }
		
	}

	public void setNewArtikelNummerValue(int row, String value) {
		
		RechnungItemProperty prop = this.rechnungItems.get(row);
        if(prop.getArtikelNummer() != value) {
            prop.setArtikelNummer(value);
            calculateRechnungSumme();
            setDirty(true);

        }
		
	}

	private void addNewRowIntern(RechnungItemProperty item) {
		
		this.rechnungItems.add(item);
	}

	public void addNewRow() {
		addNewRowIntern(new RechnungItemProperty());
	}

	public void setRechnungModel(RechnungModel rechnungModel) {
		savingModel = rechnungModel;
		rechnungItems.clear();
		
		rechnungModel.getItems().forEach(r -> addNewRowIntern(new RechnungItemProperty(r)));

        ProduktUtils.retreiveProduktList();
		
		isDirty = false;
	}


    public void setIsView(boolean isView) {
        this.isView = isView;
    }

    public boolean isView() {
        return isView;
    }
}
