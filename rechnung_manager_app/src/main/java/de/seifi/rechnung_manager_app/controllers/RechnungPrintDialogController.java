package de.seifi.rechnung_manager_app.controllers;


import de.seifi.rechnung_manager_app.models.RechnungItemPrintProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.*;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import de.seifi.rechnung_manager_app.fx_services.RechnungBindingPrintService;
import de.seifi.rechnung_manager_app.models.RechnungModel;
import de.seifi.rechnung_manager_app.ui.FloatGeldLabel;

public class RechnungPrintDialogController extends PrintDialogController<RechnungModel> implements Initializable {

    @FXML private FloatGeldLabel lblNetto;

    @FXML private FloatGeldLabel lblMvst;

    @FXML private FloatGeldLabel lblGesamt;

    @FXML private TableView<RechnungItemPrintProperty> printTableView;

    @FXML private GridPane rootPane;

    @FXML private Label lblRechnungNummer;

    @FXML private Label lblRechnungDatum;

    @FXML private Label lblLiferdatum;

    private RechnungBindingPrintService rechnungBindingService;

    @Override
    public void initialize(URL url,
                           ResourceBundle resourceBundle) {
        rechnungBindingService = new RechnungBindingPrintService();

    }


    @Override
    protected boolean startPrint(PrinterJob job, PageLayout pageLayout) {
    	
    	if(rechnungBindingService.hasMorePrintingPage()) {
    		printTableView.setItems(rechnungBindingService.getRechnungPrintItems());
            lblNetto.setText(rechnungBindingService.getNettoSumme());
            lblMvst.setText(rechnungBindingService.getMvstSumme());
            lblGesamt.setText(rechnungBindingService.getGesamtSumme());

            lblRechnungNummer.setText(rechnungBindingService.getRechnungNummer());
            lblRechnungDatum.setText(rechnungBindingService.getLiferDatum());
            lblLiferdatum.setText(rechnungBindingService.getLiferDatum());

            boolean success = job.printPage(pageLayout, rootPane);
            
            return success;
    	}
        return false;

    }


	@Override
	protected void setModelList(List<RechnungModel> modelList) {
		this.rechnungBindingService.setRechnungModelList(modelList);
	}


	@Override
	protected void setPrintingIndex(int index) {
		this.rechnungBindingService.setPrintingIndex(index);
	}


	@Override
	protected boolean hasPrintingPage() {
		return this.rechnungBindingService.hasPrintingPage();
	}


	@Override
	protected boolean increasePrintingIndex() {
		return this.rechnungBindingService.increasePrintingIndex();
	}


	@Override
	protected boolean hasMorePrintingPage() {
		
		return this.rechnungBindingService.hasMorePrintingPage();
	}


	@Override
	protected boolean increateIfMorePrintingPage() {

		return this.rechnungBindingService.increateIfMorePrintingPage();
	}


	@Override
	protected GridPane getRootPane() {
		return this.rootPane;
	}

}
