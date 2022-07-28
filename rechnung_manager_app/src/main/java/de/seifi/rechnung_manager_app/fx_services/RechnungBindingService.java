package de.seifi.rechnung_manager_app.fx_services;


import de.seifi.rechnung_common.entities.CustomerEntity;
import de.seifi.rechnung_common.entities.RechnungEntity;
import de.seifi.rechnung_common.repositories.RechnungRepository;
import de.seifi.rechnung_manager_app.adapter.CustomerAdapter;
import de.seifi.rechnung_manager_app.adapter.RechnungAdapter;
import de.seifi.rechnung_manager_app.data_service.IRechnungDataHelper;
import de.seifi.rechnung_manager_app.enums.RechnungStatus;
import de.seifi.rechnung_manager_app.enums.RechnungType;
import de.seifi.rechnung_manager_app.models.CustomerModel;
import de.seifi.rechnung_manager_app.models.CustomerModelProperty;
import de.seifi.rechnung_manager_app.models.CustomerSelectModel;
import de.seifi.rechnung_manager_app.models.ProduktModel;
import de.seifi.rechnung_manager_app.models.RechnungModel;
import de.seifi.rechnung_manager_app.utils.CustomerHelper;
import de.seifi.rechnung_manager_app.utils.GeneralUtils;
import de.seifi.rechnung_manager_app.utils.GerldCalculator;
import de.seifi.rechnung_manager_app.models.RechnungItemProperty;
import de.seifi.rechnung_manager_app.utils.ProduktHelper;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class RechnungBindingService {
	
	public static RechnungBindingService CURRENT_INSTANCE = null;
	
    private int INITIAL_ITEMS = 10;

    private final RechnungRepository rechnungRepository;
    private final IRechnungDataHelper rechnungDataHelper;
    //private final CustomerRepository customerRepository;
    
    private final Map<UUID, CustomerModel> customerList;

    private ObservableList<RechnungItemProperty> rechnungItems;   
    private CustomerModelProperty customerModelProperty;

    private FloatProperty gesamtSumme;
    private FloatProperty nettoSumme;
    private FloatProperty mvstSumme;
    
    private StringProperty rechnungNummer;
    private StringProperty rechnungDatum;
    private StringProperty liferDatum;
    
    private BooleanProperty disableSave;
    private BooleanProperty disablePrint;
    private BooleanProperty visbleToggleStatusBox;

    private RechnungModel rechnungSavingModel = new RechnungModel();

    private CustomerModel customerSavingModel = new CustomerModel();

    private boolean isDirty;
    private boolean isView = false;

    private final RechnungType rechnungType;


    private int activeBerechnenZiel;

    private float berechnenFaktorBasis = 1.4f;

    private List<Float> berechnenFaktorZielList = Arrays.asList(1.4f, 1.2f);

    private List<String> berechnenFaktorZielColorList = Arrays.asList("-fx-background-color: white", "-fx-background-color: #f2f6ff");
    
    private StringProperty bannerBackColor;
    
    private final CustomerAdapter customerAdapter = new CustomerAdapter();

    private final RechnungAdapter rechnungAdapter = new RechnungAdapter();


    public RechnungBindingService(final RechnungType rechnungType, 
    							  final RechnungRepository rechnungRepository,
                                  final IRechnungDataHelper rechnungDataHelper) {
    	
    	CURRENT_INSTANCE = this;

        this.activeBerechnenZiel = 0;
        this.bannerBackColor = new SimpleStringProperty(this.berechnenFaktorZielColorList.get(this.activeBerechnenZiel));

        this.rechnungType = rechnungType;
        this.rechnungRepository = rechnungRepository;
        this.rechnungDataHelper = rechnungDataHelper;

        this.customerList = new HashMap<>();

        gesamtSumme = new SimpleFloatProperty(0);
        nettoSumme = new SimpleFloatProperty(0);
        mvstSumme = new SimpleFloatProperty(0);
        
        rechnungNummer = new SimpleStringProperty();
        rechnungDatum = new SimpleStringProperty();
        liferDatum = new SimpleStringProperty();
        
        disableSave = new SimpleBooleanProperty(true);
        disablePrint = new SimpleBooleanProperty(false);
        visbleToggleStatusBox = new SimpleBooleanProperty(rechnungType == RechnungType.QUITTUNG);
        
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

    public float getBerechnenFaktorZiel() {
        return this.berechnenFaktorZielList.get(this.activeBerechnenZiel);
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

	public void reset() {
		this.rechnungItems.clear();
        while (this.rechnungItems.size() < INITIAL_ITEMS){
        	addNewRowIntern(new RechnungItemProperty());
        }
        calculateRechnungSumme();
        
        customerModelProperty = new CustomerModelProperty();
        

        LocalDateTime ldt = LocalDateTime.now();

        String date = GeneralUtils.formatDate(ldt);

        int lastNummer = this.rechnungDataHelper.getLastActiveRechnungNummer();

        rechnungSavingModel = new RechnungModel(lastNummer + 1, date, date, rechnungType, RechnungStatus.ACTIVE);
        
        rechnungNummer.set(String.valueOf(lastNummer + 1));
        rechnungDatum.set(date);
        liferDatum.set(date);
        
        setDirty(false);
        this.visbleToggleStatusBox.set(true);
	}

    public List<CustomerSelectModel> getCustomerSelectList() {

        this.customerList.clear();
        List<CustomerModel> modelList = CustomerHelper.getCustomerList();
        for(CustomerModel model: modelList) {
            this.customerList.put(model.getId(), model);
        }

        List<CustomerSelectModel> selectList = modelList.stream().map(c -> new CustomerSelectModel(c.getId(), c.getCustomerName())).collect(Collectors.toList());

        return selectList;
    }

    public CustomerModelProperty getCustomerModelProperty() {
		return customerModelProperty;
	}

	public int getCurrentRechnungNummer() {
		return rechnungSavingModel.getNummer();
	}

    public RechnungModel getRechnungSavingModel() {
        return rechnungSavingModel;
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
		if(this.visbleToggleStatusBox.get()) {
			this.visbleToggleStatusBox.set(this.disableSave.get());
		}
		
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

	public BooleanProperty getVisbleToggleStatusBox() {
		return visbleToggleStatusBox;
	}

	public void setVisbleToggleStatusBox(boolean visible) {
		visbleToggleStatusBox.set(visible);;
	}

	public boolean save() {
        if(this.rechnungType == RechnungType.RECHNUNG){
            CustomerEntity customerEntity = customerAdapter.toEntity(customerSavingModel);
            if(customerEntity.isNew()){
                CustomerHelper.save(customerEntity);
                customerSavingModel = customerAdapter.toModel(customerEntity);
            }

            rechnungSavingModel.setCustomerId(customerEntity.getId());
        }
        if(this.rechnungType == RechnungType.QUITTUNG){

            rechnungSavingModel.setCustomerId(RechnungModel.QUITTUNG_CUSTOMER_ID);
        }

        rechnungSavingModel.getItems().clear();
        rechnungSavingModel
                .getItems().addAll(this.rechnungItems.stream().filter(qi -> qi.canSaved()).map(qi -> qi.toModel()).collect(Collectors.toList()));

        RechnungEntity savingEntity = rechnungAdapter.toEntity(rechnungSavingModel);

        rechnungRepository.save(savingEntity);
        Optional<RechnungEntity> savedEntityOptional = rechnungRepository.findById(savingEntity.getId());
        if(savedEntityOptional.isPresent()) {
            rechnungSavingModel = rechnungAdapter.toModel(savedEntityOptional.get());
            List<RechnungItemProperty> items = this.rechnungItems.stream().filter(qi -> qi.canSaved()).collect(Collectors.toList());

            for(RechnungItemProperty item: items) {
                ProduktHelper.add(item.getProdukt(), item.getBrutoPreis());

            }
            ProduktHelper.retreiveProduktList();
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

            Optional<String> foundProdukt = ProduktHelper
                    .getProduktMap().keySet().stream().filter(k -> k.toLowerCase().equals(value.toLowerCase())).findAny();
            
            if(foundProdukt.isPresent()) {
            	
            	ProduktModel produktModel = ProduktHelper.getProduktMap().get(foundProdukt.get());
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
		
		if(rechnungType == RechnungType.RECHNUNG) {
	        Optional<CustomerEntity> customerEntityOptional = CustomerHelper.getById(rechnungModel.getCustomerId());
	        if(customerEntityOptional.isEmpty()){
	        	throw new RuntimeException("Der Kunde von der Rechnung nicht gefunden!");
	        }

	        this.customerSavingModel = customerAdapter.toModel(customerEntityOptional.get());
			
		}
        
        rechnungSavingModel = rechnungModel;
		rechnungItems.clear();

        this.rechnungSavingModel = rechnungModel;
        
        this.customerModelProperty.setModel(this.customerSavingModel);
		
		rechnungModel.getItems().forEach(r -> addNewRowIntern(new RechnungItemProperty(r)));

        ProduktHelper.retreiveProduktList();
		
		isDirty = false;
	}


    public void setIsView(boolean isView) {
        this.isView = isView;
    }

    public boolean isView() {
        return isView;
    }

    public void setCurrentCustomer(UUID id) {
        this.customerSavingModel = this.customerList.get(id);
        this.customerModelProperty.setModel(this.customerSavingModel);

    }

    public void setCustomerModel(CustomerModel customerModel) {
        this.customerSavingModel = customerModel;
        this.customerModelProperty.setModel(customerModel);

    }

    public CustomerModel getCustomerModel() {
        return this.customerSavingModel;
    }

	public void test() {
		this.customerModelProperty.setCustomerName("test name");
		
	}

    
}
