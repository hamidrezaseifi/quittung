package de.seifi.rechnung_manager.models;

import de.seifi.rechnung_manager.ui.TableUtils;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

public class ReportItemModel {

    private StringProperty rechnungDatum;
    private StringProperty nummer;
    private StringProperty rechnungZeit;
    private ListProperty produktListItem;
    private StringProperty gesamt;

    public ReportItemModel(RechnungModel model) {
        this.rechnungDatum = new SimpleStringProperty(model.getRechnungCreate());
        this.nummer = new SimpleStringProperty(String.valueOf(model.getNummer()));
        this.rechnungZeit = new SimpleStringProperty(model.getRechnungCreate());
        this.produktListItem = new SimpleListProperty(FXCollections.observableArrayList(model.getItems()));
        this.gesamt = new SimpleStringProperty(TableUtils.formatGeld(model.getGesamt()));
    }

    public String getRechnungDatum() {
        return rechnungDatum.get();
    }

    public StringProperty rechnungDatumProperty() {
        return rechnungDatum;
    }

    public String getNummer() {
        return nummer.get();
    }

    public StringProperty nummerProperty() {
        return nummer;
    }

    public String getRechnungZeit() {
        return rechnungZeit.get();
    }

    public StringProperty rechnungZeitProperty() {
        return rechnungZeit;
    }

    public Object getProduktListItem() {
        return produktListItem.get();
    }

    public ListProperty produktListItemProperty() {
        return produktListItem;
    }

    public String getGesamt() {
        return gesamt.get();
    }

    public StringProperty gesamtProperty() {
        return gesamt;
    }
}
