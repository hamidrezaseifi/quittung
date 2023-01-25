package de.seifi.rechnung_manager_app.fx_services;


import de.seifi.rechnung_common.entities.CustomerFahrzeugScheinEntity;
import de.seifi.rechnung_common.entities.KostenvoranschlagEntity;
import de.seifi.rechnung_common.repositories.CustomerFahrzeugScheinRepository;
import de.seifi.rechnung_common.repositories.KostenvoranschlagRepository;
import de.seifi.rechnung_manager_app.RechnungManagerSpringApp;
import de.seifi.rechnung_manager_app.adapter.KostenvoranschlagAdapter;
import de.seifi.rechnung_manager_app.data_service.IRechnungDataHelper;
import de.seifi.rechnung_manager_app.enums.KostenvoranschlagStatus;
import de.seifi.rechnung_manager_app.models.*;
import de.seifi.rechnung_manager_app.ui.UiUtils;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class KostenvoranschlagBindingService implements IBindingService<KostenvoranschlagItemProperty> {

	public static KostenvoranschlagBindingService CURRENT_INSTANCE = null;

    private int INITIAL_ITEMS = 10;

    private final KostenvoranschlagRepository kostenvoranschlagRepository;

    private final CustomerFahrzeugScheinRepository fahrzeugScheinRepository;

    private final IRechnungDataHelper rechnungDataHelper;
    //private final CustomerRepository customerRepository;

    private final Map<UUID, CustomerModel> customerList;

    private ObservableList<KostenvoranschlagItemProperty> vorschlagItems;

    private CustomerModelProperty customerModelProperty;

    private ObjectProperty<CustomerFahrzeugScheinModel> selectedFahrzeugSchein;

    private StringProperty selectedFahrzeugScheinNameProperty;

    private StringProperty schluesselNummerProperty;

    private StringProperty fahrgestellNummerProperty;


    private FloatProperty gesamtSumme;
    private FloatProperty nettoSumme;
    private FloatProperty mvstSumme;

    private StringProperty vorschlagNummer;

    private BooleanProperty disableSave;
    private BooleanProperty disablePrint;

    private BooleanProperty hasCustomerProperty;

    private KostenvoranschlagModel savingModel = new KostenvoranschlagModel();

    private CustomerModel customerSavingModel = new CustomerModel();

    private boolean isDirty;
    private boolean isView = false;

    private boolean editingMode = false;

    private final KostenvoranschlagAdapter kostenvoranschlagAdapter = new KostenvoranschlagAdapter();

    public KostenvoranschlagBindingService(KostenvoranschlagRepository kostenvoranschlagRepository,
                                           CustomerFahrzeugScheinRepository fahrzeugScheinRepository,
                                           IRechnungDataHelper rechnungDataHelper) {
    	
    	CURRENT_INSTANCE = this;

        this.kostenvoranschlagRepository = kostenvoranschlagRepository;
        this.fahrzeugScheinRepository = fahrzeugScheinRepository;
        this.rechnungDataHelper = rechnungDataHelper;

        this.customerList = new HashMap<>();
        
        gesamtSumme = new SimpleFloatProperty(0);
        nettoSumme = new SimpleFloatProperty(0);
        mvstSumme = new SimpleFloatProperty(0);

        vorschlagNummer = new SimpleStringProperty();

        disableSave = new SimpleBooleanProperty(true);
        disablePrint = new SimpleBooleanProperty(false);

        this.vorschlagItems = FXCollections.observableArrayList();

        this.selectedFahrzeugSchein = new SimpleObjectProperty<>();
        this.selectedFahrzeugSchein.set(null);
        this.selectedFahrzeugScheinNameProperty = new SimpleStringProperty("");

        this.schluesselNummerProperty = new SimpleStringProperty("");
        this.fahrgestellNummerProperty = new SimpleStringProperty("");

        this.schluesselNummerProperty.addListener((arg, oldVal, newVal) -> {
            calculateButtons();

        });

        this.fahrgestellNummerProperty.addListener((arg, oldVal, newVal) -> {
            calculateButtons();

        });

        this.selectedFahrzeugSchein.addListener((arg, oldVal, newVal) -> {
            calculateButtons();

        });

        this.editingMode = true;

        this.hasCustomerProperty = new SimpleBooleanProperty(false);

        reset();

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

    public CustomerFahrzeugScheinModel getSelectedFahrzeugSchein() {
        return selectedFahrzeugSchein.get();
    }

    public StringProperty selectedFahrzeugScheinNamePropertyProperty() {
        return selectedFahrzeugScheinNameProperty;
    }

    public String getSchluesselNummerProperty() {
        return schluesselNummerProperty.get();
    }

    public String getFahrgestellNummerProperty() {
        return fahrgestellNummerProperty.get();
    }

    public StringProperty fahrgestellNummerPropertyProperty() {
        return fahrgestellNummerProperty;
    }

    public StringProperty schluesselNummerPropertyProperty() {
        return schluesselNummerProperty;
    }

    public ObjectProperty<CustomerFahrzeugScheinModel> selectedFahrzeugScheinProperty() {
        return selectedFahrzeugSchein;
    }

    public String getSelectedFahrzeugScheinNameProperty() {
        return selectedFahrzeugScheinNameProperty.get();
    }

    public void setSelectedFahrzeugSchein(CustomerFahrzeugScheinModel model) {
        this.selectedFahrzeugSchein.set(model);
        String name = model != null ? model.getName() : "";
        this.selectedFahrzeugScheinNameProperty.set(name);
    }

    public boolean isHasCustomerProperty() {
        return hasCustomerProperty.get();
    }

    public BooleanProperty hasCustomerPropertyProperty() {
        return hasCustomerProperty;
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

	public void reset() {
		
        this.vorschlagItems.clear();
        while (this.vorschlagItems.size() < INITIAL_ITEMS){
            addNewRowIntern(new KostenvoranschlagItemProperty(true));
        }
        calculateRechnungSumme();

        customerModelProperty = new CustomerModelProperty();

        schluesselNummerProperty.set("");
        fahrgestellNummerProperty.set("");
        selectedFahrzeugSchein.set(null);
        selectedFahrzeugScheinNameProperty.set("");

        LocalDateTime ldt = LocalDateTime.now();

        String lastNummer = this.rechnungDataHelper.getNewActivevorschlagNummer();

        KostenvoranschlagModel model = new KostenvoranschlagModel(
                lastNummer,
                null,
                null,
                null,
                KostenvoranschlagStatus.OPEN);

        setModel(model);
        
        setDirty(false);
	}

	private void setModel(KostenvoranschlagModel model) {
		this.savingModel = model;
        this.vorschlagNummer.set(String.valueOf(savingModel.getNummer()));

        schluesselNummerProperty.set(model.getSchluesselNummer());
        fahrgestellNummerProperty.set(model.getFahrgestellNummer());

        if(savingModel.getFahrzeugSchein() != null){
            Optional<CustomerFahrzeugScheinEntity> CustomerFahrzeugScheinEntityOptional =
                    fahrzeugScheinRepository.findById(savingModel.getFahrzeugSchein());
            if(CustomerFahrzeugScheinEntityOptional.isPresent()){
                selectedFahrzeugSchein.set(new CustomerFahrzeugScheinModel(CustomerFahrzeugScheinEntityOptional.get()));
                selectedFahrzeugScheinNameProperty.set(CustomerFahrzeugScheinEntityOptional.get().getName());
            }
        }
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

        if (!this.hasCustomerProperty.get()) {
            if(showError){
                UiUtils.showError("Kostenvoranschlag-Speichern",
                                  "Es ist kein Kunde ausgewählt. Bitte wählen Sie einen Kunden aus.");
            }

            return false;
        }

        if (this.selectedFahrzeugSchein.get() == null &&
            schluesselNummerProperty.get().isEmpty() &&
            fahrgestellNummerProperty.get().isEmpty()) {
            if(showError){
                UiUtils.showError("Kostenvoranschlag-Speichern",
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
        if(!verifySaving(true)){
            return false;
        }
        if(customerSavingModel.isNew()){
            Optional<CustomerModel> customerModelOptional = RechnungManagerSpringApp.getCustomerService().save(customerSavingModel);
            customerSavingModel = customerModelOptional.get();
        }

        savingModel.setCustomerId(customerSavingModel.getId());
        savingModel.setFahrgestellNummer(fahrgestellNummerProperty.get());
        savingModel.setSchluesselNummer(schluesselNummerProperty.get());
        savingModel.setFahrzeugSchein(selectedFahrzeugSchein.get() != null? selectedFahrzeugSchein.get().getId(): null);

        savingModel.setItems(getSavingRechnungItems());

        KostenvoranschlagEntity savingEntity = kostenvoranschlagAdapter.toEntity(savingModel);
        savingEntity.setUpdated(null);
        savingEntity.setStatus(KostenvoranschlagStatus.ACTIVE.getValue());
        kostenvoranschlagRepository.save(savingEntity);
        Optional<KostenvoranschlagEntity> savedEntityOptional = kostenvoranschlagRepository.findById(savingEntity.getId());
        if(savedEntityOptional.isPresent()) {
            savingModel = kostenvoranschlagAdapter.toModel(savedEntityOptional.get());
        }
        startEditing(savingModel, customerSavingModel, false);
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

	public void startEditing(KostenvoranschlagModel kostenvoranschlagModel, CustomerModel customerModel, boolean isView) {

        if(customerModel == null){
            Optional<CustomerModel> customerEntityOptional = RechnungManagerSpringApp
                    .getCustomerService().getById(kostenvoranschlagModel.getCustomerId());
            if(customerEntityOptional.isEmpty()){
                throw new RuntimeException("Der Kunde von der Kostenvoranschlag nicht gefunden!");
            }

            customerModel = customerEntityOptional.get();
        }

        this.customerSavingModel = customerModel;

        this.hasCustomerProperty.set(this.customerSavingModel != null);

        this.customerModelProperty.setModel(this.customerSavingModel);

        this.calculateButtons();

		setModel(kostenvoranschlagModel);
		
		vorschlagItems.clear();

        this.savingModel = kostenvoranschlagModel;

		kostenvoranschlagModel.getItems().forEach(r -> addNewRowIntern(new KostenvoranschlagItemProperty(r)));
        if(!isView){
            while (this.vorschlagItems.size() < INITIAL_ITEMS){
                addNewRowIntern(new KostenvoranschlagItemProperty(true));
            }
        }

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
        if(customerModel.isNew()){
            Optional<CustomerModel> customerModelOptional = RechnungManagerSpringApp.getCustomerService().save(customerModel);
            customerModel = customerModelOptional.get();
        }
        this.customerSavingModel = customerModel;
        this.customerModelProperty.setModel(customerModel);
        this.hasCustomerProperty.set(customerModel != null);
        setSelectedFahrzeugSchein(null);

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

    @Override
    public boolean isEditingMode() {
        return editingMode;
    }
}
