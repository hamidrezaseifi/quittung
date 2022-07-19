package de.seifi.rechnung_manager_app.fx_services;


import de.seifi.rechnung_manager_app.data_service.IRechnungDataHelper;
import de.seifi.rechnung_manager_app.entities.QuittungEntity;
import de.seifi.rechnung_manager_app.models.ProduktModel;
import de.seifi.rechnung_manager_app.models.QuittungItemProperty;
import de.seifi.rechnung_manager_app.models.QuittungModel;
import de.seifi.rechnung_manager_app.repositories.QuittungRepository;
import de.seifi.rechnung_manager_app.utils.GeneralUtils;
import de.seifi.rechnung_manager_app.utils.GerldCalculator;
import de.seifi.rechnung_manager_app.utils.ProduktUtils;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class QuittungBindingService {

	public static QuittungBindingService CURRENT_INSTANCE = null;

    private int INITIAL_ITEMS = 10;

    private final QuittungRepository quittungRepository;

    private final IRechnungDataHelper rechnungDataHelper;

    private ObservableList<QuittungItemProperty> quittungItems;

    private FloatProperty gesamtSumme;
    private FloatProperty nettoSumme;
    private FloatProperty mvstSumme;

    private StringProperty quittungNummer;
    private StringProperty quittungDatum;
    private StringProperty liferDatum;

    private BooleanProperty disableSave;
    private BooleanProperty disablePrint;

    private int activeBerechnenZiel;

    private float berechnenFaktorBasis = 1.4f;

    private List<Float> berechnenFaktorZielList = Arrays.asList(1.4f, 1.2f);

    private List<String> berechnenFaktorZielColorList = Arrays.asList("-fx-background-color: white", "-fx-background-color: #f7fbff");
    private StringProperty bannerBackColor;



    private QuittungModel savingModel = new QuittungModel();

    private boolean isDirty;
    private boolean isView = false;

    public QuittungBindingService(final QuittungRepository quittungRepository, IRechnungDataHelper rechnungDataHelper) {
    	
    	CURRENT_INSTANCE = this;
        this.activeBerechnenZiel = 0;
        this.bannerBackColor = new SimpleStringProperty(this.berechnenFaktorZielColorList.get(this.activeBerechnenZiel));

        this.quittungRepository = quittungRepository;
        this.rechnungDataHelper = rechnungDataHelper;

        gesamtSumme = new SimpleFloatProperty(0);
        nettoSumme = new SimpleFloatProperty(0);
        mvstSumme = new SimpleFloatProperty(0);

        quittungNummer = new SimpleStringProperty();
        quittungDatum = new SimpleStringProperty();
        liferDatum = new SimpleStringProperty();
        
        disableSave = new SimpleBooleanProperty(true);
        disablePrint = new SimpleBooleanProperty(false);
        
        this.quittungItems = FXCollections.observableArrayList();

        ProduktUtils.retreiveProduktList();

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
    
    public void calculateQuittungSumme() {
        float netto = 0;

        for(QuittungItemProperty i:this.quittungItems){
            netto += i.getGesamt();
        }
        
        nettoSumme.set(netto);
        mvstSumme.set(netto * 19 / 100);
        gesamtSumme.set(nettoSumme.getValue() + mvstSumme.getValue());
    }

    public ObservableList<QuittungItemProperty> getQuittungItems() {
        return this.quittungItems;
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
		this.quittungItems.clear();
        while (this.quittungItems.size() < INITIAL_ITEMS){
        	addNewRowIntern(new QuittungItemProperty());
        }
        calculateQuittungSumme();
        

        LocalDateTime ldt = LocalDateTime.now();

        String date = GeneralUtils.formatDate(ldt);
        String time = GeneralUtils.formatTime(ldt);

        int lastNummer = this.rechnungDataHelper.getLastActiveRechnungNummer();

        savingModel = new QuittungModel(lastNummer + 1, date, time);

        quittungNummer.set(String.valueOf(lastNummer + 1));
        quittungDatum.set(date);
        liferDatum.set(date);
        
        isDirty = false;
	}

	public int getCurrentQuittungNummer() {
		return savingModel.getNummer();
	}

    public QuittungModel getSavingModel() {
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
			if(this.quittungItems.stream().anyMatch(pi -> !pi.isValid())) {
				this.disableSave.set(true);
			}
			else {
				this.disableSave.set(!isAnyValidArtikelToSave());
			}
		}
		
		this.disablePrint.set(isDirty);
	}
	
	private boolean isAnyValidArtikelToSave() {
		return this.quittungItems.stream().anyMatch(pi -> pi.isValid() & !pi.isEmpty());
	}

	public BooleanProperty getDisableSaveProperty() {
		return disableSave;
	}

	public BooleanProperty getDisablePrintProperty() {
		return disablePrint;
	}

	public boolean save() {

        savingModel.getItems().clear();
        savingModel.getItems().addAll(this.quittungItems
                                              .stream().filter(qi -> qi.canSaved()).map(qi -> qi.toModel()).collect(Collectors.toList()));

        QuittungEntity savingEntity = savingModel.toEntity();

        quittungRepository.save(savingEntity);
        Optional<QuittungEntity> savedEntityOptional = quittungRepository.findById(savingEntity.getId());
        if(savedEntityOptional.isPresent()) {
            savingModel = savedEntityOptional.get().toModel();
            List<QuittungItemProperty> items = this.quittungItems
                    .stream().filter(qi -> qi.canSaved()).collect(Collectors.toList());

            for(QuittungItemProperty item: items) {
                ProduktUtils.add(item.getProdukt(), item.getBrutoPreis());

            }
            ProduktUtils.retreiveProduktList();
        }


        setDirty(false);



        return true;
    }

	public void setNewMengeValue(int row, Integer value) {
		QuittungItemProperty prop = this.quittungItems.get(row);
        if(prop.getMenge() != value) {
        	prop.setMenge(value);

            calculateQuittungSumme();
            setDirty(true);

        }
	}

	public void setNewBrutoPreisValue(int row, Float value) {
		
		QuittungItemProperty prop = this.quittungItems.get(row);
        
        if(prop.getBrutoPreis() != value) {
        	
        	prop.setBrutoPreis(value);
            prop.setPreis(calculateNettoPreis(value));
            
            calculateQuittungSumme();
            setDirty(true);
 
        }
		
	}

	public void setNewProduktValue(int row, String value) {
		
		QuittungItemProperty prop = this.quittungItems.get(row);
        if(prop.getProdukt() != value) {
            prop.setProdukt(value);
            calculateQuittungSumme();
            setDirty(true);

            Optional<String> foundProdukt = ProduktUtils.getProduktMap().keySet().stream().filter(k -> k.toLowerCase().equals(value.toLowerCase())).findAny();
            
            if(foundProdukt.isPresent()) {
            	
            	ProduktModel produktModel = ProduktUtils.getProduktMap().get(foundProdukt.get());
            	prop.setBrutoPreis(produktModel.getLastPreis());
            }

        }
		
	}

	public void setNewArtikelNummerValue(int row, String value) {
		
		QuittungItemProperty prop = this.quittungItems.get(row);
        if(prop.getArtikelNummer() != value) {
            prop.setArtikelNummer(value);
            calculateQuittungSumme();
            setDirty(true);

        }
		
	}

	private void addNewRowIntern(QuittungItemProperty item) {
		
		this.quittungItems.add(item);
	}

	public void addNewRow() {
		addNewRowIntern(new QuittungItemProperty());
	}

	public void setQuittungModel(QuittungModel quittungModel) {
		savingModel = quittungModel;
		quittungItems.clear();
		
		quittungModel.getItems().forEach(r -> addNewRowIntern(new QuittungItemProperty(r)));

		isDirty = false;
	}


    public void setIsView(boolean isView) {
        this.isView = isView;
    }

    public boolean isView() {
        return isView;
    }
}
