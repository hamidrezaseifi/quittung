package de.seifi.rechnung_manager_app.fx_services;


import de.seifi.rechnung_common.entities.KostenvoranschlagEntity;
import de.seifi.rechnung_common.entities.RechnungEntity;
import de.seifi.rechnung_common.repositories.KostenvoranschlagRepository;
import de.seifi.rechnung_manager_app.RechnungManagerFxApp;
import de.seifi.rechnung_manager_app.RechnungManagerSpringApp;
import de.seifi.rechnung_manager_app.adapter.KostenvoranschlagAdapter;
import de.seifi.rechnung_manager_app.adapter.RechnungAdapter;
import de.seifi.rechnung_manager_app.data_service.IRechnungDataHelper;
import de.seifi.rechnung_manager_app.enums.KostenvoranschlagStatus;
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

public class KostenvoranschlagBindingService {

	public static KostenvoranschlagBindingService CURRENT_INSTANCE = null;

    private int INITIAL_ITEMS = 10;

    private final KostenvoranschlagRepository kostenvoranschlagRepository;
    private final IRechnungDataHelper rechnungDataHelper;
    //private final CustomerRepository customerRepository;

    private final Map<UUID, CustomerModel> customerList;

    private ObservableList<KostenvoranschlagItemProperty> vorschlagItems;
    private CustomerModelProperty customerModelProperty;

    private FloatProperty gesamtSumme;
    private FloatProperty nettoSumme;
    private FloatProperty mvstSumme;

    private StringProperty vorschlagNummer;

    private BooleanProperty disableSave;
    private BooleanProperty disablePrint;

    private KostenvoranschlagModel savingModel = new KostenvoranschlagModel();

    private CustomerModel customerSavingModel = new CustomerModel();

    private boolean isDirty;
    private boolean isView = false;


    private int activeBerechnenZiel;

    private float berechnenFaktorBasis = 1.4f;

    private List<Float> berechnenFaktorZielList = Arrays.asList(1.4f, 1.2f);

    private List<String> berechnenFaktorZielColorList = Arrays.asList("-fx-background-color: white", "-fx-background-color: #f2f6ff");

    private StringProperty bannerBackColor;

    private final KostenvoranschlagAdapter kostenvoranschlagAdapter = new KostenvoranschlagAdapter();

    private boolean isCustomerSelected = false;


    public KostenvoranschlagBindingService(final KostenvoranschlagRepository kostenvoranschlagRepository,
                                           final IRechnungDataHelper rechnungDataHelper) {
    	
    	CURRENT_INSTANCE = this;

        this.activeBerechnenZiel = 0;
        this.bannerBackColor = new SimpleStringProperty(this.berechnenFaktorZielColorList.get(this.activeBerechnenZiel));

        this.kostenvoranschlagRepository = kostenvoranschlagRepository;
        this.rechnungDataHelper = rechnungDataHelper;

        this.customerList = new HashMap<>();
        
        gesamtSumme = new SimpleFloatProperty(0);
        nettoSumme = new SimpleFloatProperty(0);
        mvstSumme = new SimpleFloatProperty(0);

        vorschlagNummer = new SimpleStringProperty();

        disableSave = new SimpleBooleanProperty(true);
        disablePrint = new SimpleBooleanProperty(false);

        this.vorschlagItems = FXCollections.observableArrayList();
        
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

        for(KostenvoranschlagItemProperty item:this.vorschlagItems){

            netto += item.getPreis();
        }
        
        nettoSumme.set(netto);
        mvstSumme.set(netto * 19 / 100);
        gesamtSumme.set(nettoSumme.getValue() + mvstSumme.getValue());
    }

    public ObservableList<KostenvoranschlagItemProperty> getKostenvoranschlagItems() {
        return this.vorschlagItems;
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
		
        this.vorschlagItems.clear();
        while (this.vorschlagItems.size() < INITIAL_ITEMS){
            addNewRowIntern(new KostenvoranschlagItemProperty(true));
        }
        calculateRechnungSumme();

        customerModelProperty = new CustomerModelProperty();


        LocalDateTime ldt = LocalDateTime.now();

        String lastNummer = this.rechnungDataHelper.getNewActivevorschlagNummer();

        KostenvoranschlagModel model = new KostenvoranschlagModel(
                lastNummer,
                null,
                null,
                null,
                KostenvoranschlagStatus.OPEN);

        setRechnungModel(model);
        
        setDirty(false);
	}

	private void setRechnungModel(KostenvoranschlagModel model) {
		this.savingModel = model;
        this.vorschlagNummer.set(String.valueOf(savingModel.getNummer()));
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

	public String getCurrentvorschlagNummer() {
		return savingModel.getNummer();
	}

    public KostenvoranschlagModel getRechnungSavingModel() {
        return savingModel;
    }

    public StringProperty getNummerProperty() {
		return vorschlagNummer;
	}

	public StringProperty getRechnungDatumProperty() {
		return vorschlagNummer;
	}

	public boolean isDirty() {
		return isDirty;
	}

	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;

        calculateButtons();

    }
	
	private boolean isAnyValidArtikelToSave() {
		return this.vorschlagItems.stream().anyMatch(pi -> pi.isValid() & !pi.isEmpty());
	}

	public BooleanProperty getDisableSaveProperty() {
		return disableSave;
	}

	public BooleanProperty getDisablePrintProperty() {
		return disablePrint;
	}

    public boolean verifySaving(boolean showError) {

        if (!this.isCustomerSelected) {
            if(showError){
                UiUtils.showError("Rechnung-Speichern",
                        "Es ist kein Kunde ausgewählt. Bitte wählen Sie einen Kunden aus.");
            }

            return false;
        }

        if(this.vorschlagItems.stream().anyMatch(pi -> !pi.isValid())){
            return false;
        }

        if(!isAnyValidArtikelToSave()){
            return false;
        }

        return true;
    }

    public boolean save() {
        if(!this.isCustomerSelected){
            UiUtils.showError("Rechnung-Speichern", "Es ist kein Kunde ausgewählt. Bitte wählen Sie einen Kunden aus");
            return false;
        }
        if(customerSavingModel.isNew()){
            Optional<CustomerModel> customerModelOptional = RechnungManagerSpringApp.getCustomerService().save(customerSavingModel);
            customerSavingModel = customerModelOptional.get();
        }

        savingModel.setCustomerId(customerSavingModel.getId());

        savingModel.getItems().clear();
        savingModel.getItems().addAll(getSavingRechnungItems());

        KostenvoranschlagEntity savingEntity = kostenvoranschlagAdapter.toEntity(savingModel);
        savingEntity.setUpdated(null);
        kostenvoranschlagRepository.save(savingEntity);
        startEditing(savingModel, customerSavingModel);
        setDirty(false);

        return true;
    }

    private List<KostenvoranschlagItemModel> getSavingRechnungItems() {
        return this.vorschlagItems.stream().
                filter(KostenvoranschlagItemProperty::canSaved).
                map(KostenvoranschlagItemProperty::toModel).collect(Collectors.toList());
    }

	public void setNewProduktValue(int row, String value) {

        KostenvoranschlagItemProperty prop = this.vorschlagItems.get(row);
        if(prop.getProdukt() != value) {
            prop.setProdukt(value);
            calculateRechnungSumme();
            setDirty(true);

        }
		
	}

    public void setNewOriginalNummerValue(int row, String value) {
        KostenvoranschlagItemProperty prop = this.vorschlagItems.get(row);
        if(prop.getProdukt() != value) {
            prop.setOriginalNummer(value);
            setDirty(true);

        }
    }

    public void setNewTeilNummerValue(int row, String value) {
        KostenvoranschlagItemProperty prop = this.vorschlagItems.get(row);
        if(prop.getProdukt() != value) {
            prop.setTeilNummer(value);
            setDirty(true);

        }
    }

    public void setNewMarkeValue(int row, String value) {
        KostenvoranschlagItemProperty prop = this.vorschlagItems.get(row);
        if(prop.getProdukt() != value) {
            prop.setMarke(value);
            setDirty(true);

        }
    }

    public void setNewPreisValue(int row, Float value) {
        KostenvoranschlagItemProperty prop = this.vorschlagItems.get(row);
        if(prop.getPreis() != value) {
            prop.setPreis(value);
            setDirty(true);

        }
    }



	private void addNewRowIntern(KostenvoranschlagItemProperty item) {
		
		this.vorschlagItems.add(item);
	}

	public KostenvoranschlagItemProperty addNewRow() {
        KostenvoranschlagItemProperty item = new KostenvoranschlagItemProperty(true);
		addNewRowIntern(item);
        setDirty(true);
		return item;
	}

	public void startEditing(KostenvoranschlagModel rechnungModel, CustomerModel customerModel) {

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

		setRechnungModel(rechnungModel);
		
		vorschlagItems.clear();

        this.savingModel = rechnungModel;

		rechnungModel.getItems().forEach(r -> addNewRowIntern(new KostenvoranschlagItemProperty(r)));
		
        RechnungManagerSpringApp.getProduktService().retreiveProduktList();
		
		isDirty = false;

        this.calculateRechnungSumme();
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
		
		if(vorschlagItems.get(selectedIndx).isNewItem()) {
			vorschlagItems.remove(selectedIndx);
		}

        calculateRechnungSumme();
        setDirty(true);
	}


    public void calculateButtons() {
        this.disableSave.set(!this.verifySaving(false));

        this.disablePrint.set(isDirty);

    }
}
