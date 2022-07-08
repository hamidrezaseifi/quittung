package de.seifi.rechnung_manager.fx_services;

import de.seifi.rechnung_manager.models.RechnungItemModel;
import de.seifi.rechnung_manager.models.RechnungModel;
import de.seifi.rechnung_manager.models.RechnungItemProperty;
import de.seifi.rechnung_manager.ui.TableUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RechnungBindingPrintService {

    private String gesamtSumme;
    private String nettoSumme;
    private String mvstSumme;
    private List<RechnungModel> rechnungModelList = new ArrayList<>();

    private int printingIndex = 0;

    public RechnungBindingPrintService() {



    }

    public ObservableList<RechnungItemProperty> getRechnungItems() {
        List<RechnungItemProperty> propList =
                rechnungModelList.get(printingIndex).getItems().stream().map(i -> new RechnungItemProperty(i)).collect(Collectors.toList());
        return FXCollections.observableArrayList(propList);
    }

    public String getNettoSumme() {
        return nettoSumme;
    }

    public String getMvstSumme() {
        return mvstSumme;
    }

    public String getRechnungNummer() {
        return String.valueOf(rechnungModelList.get(printingIndex).getNummer());
    }

    public String getGesamtSumme() {
        return gesamtSumme;
    }

    public String getRechnungDatum() {
        return rechnungModelList.get(printingIndex).getRechnungCreate();
    }

    public String getLiferDatum() {
        return rechnungModelList.get(printingIndex).getLiferDate();
    }

    public void setRechnungModelList(List<RechnungModel> rechnungModelList) {
        this.rechnungModelList = rechnungModelList;
        if(!rechnungModelList.isEmpty()){
            setPrintingIndex(0);
            calculateRechnungSumme();
        }

    }

    private void calculateRechnungSumme() {
        float netto = 0;

        for(RechnungItemModel i:rechnungModelList.get(printingIndex).getItems()){
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
        if(this.printingIndex >= rechnungModelList.size() - 1){
            return false;
        }
        this.printingIndex += 1;
        calculateRechnungSumme();
        return true;
    }

    public boolean hasPrintingPage() {
        if(this.printingIndex >= rechnungModelList.size()){
            return false;
        }

        return true;
    }

}