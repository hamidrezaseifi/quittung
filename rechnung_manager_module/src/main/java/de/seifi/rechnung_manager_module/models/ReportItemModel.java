package de.seifi.rechnung_manager_module.models;

import de.seifi.rechnung_common.utils.GerldCalculator;
import de.seifi.rechnung_manager_app.models.RechnungItemModel;
import de.seifi.rechnung_manager_module.enums.RechnungType;
import de.seifi.rechnung_manager_module.ui.TableUtils;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

public class ReportItemModel {

    private StringProperty rechnungDatum;

    private StringProperty nummer;

    private StringProperty rechnungZeit;

    private ListProperty<RechnungItemModel> produktListItem;

    private StringProperty nettoGesamt;

    private StringProperty bruttoGesamt;

    private StringProperty typeDetails;

    private final RechnungModel rechnungModel;

    public ReportItemModel(RechnungModel model,
                           CustomerModel customerModel) {
        this.rechnungDatum = new SimpleStringProperty(model.getRechnungCreate());
        this.nummer = new SimpleStringProperty(String.valueOf(model.getNummer()));
        this.rechnungZeit = new SimpleStringProperty(model.getRechnungCreate());
        this.produktListItem = new SimpleListProperty<RechnungItemModel>(FXCollections.observableArrayList(model.getItems()));
        this.nettoGesamt = new SimpleStringProperty(TableUtils.formatGeld(model.getGesamt()));
        this.bruttoGesamt = new SimpleStringProperty(TableUtils.formatGeld(
                GerldCalculator.nettoToBrutto(model.getGesamt())));
        if(model.getRechnungType() == RechnungType.RECHNUNG){
            this.typeDetails = new SimpleStringProperty(model.getRechnungType().toString() + ": " + customerModel.getCustomerName());
        }
        if(model.getRechnungType() == RechnungType.QUITTUNG){
            this.typeDetails = new SimpleStringProperty(model.getRechnungType().toString());
        }


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

    public ListProperty<RechnungItemModel> produktListItemProperty() {
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
