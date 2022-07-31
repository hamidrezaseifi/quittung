package de.seifi.rechnung_manager_app.models;

import de.seifi.rechnung_manager_app.utils.GeneralUtils;
import javafx.beans.property.*;

import java.time.LocalDate;

public class SearchFilterProperty {

    private ObjectProperty<LocalDate> from;

    private ObjectProperty<LocalDate> to;

    private StringProperty nummer;

    private StringProperty produkt;

    private StringProperty label;

    public SearchFilterProperty() {
        this.from = new SimpleObjectProperty<LocalDate>();
        this.to = new SimpleObjectProperty<LocalDate>();
        this.nummer = new SimpleStringProperty("");
        this.produkt = new SimpleStringProperty("");
        this.label = new SimpleStringProperty("Keine Filter");

        this.from.addListener((observableValue, localDate, t1) -> {
            this.label.set(getLabelText());
        });

        this.to.addListener((observableValue, localDate, t1) -> {
            this.label.set(getLabelText());
        });

        this.nummer.addListener((observableValue, s, t1) -> {
            this.label.set(getLabelText());
        });

        this.produkt.addListener((observableValue, s, t1) -> {
            this.label.set(getLabelText());
        });
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
        this.nummer.set(nummer);
    }

    public String getProdukt() {
        return produkt.get();
    }

    public StringProperty produktProperty() {
        return produkt;
    }

    public void setProdukt(String produkt) {
        this.produkt.set(produkt);
    }

    public String getLabel() {
        return label.get();
    }

    public StringProperty labelProperty() {
        return label;
    }

    public void setLabel(String label) {
        this.label.set(label);
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

        return labelText;
    }

}
