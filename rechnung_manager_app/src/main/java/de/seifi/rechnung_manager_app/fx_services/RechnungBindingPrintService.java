package de.seifi.rechnung_manager_app.fx_services;

import de.seifi.rechnung_manager_app.enums.RechnungType;
import de.seifi.rechnung_manager_app.models.CustomerModel;
import de.seifi.rechnung_manager_app.models.RechnungItemModel;
import de.seifi.rechnung_manager_app.models.RechnungItemPrintProperty;
import de.seifi.rechnung_manager_app.models.RechnungModel;
import de.seifi.rechnung_manager_app.ui.TableUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RechnungBindingPrintService {

    //private static int MAX_PRINT_ROW_COUNT = 12;
    Map<RechnungType, Integer> RECHNUNG_TYPE_MAX_ROW_COUNT =
            Map.ofEntries(Map.entry(RechnungType.RECHNUNG,12), Map.entry(RechnungType.QUITTUNG,14));


    private String gesamtSumme;
    private String nettoSumme;
    private String mvstSumme;
    private RechnungModel rechnungModel = null;
    private CustomerModel customerModel = null;

    private int itemPageCount = 0;
    
    List<ObservableList<RechnungItemPrintProperty>> printPropertyList;

    private final RechnungType rechnungType;

    public RechnungBindingPrintService(RechnungType rechnungType) {
        this.rechnungType = rechnungType;
    }


    public void setRechnungModel(RechnungModel model,
                                 CustomerModel customerModel) {
        this.rechnungModel = model;
        this.customerModel = customerModel;

        preparePrintPropertyList();

    }

    public int getItemPageCount() {
        return itemPageCount;
    }

    private void preparePrintPropertyList() {
        calculateRechnungSumme();
    	List<RechnungItemModel> modelItems = rechnungModel.getItems();
        this.printPropertyList = new ArrayList<>(); //FXCollections.observableArrayList();
        int maxPrintRowCount = RECHNUNG_TYPE_MAX_ROW_COUNT.get(this.rechnungType);

        itemPageCount = (int)(rechnungModel.getItems().size() / maxPrintRowCount);
        if(itemPageCount * maxPrintRowCount < rechnungModel.getItems().size()){
            itemPageCount ++;
        }

        for(int i=0; i<itemPageCount; i++){
            ObservableList<RechnungItemPrintProperty> obsList = FXCollections.observableArrayList();
            int from = i * maxPrintRowCount;
            int to = (i + 1) * maxPrintRowCount;
            if(to > rechnungModel.getItems().size()){
                to = rechnungModel.getItems().size();
            }

            List<RechnungItemModel> itemsSubList = rechnungModel.getItems().subList(from, to);
            for(int j=0; j<itemsSubList.size(); j++){
                obsList.add(new RechnungItemPrintProperty(i * maxPrintRowCount + j + 1, itemsSubList.get(j)));
            }
            this.printPropertyList.add(obsList);
        }
    }

    public ObservableList<RechnungItemPrintProperty> getRechnungPrintPageItems(int pageIndex) {
        return this.printPropertyList.get(pageIndex);
    }

    public String getNettoSumme() {
        return nettoSumme;
    }

    public String getMvstSumme() {
        return mvstSumme;
    }

    public String getRechnungNummer() {
        return String.valueOf(rechnungModel.getNummer());
    }

    public String getGesamtSumme() {
        return gesamtSumme;
    }

    public String getRechnungDatum() {
        return rechnungModel.getRechnungCreate();
    }

    public String getLiferDatum() {
        return rechnungModel.getLiferDate();
    }

    public CustomerModel getCustomerModel() {
        return customerModel;
    }

    private void calculateRechnungSumme() {
        float netto = 0;

        for(RechnungItemModel i:rechnungModel.getItems()){
            netto += i.getGesmt();
        }

        nettoSumme = TableUtils.formatGeld(netto);
        mvstSumme = TableUtils.formatGeld(netto * 19 / 100);
        gesamtSumme = TableUtils.formatGeld(netto + (netto * 19 / 100));
    }

}