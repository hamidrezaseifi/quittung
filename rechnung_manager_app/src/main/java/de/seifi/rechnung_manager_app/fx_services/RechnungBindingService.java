package de.seifi.rechnung_manager_app.fx_services;


import de.seifi.rechnung_common.entities.RechnungEntity;
import de.seifi.rechnung_common.repositories.RechnungRepository;
import de.seifi.rechnung_manager_app.RechnungManagerFxApp;
import de.seifi.rechnung_manager_app.RechnungManagerSpringApp;
import de.seifi.rechnung_manager_app.adapter.RechnungAdapter;
import de.seifi.rechnung_manager_app.data_service.IRechnungDataHelper;
import de.seifi.rechnung_manager_app.enums.PaymentType;
import de.seifi.rechnung_manager_app.enums.RechnungStatus;
import de.seifi.rechnung_manager_app.enums.RechnungType;
import de.seifi.rechnung_manager_app.models.*;
import de.seifi.rechnung_manager_app.ui.UiUtils;
import de.seifi.rechnung_manager_app.utils.GeneralUtils;
import de.seifi.rechnung_manager_app.utils.GerldCalculator;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class RechnungBindingService implements IBindingService<RechnungItemProperty> {
	
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
    
    private BooleanProperty disableSave;
    private BooleanProperty disablePrint;
    private BooleanProperty visbleToggleStatusBox;

    private ObjectProperty<PaymentType> paymentTypeProperty;

    private RechnungModel rechnungSavingModel = new RechnungModel();

    private CustomerModel customerSavingModel = new CustomerModel();

    private boolean isDirty;
    private boolean isView = false;

    private final RechnungType rechnungType;


    private int activeBerechnenZiel;

    private float berechnenFaktorBasis = 1.5f;

    private List<Float> berechnenFaktorZielList = Arrays.asList(1.5f, 1.2f);

    private List<String> berechnenFaktorZielColorList = Arrays.asList("-fx-background-color: white", "-fx-background-color: #f2f6ff");
    
    private StringProperty bannerBackColor;

    private final RechnungAdapter rechnungAdapter = new RechnungAdapter();

    private boolean isCustomerSelected = false;
    
    private boolean editingMode = false;

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

        this.editingMode = false;
        
        gesamtSumme = new SimpleFloatProperty(0);
        nettoSumme = new SimpleFloatProperty(0);
        mvstSumme = new SimpleFloatProperty(0);
        
        rechnungNummer = new SimpleStringProperty();
        rechnungDatum = new SimpleStringProperty();
        paymentTypeProperty = new SimpleObjectProperty<>(PaymentType.NOT_SET);
        
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

        for(RechnungItemProperty item:this.rechnungItems){
            if(item.getIsMarkedAsDeleted()){
                continue;
            }
            netto += item.getGesamt();
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
		
		if(!this.editingMode) {
			this.rechnungItems.clear();
	        while (this.rechnungItems.size() < INITIAL_ITEMS){
	        	addNewRowIntern(new RechnungItemProperty(true));
	        }
	        calculateRechnungSumme();
	        
	        customerModelProperty = new CustomerModelProperty();

	        RechnungModel model = new RechnungModel(null, 0, "", "", 1,
                    PaymentType.NOT_SET, rechnungType, RechnungStatus.ACTIVE, RechnungManagerFxApp.loggedUser);


            initializeRechnungModel(model);

            setRechnungModel(model);
			
		}
		
		if(this.editingMode) {
			this.startEditing(rechnungSavingModel, this.customerSavingModel);
		}
        
        setDirty(false);
        this.visbleToggleStatusBox.set(true);
	}

    private void initializeRechnungModel(RechnungModel model) {
        LocalDateTime ldt = LocalDateTime.now();

        String date = GeneralUtils.formatDate(ldt);

        int lastNummer = this.rechnungDataHelper.getLastActiveRechnungNummer();

        model.setNummer(lastNummer + 1);
        model.setLiferDate(date);
        model.setRechnungCreate(date);
        model.setStatus(RechnungStatus.ACTIVE);
        model.setPaymentType(PaymentType.NOT_SET);
        model.setUserId(RechnungManagerFxApp.loggedUser);
    }

    private void setRechnungModel(RechnungModel model) {

        if(model.getNummer() == null || model.getNummer() < 1){
            initializeRechnungModel(model);

        }

		this.rechnungSavingModel = model;
        this.rechnungNummer.set(String.valueOf(rechnungSavingModel.getNummer()));
        this.rechnungDatum.set(rechnungSavingModel.getRechnungCreate());
        this.paymentTypeProperty.set(rechnungSavingModel.getPaymentType());
	}

    public List<CustomerSelectModel> getCustomerSelectList() {

        this.customerList.clear();
        List<CustomerModel> modelList = RechnungManagerSpringApp.getCustomerService().getCustomerList();
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

    public ObjectProperty<PaymentType> getPaymentTypeProperty(){
        return this.paymentTypeProperty;
    }
	public boolean isDirty() {
		return isDirty;
	}

	
	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;

        calculateButtons();

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
		visbleToggleStatusBox.set(visible);
	}

    public boolean verifySaving(boolean showError) {
        if(paymentTypeProperty.get() == PaymentType.NOT_SET){
            if(showError){
                UiUtils.showError("Rechnung-Speichern",
                        "Zahltyp ist nicht ausgewählt. Bitte wählen Sie einen Zahltyp aus.");
            }
            return false;
        }
        if(this.rechnungType == RechnungType.RECHNUNG) {
            if (!this.isCustomerSelected) {
                if(showError){
                    UiUtils.showError("Rechnung-Speichern",
                            "Es ist kein Kunde ausgewählt. Bitte wählen Sie einen Kunden aus.");
                }

                return false;
            }
        }

        if(this.rechnungItems.stream().anyMatch(pi -> !pi.isValid())){
            return false;
        }

        if(!isAnyValidArtikelToSave()){
            return false;
        }

        return true;
    }

    public boolean save() {
        if(this.editingMode){
            RechnungModel oldInstance = rechnungSavingModel.clone();
            oldInstance.setStatus(RechnungStatus.OLD);
            RechnungEntity savingEntity = rechnungAdapter.toEntity(oldInstance);
            savingEntity.setUpdated(null);
            rechnungRepository.save(savingEntity);

            rechnungSavingModel.setExemplarOf(oldInstance);

        }

        if(!this.editingMode && this.rechnungType == RechnungType.RECHNUNG){
            if(!this.isCustomerSelected){
                UiUtils.showError("Rechnung-Speichern", "Es ist kein Kunde ausgewählt. Bitte wählen Sie einen Kunden aus");
                return false;
            }
            if(customerSavingModel.isNew()){
                Optional<CustomerModel> customerModelOptional = RechnungManagerSpringApp.getCustomerService().save(customerSavingModel);
                customerSavingModel = customerModelOptional.get();
            }

            rechnungSavingModel.setCustomerId(customerSavingModel.getId());
        }
        if(this.rechnungType == RechnungType.QUITTUNG){

            rechnungSavingModel.setCustomerId(RechnungModel.QUITTUNG_CUSTOMER_ID);
        }

        rechnungSavingModel.setPaymentType(this.paymentTypeProperty.get());
        rechnungSavingModel.getItems().clear();
        rechnungSavingModel.getItems().addAll(getSavingRechnungItems());

        RechnungEntity savingEntity = rechnungAdapter.toEntity(rechnungSavingModel);
        savingEntity.setUpdated(null);
        rechnungRepository.save(savingEntity);
        Optional<RechnungEntity> savedEntityOptional = rechnungRepository.findById(savingEntity.getId());
        if(savedEntityOptional.isPresent()) {
            rechnungSavingModel = rechnungAdapter.toModel(savedEntityOptional.get());
            List<RechnungItemProperty> items = this.rechnungItems.stream().filter(RechnungItemProperty::canSaved).collect(Collectors.toList());

            RechnungManagerSpringApp.getProduktService().retreiveProduktList();
            
            for(RechnungItemProperty item: items) {
            	RechnungManagerSpringApp.getProduktService().add(item.getProdukt(), item.getBrutoPreis());

            }
            RechnungManagerSpringApp.getProduktService().retreiveProduktList();
        }
        startEditing(rechnungSavingModel, customerSavingModel);
        setDirty(false);

        return true;
    }

    private List<RechnungItemModel> getSavingRechnungItems() {
        return this.rechnungItems.stream().
                filter(RechnungItemProperty::canSaved).
                map(RechnungItemProperty::toModel).collect(Collectors.toList());
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

            Optional<String> foundProdukt = RechnungManagerSpringApp.getProduktService().getProduktMap().keySet().stream().filter(k -> k.equalsIgnoreCase(
                    value)).findAny();
            
            if(foundProdukt.isPresent()) {
            	
            	ProduktModel produktModel = RechnungManagerSpringApp.getProduktService().getProduktMap().get(foundProdukt.get());
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

	public RechnungItemProperty addNewRow() {
		RechnungItemProperty item = new RechnungItemProperty(true);
		addNewRowIntern(item);
        setDirty(true);
		return item;
	}

	public void startEditing(RechnungModel rechnungModel, CustomerModel customerModel) {
		

        this.editingMode = true;

		if(rechnungType == RechnungType.RECHNUNG) {

            if(customerModel == null){
                Optional<CustomerModel> customerEntityOptional = RechnungManagerSpringApp
                        .getCustomerService().getById(rechnungModel.getCustomerId());
                if(customerEntityOptional.isEmpty()){
                    throw new RuntimeException("Der Kunde von der Rechnung nicht gefunden!");
                }

                customerModel = customerEntityOptional.get();

            }

            this.customerSavingModel = customerModel;

            this.customerModelProperty.setModel(this.customerSavingModel);

            this.isCustomerSelected = this.customerSavingModel != null;

            this.calculateButtons();
        }

		setRechnungModel(rechnungModel);
		
		rechnungItems.clear();

        this.rechnungSavingModel = rechnungModel;
        this.paymentTypeProperty.set(this.rechnungSavingModel.getPaymentType());

		rechnungModel.getItems().forEach(r -> addNewRowIntern(new RechnungItemProperty(r)));
		
        RechnungManagerSpringApp.getProduktService().retreiveProduktList();
		
		isDirty = false;

        this.calculateRechnungSumme();
	}

    @Override
    public boolean isEditingMode() {
		return editingMode;
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
        this.isCustomerSelected = true;

    }

    public CustomerModel getCustomerModel() {
        return this.customerSavingModel;
    }

	public void test() {
		this.customerModelProperty.setCustomerName("test name");
		
	}

	public void deleteItemAtIndex(int selectedIndx) {
		if(selectedIndx == -1) {
			return;
		}
		
		if(rechnungItems.get(selectedIndx).isNewItem()) {
			rechnungItems.remove(selectedIndx);
		}
		else {
			rechnungItems.get(selectedIndx).setIsMarkedAsDeleted(true);
		}

        calculateRechnungSumme();
        setDirty(true);
	}


    public void calculateButtons() {
        this.disableSave.set(!this.verifySaving(false));

        this.disablePrint.set(isDirty);
        if(this.visbleToggleStatusBox.get()) {
            this.visbleToggleStatusBox.set(this.disableSave.get());
        }
    }
}
