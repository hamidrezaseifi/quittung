package de.seifi.rechnung_manager_app.fx_services;


import de.seifi.rechnung_common.entities.CustomerEntity;
import de.seifi.rechnung_common.entities.ProduktEntity;
import de.seifi.rechnung_common.entities.RechnungEntity;
import de.seifi.rechnung_common.repositories.CustomerRepository;
import de.seifi.rechnung_common.repositories.ProduktRepository;
import de.seifi.rechnung_manager_app.adapter.CustomerAdapter;
import de.seifi.rechnung_manager_app.adapter.ProduktAdapter;
import de.seifi.rechnung_manager_app.adapter.RechnungAdapter;
import de.seifi.rechnung_manager_app.enums.CustomerStatus;
import de.seifi.rechnung_manager_app.models.*;
import de.seifi.rechnung_manager_app.services.ICustomerService;
import de.seifi.rechnung_manager_app.services.IRechnungService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;

public class RechnungReportBindingService {
	
	public static RechnungReportBindingService CURRENT_INSTANCE = null;

    private final IRechnungService rechnungService;

    private final ICustomerService customerService;

    private ObservableList<RechnungReportItemModel> reportItems;

    private SearchFilterProperty searchFilterProperty;

    private final ProduktAdapter produktAdapter = new ProduktAdapter();

    private final RechnungAdapter rechnungAdapter = new RechnungAdapter(); 
    
    public RechnungReportBindingService(final IRechnungService rechnungService,
                                        ICustomerService customerService) {
    	
    	CURRENT_INSTANCE = this;

    	this.rechnungService = rechnungService;
        this.customerService = customerService;

        reportItems = FXCollections.observableArrayList();

        this.searchFilterProperty = new SearchFilterProperty();
        this.searchFilterProperty.setFrom(LocalDate.now().minusMonths(2));
        this.searchFilterProperty.setTo(LocalDate.now());


    }

    public ObservableList<RechnungReportItemModel> getReportItems() {
        return reportItems;
    }

	public void search() {
        Map<UUID, CustomerModel> allCustomers = customerService.getCustomerMap();

		reportItems.clear();

        List<RechnungEntity> entityList = this.rechnungService.search(searchFilterProperty);

        List<RechnungReportItemModel> modelList = entityList.stream().map(e -> new RechnungReportItemModel(rechnungAdapter.toModel(e), allCustomers.get(e.getCustomerId()))).collect(Collectors.toList());

        reportItems.addAll(modelList);
	}

    public SearchFilterProperty getSearchFilterProperty() {
        return searchFilterProperty;
    }

    public CustomerModel getSelectedCustomerModel() {
        CustomerModel selectedModel = searchFilterProperty.customerProperty().get();
        return selectedModel == null ? new CustomerModel() : selectedModel;
    }

}
