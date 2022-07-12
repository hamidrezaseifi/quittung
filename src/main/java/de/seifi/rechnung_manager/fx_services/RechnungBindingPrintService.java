package de.seifi.rechnung_manager.fx_services;

import de.seifi.rechnung_manager.models.RechnungItemModel;
import de.seifi.rechnung_manager.models.RechnungItemPrintProperty;
import de.seifi.rechnung_manager.models.RechnungModel;
import de.seifi.rechnung_manager.ui.TableUtils;
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
    private List<RechnungModel> rechnungModelList = new ArrayList<>();
    
    ObservableList<RechnungItemPrintProperty> printPropertyList;

    private int printingIndex = 0;
    private int printingItemPageIndex = 0;
    private int printingItemPageCount = 0;

    public RechnungBindingPrintService() {

    }


    public void setRechnungModelList(List<RechnungModel> rechnungModelList) {
        this.rechnungModelList = rechnungModelList;
        this.printPropertyList = FXCollections.observableArrayList();
        
        if(!rechnungModelList.isEmpty()){
            setPrintingIndex(0);
            
        }

    }

    public boolean increasePrintingIndex() {
        if(this.printingIndex >= rechnungModelList.size() - 1){
            return false;
        }
        this.printingIndex += 1;
        setPrintingIndex(this.printingIndex);
        calculateRechnungSumme();
        return true;
    }

    public void setPrintingIndex(int printingIndex) {
        this.printingIndex = printingIndex;
        preparePrintPropertyList();
        calculateRechnungSumme();
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
    	List<RechnungItemModel> modelItems = rechnungModelList.get(printingIndex).getItems();
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

    private void calculateRechnungSumme() {
        float netto = 0;

        for(RechnungItemModel i:rechnungModelList.get(printingIndex).getItems()){
            netto += i.getGesmt();
        }

        nettoSumme = TableUtils.formatGeld(netto);
        mvstSumme = TableUtils.formatGeld(netto * 19 / 100);
        gesamtSumme = TableUtils.formatGeld(netto + (netto * 19 / 100));
    }

    public boolean hasPrintingPage() {
        if(this.printingIndex >= rechnungModelList.size()){
            return false;
        }

        return true;
    }

}