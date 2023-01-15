package de.seifi.rechnung_manager_app.models;

import de.seifi.rechnung_manager_app.utils.GeneralUtils;
import javafx.beans.property.*;

import java.time.LocalDate;
import java.util.UUID;

public class KostenvoranschlagSearchFilterProperty {

    private ObjectProperty<LocalDate> from;

    private ObjectProperty<LocalDate> to;

    private StringProperty nummer;

    private StringProperty produkt;

    private StringProperty label;

    private ObjectProperty<CustomerModel> customer;

    private StringProperty orderBy;

    private StringProperty orderType;

    private IntegerProperty status;

    public KostenvoranschlagSearchFilterProperty() {
        this.from = new SimpleObjectProperty<LocalDate>();
        this.to = new SimpleObjectProperty<LocalDate>();
        this.nummer = new SimpleStringProperty("");
        this.produkt = new SimpleStringProperty("");
        this.label = new SimpleStringProperty("Keine Filter");
        this.customer = new SimpleObjectProperty<>(null);
        this.orderBy = new SimpleStringProperty("nummer");
        this.orderType = new SimpleStringProperty("desc");
        this.status = new SimpleIntegerProperty(-1);

        this.from.addListener((observableValue, localDate, t1) -> {
            reloadLabel();
        });

        this.to.addListener((observableValue, localDate, t1) -> {
            reloadLabel();
        });

        this.nummer.addListener((observableValue, s, t1) -> {
            reloadLabel();
        });

        this.produkt.addListener((observableValue, s, t1) -> {
            reloadLabel();
        });

        this.customer.addListener((observableValue, s, t1) -> {
            reloadLabel();
        });
    }

    private void reloadLabel() {
        this.label.set(getLabelText());
    }

    public LocalDate getFrom() {
        return from.get();
    }

    public ObjectProperty<LocalDate> fromProperty() {
        return from;
    }

    public void setFrom(LocalDate from) {
        this.from.set(from);
    }

    public LocalDate getTo() {
        return to.get();
    }

    public ObjectProperty<LocalDate> toProperty() {
        return to;
    }

    public void setTo(LocalDate to) {
        this.to.set(to);

    }

    public String getNummer() {
        return nummer.get();
    }

    public boolean hasNummer() {
        return nummer.get()!= null && !nummer.get().trim().isEmpty();
    }

    public StringProperty nummerProperty() {
        return nummer;
    }

    public void setNummer(String nummer) {
        reloadLabel();
    }

    public String getProdukt() {
        return produkt.get();
    }

    public UUID getCustomerId() {
        return this.customer.get()!= null ? this.customer.get().getId() : null;
    }

    public StringProperty produktProperty() {
        return produkt;
    }

    public void setProdukt(String produkt) {
        reloadLabel();
    }

    public CustomerModel getCustomer() {
        return customer.get();
    }

    public ObjectProperty<CustomerModel> customerProperty() {
        return customer;
    }

    public void setCustomer(CustomerModel customer) {
        this.customer.set(customer);
    }

    public String getLabel() {
        return label.get();
    }

    public StringProperty labelProperty() {
        return label;
    }

    public String getOrderBy() {
        return orderBy.get();
    }

    public StringProperty orderByProperty() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy.set(orderBy);
    }

    public String getOrderType() {
        return orderType.get();
    }

    public StringProperty orderTypeProperty() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType.set(orderType);
    }

    public int getStatus() {
        return status.get();
    }

    public IntegerProperty statusProperty() {
        return status;
    }

    public void setStatus(int status) {
        this.status.set(status);
    }

    private String getLabelText() {
        String labelText = "Filter: ";
        if(this.from.get() != null){
            String date = GeneralUtils.formatDate(this.from.get());
            labelText += String.format("Von '%s',  ", date);
        }
        if(this.to.get() != null){
            String date = GeneralUtils.formatDate(this.to.get());
            labelText += String.format("Bis '%s',  ", date);
        }
        if(this.nummer.get() != null && !this.nummer.get().trim().isEmpty()){
            labelText += String.format("nummer '%s',  ", this.nummer.get());
        }
        if(this.produkt.get() != null && !this.produkt.get().trim().isEmpty()){
            labelText += String.format("nummer '%s',  ", this.produkt.get());
        }
        if(this.customer.get() != null){
            labelText += String.format("Kunde '%s',  ", this.customer.get().getCustomerName());
        }

        return labelText;
    }

}
