package de.seifi.rechnung_manager_app.models;

import de.seifi.rechnung_manager_app.entities.CustomerEntity;
import de.seifi.rechnung_manager_app.enums.RechnungType;
import de.seifi.rechnung_manager_app.ui.TableUtils;
import de.seifi.rechnung_manager_app.utils.GerldCalculator;
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

    private StringProperty typeDetails;

    private final RechnungModel rechnungModel;

    private final CustomerModel customerModel;

    public ReportItemModel(RechnungModel model,
                           CustomerModel customerModel) {
        this.rechnungDatum = new SimpleStringProperty(model.getRechnungCreate());
        this.nummer = new SimpleStringProperty(String.valueOf(model.getNummer()));
        this.rechnungZeit = new SimpleStringProperty(model.getRechnungCreate());
        this.produktListItem = new SimpleListProperty(FXCollections.observableArrayList(model.getItems()));
        this.nettoGesamt = new SimpleStringProperty(TableUtils.formatGeld(model.getGesamt()));
        this.bruttoGesamt = new SimpleStringProperty(TableUtils.formatGeld(GerldCalculator.nettoToBrutto(model.getGesamt())));
        if(model.getRechnungType() == RechnungType.RECHNUNG){
            this.typeDetails = new SimpleStringProperty(model.getRechnungType().toString() + ": " + customerModel.getCustomerName());
        }
        if(model.getRechnungType() == RechnungType.QUITTUNG){
            this.typeDetails = new SimpleStringProperty(model.getRechnungType().toString());
        }


        this.rechnungModel = model;
        this.customerModel = customerModel;
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

    public String getTypeDetails() {
        return typeDetails.get();
    }

    public StringProperty typeDetailsProperty() {
        return typeDetails;
    }

    public RechnungModel getRechnungModel() {
		return rechnungModel;
	}
    
    
}
