package de.seifi.rechnung_manager_module.fx_services;

import de.seifi.rechnung_manager_module.models.RechnungItemModel;
import de.seifi.rechnung_manager_module.models.CustomerModel;
import de.seifi.rechnung_manager_module.models.RechnungItemPrintProperty;
import de.seifi.rechnung_manager_module.models.RechnungModel;
import de.seifi.rechnung_manager_module.ui.TableUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class RechnungBindingPrintService {

    private static int MAX_PRINT_LIST_ITEMS = 17;

    private String gesamtSumme;
    private String nettoSumme;
    private String mvstSumme;
    private RechnungModel rechnungModel = null;
    private CustomerModel customerModel = null;
    
    ObservableList<RechnungItemPrintProperty> printPropertyList;

    private int printingItemPageIndex = 0;
    private int printingItemPageCount = 0;

    public RechnungBindingPrintService() {

    }


    public void setRechnungModel(RechnungModel model,
                                 CustomerModel customerModel) {
        this.rechnungModel = model;
        this.customerModel = customerModel;
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