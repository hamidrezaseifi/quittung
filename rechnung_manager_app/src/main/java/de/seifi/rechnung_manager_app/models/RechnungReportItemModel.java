package de.seifi.rechnung_manager_app.models;

import de.seifi.rechnung_manager_app.enums.RechnungType;
import de.seifi.rechnung_manager_app.ui.TableUtils;
import de.seifi.rechnung_manager_app.utils.GerldCalculator;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

public class RechnungReportItemModel {

    private StringProperty rechnungDatum;

    private StringProperty nummer;

    private StringProperty rechnungZeit;

    private StringProperty paymentType;

    private ListProperty<RechnungItemModel> produktListItem;

    private StringProperty nettoGesamt;

    private StringProperty bruttoGesamt;

    private StringProperty anzahlung;

    private StringProperty restZahlung;

    private StringProperty typeDetails;

    private final RechnungModel rechnungModel;

    public RechnungReportItemModel(RechnungModel model,
                                   CustomerModel customerModel) {
        this.rechnungDatum = new SimpleStringProperty(model.getRechnungCreate());
        this.nummer = new SimpleStringProperty(String.valueOf(model.getNummer()));
        this.rechnungZeit = new SimpleStringProperty(model.getRechnungCreate());
        this.paymentType = new SimpleStringProperty(model.getPaymentType().getTitle());
        this.produktListItem = new SimpleListProperty<RechnungItemModel>(FXCollections.observableArrayList(model.getItems()));
        this.nettoGesamt = new SimpleStringProperty(TableUtils.formatGeld(model.getGesamt()));

        Float brutto = GerldCalculator.nettoToBrutto(model.getGesamt());
        this.bruttoGesamt = new SimpleStringProperty(TableUtils.formatGeld(brutto));
        this.anzahlung = new SimpleStringProperty(TableUtils.formatGeld(model.getAnzahlung()));
        this.restZahlung = new SimpleStringProperty(TableUtils.formatGeld(brutto - model.getAnzahlung()));

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

    public String getPaymentType() {
        return paymentType.get();
    }

    public StringProperty paymentTypeProperty() {
        return paymentType;
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

    public String getAnzahlung() {
        return anzahlung.get();
    }

    public StringProperty anzahlungProperty() {
        return anzahlung;
    }

    public String getRestZahlung() {
        return restZahlung.get();
    }

    public StringProperty restZahlungProperty() {
        return restZahlung;
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
