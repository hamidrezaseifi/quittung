package de.seifi.rechnung_manager.models;

import de.seifi.rechnung_manager.ui.TableUtils;
import de.seifi.rechnung_manager.utils.GerldCalculator;
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
    private StringProperty nettoGesamt;
    private StringProperty bruttoGesamt;
    private RechnungModel rechnungModel;

    public ReportItemModel(RechnungModel model) {
        this.rechnungDatum = new SimpleStringProperty(model.getRechnungCreate());
        this.nummer = new SimpleStringProperty(String.valueOf(model.getNummer()));
        this.rechnungZeit = new SimpleStringProperty(model.getRechnungCreate());
        this.produktListItem = new SimpleListProperty(FXCollections.observableArrayList(model.getItems()));
        this.nettoGesamt = new SimpleStringProperty(TableUtils.formatGeld(model.getGesamt()));
        this.bruttoGesamt = new SimpleStringProperty(TableUtils.formatGeld(GerldCalculator.nettoToBrutto(model.getGesamt())));
        this.rechnungModel = model;
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

    public String getNettoGesamt() {
        return nettoGesamt.get();
    }

    public StringProperty nettoGesamtProperty() {
        return nettoGesamt;
    }

    public String getBruttoGesamt() {
        return bruttoGesamt.get();
    }

    public StringProperty bruttoGesamtProperty() {
        return bruttoGesamt;
    }

	public RechnungModel getRechnungModel() {
		return rechnungModel;
	}
    
    
}
