package de.seifi.rechnung_manager.fx_services;

import de.seifi.rechnung_manager.models.RechnungItemModel;
import de.seifi.rechnung_manager.models.RechnungModel;
import de.seifi.rechnung_manager.ui.QuittungItemProperty;
import de.seifi.rechnung_manager.ui.TableUtils;
import javafx.beans.property.FloatProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QuittungBindingPrintService {

    private String gesamtSumme;
    private String nettoSumme;
    private String mvstSumme;
    private List<RechnungModel> quittungModelList = new ArrayList<>();

    private int printingIndex = 0;

    public QuittungBindingPrintService() {



    }

    public ObservableList<QuittungItemProperty> getQuittungItems() {
        List<QuittungItemProperty> propList =
                quittungModelList.get(printingIndex).getItems().stream().map(i -> new QuittungItemProperty(i)).collect(Collectors.toList());
        return FXCollections.observableArrayList(propList);
    }

    public String getNettoSumme() {
        return nettoSumme;
    }

    public String getMvstSumme() {
        return mvstSumme;
    }

    public String getQuittungNummer() {
        return String.valueOf(quittungModelList.get(printingIndex).getNummer());
    }

    public String getGesamtSumme() {
        return gesamtSumme;
    }

    public String getQuittungDatum() {
        return quittungModelList.get(printingIndex).getRechnungCreate();
    }

    public String getLiferDatum() {
        return quittungModelList.get(printingIndex).getLiferDate();
    }

    public void setQuittungModelList(List<RechnungModel> quittungModelList) {
        this.quittungModelList = quittungModelList;
        if(!quittungModelList.isEmpty()){
            setPrintingIndex(0);
            calculateQuittungSumme();
        }

    }

    private void calculateQuittungSumme() {
        float netto = 0;

        for(RechnungItemModel i:quittungModelList.get(printingIndex).getItems()){
            netto += i.getGesmt();
        }

        nettoSumme = TableUtils.formatGeld(netto);
        mvstSumme = TableUtils.formatGeld(netto * 19 / 100);
        gesamtSumme = TableUtils.formatGeld(netto + (netto * 19 / 100));
    }

    public void setPrintingIndex(int printingIndex) {
        this.printingIndex = printingIndex;
    }

    public boolean increasePrintingIndex() {
        if(this.printingIndex >= quittungModelList.size() - 1){
            return false;
        }
        this.printingIndex += 1;
        calculateQuittungSumme();
        return true;
    }

    public boolean hasPrintingPage() {
        if(this.printingIndex >= quittungModelList.size()){
            return false;
        }

        return true;
    }

}