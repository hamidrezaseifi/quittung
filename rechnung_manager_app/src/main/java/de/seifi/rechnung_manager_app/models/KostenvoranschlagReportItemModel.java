package de.seifi.rechnung_manager_app.models;

import de.seifi.rechnung_manager_app.enums.RechnungType;
import de.seifi.rechnung_manager_app.ui.TableUtils;
import de.seifi.rechnung_manager_app.utils.GeneralUtils;
import de.seifi.rechnung_manager_app.utils.GerldCalculator;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;

public class KostenvoranschlagReportItemModel {

    private StringProperty created;

    private StringProperty nummer;

    private StringProperty customer;

    private ListProperty<KostenvoranschlagItemModel> produktListItem;

    private StringProperty status;

    private StringProperty totalPrise;

    private final KostenvoranschlagModel kostenvoranschlagModel;

    public KostenvoranschlagReportItemModel(KostenvoranschlagModel model,
                                            CustomerModel customerModel) {
        this.created = new SimpleStringProperty(GeneralUtils.formatDate(model.getCreated()));
        this.nummer = new SimpleStringProperty(String.valueOf(model.getNummer()));
        this.customer = new SimpleStringProperty(customerModel.getCustomerName());
        this.produktListItem = new SimpleListProperty<>(FXCollections.observableArrayList(model.getItems()));
        this.totalPrise = new SimpleStringProperty(TableUtils.formatGeld(model.getTotalPrise()));
        this.status = new SimpleStringProperty(model.getStatus().toString());

        this.kostenvoranschlagModel = model;
    }

    public String getCreated() {
        return created.get();
    }

    public StringProperty createdProperty() {
        return created;
    }

    public String getNummer() {
        return nummer.get();
    }

    public StringProperty nummerProperty() {
        return nummer;
    }

    public String getCustomer() {
        return customer.get();
    }

    public StringProperty customerProperty() {
        return customer;
    }

    public ObservableList<KostenvoranschlagItemModel> getProduktListItem() {
        return produktListItem.get();
    }

    public ListProperty<KostenvoranschlagItemModel> produktListItemProperty() {
        return produktListItem;
    }

    public String getTotalPrise() {
        return totalPrise.get();
    }

    public StringProperty totalPriseProperty() {
        return totalPrise;
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public KostenvoranschlagModel getKostenvoranschlagModel() {
        return kostenvoranschlagModel;
    }
}
