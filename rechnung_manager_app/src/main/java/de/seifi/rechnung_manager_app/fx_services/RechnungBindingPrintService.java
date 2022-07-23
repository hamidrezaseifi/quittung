package de.seifi.rechnung_manager_app.fx_services;

import de.seifi.rechnung_manager_app.models.RechnungItemModel;
import de.seifi.rechnung_manager_app.models.RechnungItemPrintProperty;
import de.seifi.rechnung_manager_app.models.RechnungModel;
import de.seifi.rechnung_manager_app.ui.TableUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class RechnungBindingPrintService {

    private static int MAX_PRINT_LIST_ITEMS = 17;

    private String gesamtSumme;
    private String nettoSumme;
    private String mvstSumme;
    private RechnungModel rechnungModel = null;
    
    ObservableList<RechnungItemPrintProperty> printPropertyList;

    private int printingItemPageIndex = 0;
    private int printingItemPageCount = 0;

    public RechnungBindingPrintService() {

    }


    public void setRechnungModelList(RechnungModel model) {
        this.rechnungModel = model;
        this.printPropertyList = FXCollections.observableArrayList();

        preparePrintPropertyList();

    }
    
    public boolean increateIfMorePrintingPage() {
    	if(!hasMorePrintingPage()){
            return false;
        }
        this.printingItemPageIndex += 1;
        return true;
    }
    
    public boolean hasMorePrintingPage() {
    	if(this.printingItemPageIndex >= this.printingItemPageCount){
            return false;
        }
        
        return true;
    }
    
    private void preparePrintPropertyList() {
        calculateRechnungSumme();
    	List<RechnungItemModel> modelItems = rechnungModel.getItems();
    	this.printPropertyList.clear();

        //for(int k= 0; k< 5; k++){
            for(int i=0; i<modelItems.size(); i++){
            	this.printPropertyList.add(new RechnungItemPrintProperty(this.printPropertyList.size() + 1, modelItems.get(i)));
            }
        //}
        
        this.printingItemPageIndex = 0;
        this.printingItemPageCount = (int)(this.printPropertyList.size() / MAX_PRINT_LIST_ITEMS);
        if(this.printingItemPageCount * MAX_PRINT_LIST_ITEMS < this.printPropertyList.size()) {
        	this.printingItemPageCount += 1;
        }
    }

    public ObservableList<RechnungItemPrintProperty> getRechnungPrintItems() {
    	int start = this.printingItemPageIndex * MAX_PRINT_LIST_ITEMS;
    	int to = ((this.printingItemPageIndex + 1) * MAX_PRINT_LIST_ITEMS);
    	if(to > this.printPropertyList.size()) {
    		to = this.printPropertyList.size();
    	}
    	List<RechnungItemPrintProperty> items = this.printPropertyList.subList(start, to);
        return FXCollections.observableArrayList(items);
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