package de.seifi.rechnung_manager_app.fx_services;

import de.seifi.rechnung_common.entities.CustomerEntity;
import de.seifi.rechnung_common.entities.KostenvoranschlagEntity;
import de.seifi.rechnung_common.entities.RechnungEntity;
import de.seifi.rechnung_common.repositories.CustomerRepository;
import de.seifi.rechnung_common.repositories.ProduktRepository;
import de.seifi.rechnung_manager_app.adapter.CustomerAdapter;
import de.seifi.rechnung_manager_app.adapter.KostenvoranschlagAdapter;
import de.seifi.rechnung_manager_app.enums.CustomerStatus;
import de.seifi.rechnung_manager_app.models.*;
import de.seifi.rechnung_manager_app.services.ICustomerService;
import de.seifi.rechnung_manager_app.services.IKostenvoranschlagService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class KostenvoranschlagReportBindingService {

    public static KostenvoranschlagReportBindingService CURRENT_INSTANCE = null;

    private final IKostenvoranschlagService kostenvoranschlagService;

    private final ICustomerService customerService;

    private KostenvoranschlagSearchFilterProperty searchFilterProperty;

    private ObservableList<KostenvoranschlagReportItemModel> reportItems;

    private final KostenvoranschlagAdapter kostenvoranschlagAdapter = new KostenvoranschlagAdapter();
    private final CustomerAdapter customerAdapter = new CustomerAdapter();

    public KostenvoranschlagReportBindingService(IKostenvoranschlagService kostenvoranschlagService,
                                                 ICustomerService customerService) {
        CURRENT_INSTANCE = this;

        this.kostenvoranschlagService = kostenvoranschlagService;
        this.customerService = customerService;

        reportItems = FXCollections.observableArrayList();

        this.searchFilterProperty = new KostenvoranschlagSearchFilterProperty();
        this.searchFilterProperty.setFrom(LocalDate.now().minusMonths(2));
        this.searchFilterProperty.setTo(LocalDate.now());

    }

    public ObservableList<KostenvoranschlagReportItemModel> getReportItems() {
        return reportItems;
    }

    public void search() {

        Map<UUID, CustomerModel> allCustomers = customerService.getCustomerMap();

        reportItems.clear();

        List<KostenvoranschlagEntity> entityList = this.kostenvoranschlagService.search(searchFilterProperty);

        List<KostenvoranschlagReportItemModel> modelList =
                entityList.stream().map(e -> new KostenvoranschlagReportItemModel(kostenvoranschlagAdapter.toModel(e),
                                                                                  allCustomers.get(e.getCustomerId()))).collect(
                Collectors.toList());

        reportItems.addAll(modelList);

    }

    public CustomerModel getSelectedCustomerModel() {
        CustomerModel selectedModel = searchFilterProperty.customerProperty().get();
        return selectedModel == null ? new CustomerModel() : selectedModel;
    }

    public KostenvoranschlagSearchFilterProperty getSearchFilterProperty() {
        return searchFilterProperty;
    }
}
