package de.seifi.quittung.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import de.seifi.quittung.QuittungApp;
import de.seifi.quittung.ui.FloatGeldLabel;
import de.seifi.quittung.ui.QuittungBindingViewModel;
import de.seifi.quittung.ui.QuittungItemProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.transform.Scale;

public class QuittungController implements Initializable, ControllerBse {

    @FXML private TableView<QuittungItemProperty> itemsTableView;

    @FXML private FloatGeldLabel lblNetto;

    @FXML private FloatGeldLabel lblMvst;

    @FXML private FloatGeldLabel lblGesamt;

    @FXML private TableColumn<QuittungItemProperty, String> produktColumn;

    @FXML private TableColumn<QuittungItemProperty, String> artikelNummerColumn;

    @FXML private TableColumn<QuittungItemProperty, Integer> mengeColumn;

    @FXML private TableColumn<QuittungItemProperty, Float> nPreisColumn;

    @FXML private TableColumn<QuittungItemProperty, Float> bPreisColumn;

    @FXML private TableColumn<QuittungItemProperty, Float> gesamtColumn;

    @FXML private GridPane rootPane;
    
    @FXML private Label lblQuittungNummer;
    
    @FXML private Label lblQuittungDatum;
    
    @FXML private Label lblLiferdatum;
    
    @FXML private Button btnSave;
    
    @FXML private Button btnPrint;


    private QuittungBindingViewModel quittungModel;

    @FXML
    private void speichern() throws IOException {
        //QuittungApp.setRoot("secondary");
        if(quittungModel.save()){

        }
    }

    @FXML
    private void reload() throws IOException {
        //QuittungApp.setRoot("secondary");
    	
    	if(canResetData()) {
    		doReloadData();
	        
    	}
    }

    private void doReloadData() {
    	itemsTableView.setItems(null);
    	quittungModel.reset();
        itemsTableView.setItems(quittungModel.getQuittungItems());
    }
    
    @FXML
    private void printQuittung() {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            Printer printer = job.getPrinter();
            PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);

            //PageLayout pageLayout = job.getPrinter().getDefaultPageLayout();
            job.showPrintDialog(null);
            //job.showPageSetupDialog(null);
            double w = rootPane.getWidth();
            double h = rootPane.getHeight();

            double scaleX = pageLayout.getPrintableWidth() / rootPane.getWidth();
            double scaleY = pageLayout.getPrintableHeight() / rootPane.getHeight();

            Scale scale = new Scale(scaleX, scaleY);

            rootPane.getTransforms().add(scale);
            boolean success = job.printPage(pageLayout, rootPane);
            if (success) {
                job.endJob();
            }

            rootPane.setPrefHeight(h);
            rootPane.setPrefWidth(w);
        }
    }

    @FXML
    private void closeQuittung() throws IOException {
    	if(canResetData()) {
    		doReloadData();
	        QuittungApp.getMainController().showHome();
    	}
    	
    }

    private boolean canResetData() {
    	if(quittungModel.isDirty()) {
    		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    	    alert.setTitle("Geändrte daten löschen ...");
    	    alert.setHeaderText("Die Daten von Quittung ist geändert. Soll die Anderungen gelöscht werden?");
    	    alert.setContentText(null);
    	    Optional<ButtonType> res = alert.showAndWait();
    	    if(res.isPresent() && res.get() == ButtonType.OK) {
    	    	return true;
    	    }
    	    return false;
    		
    	}
    	return true;
    }
    
    @Override
    public void initialize(URL url,
                           ResourceBundle resourceBundle) {
    	
    	QuittungApp.setCurrentController(this);

        quittungModel = new QuittungBindingViewModel(1.4f, 1.2f);

        produktColumn.prefWidthProperty().bind(
                itemsTableView.widthProperty().subtract(
                        artikelNummerColumn.widthProperty()).subtract(
                            mengeColumn.widthProperty()).subtract(
                                    bPreisColumn.widthProperty()).subtract(
                                            nPreisColumn.widthProperty()).subtract(
                                                    gesamtColumn.widthProperty()).subtract(5)
                                                                       );

        itemsTableView.setItems(quittungModel.getQuittungItems());

        mengeColumn.setOnEditCommit(event -> {
            final Integer value = event.getNewValue();
            QuittungItemProperty prop = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if(prop.getMenge() != value) {
            	prop.setMenge(value);

                quittungModel.calculateQuittungSumme();
                quittungModel.setDirty(true);
            }
            
        });

        bPreisColumn.setOnEditCommit(event -> {
            final Float value = event.getNewValue();
            QuittungItemProperty prop = event.getTableView().getItems().get(event.getTablePosition().getRow());
            
            if(prop.getBrutoPreis() != value) {
            	
            	prop.setBrutoPreis(value);
                prop.setPreis(quittungModel.calculateNettoPreis(value));
                
                quittungModel.calculateQuittungSumme();
                quittungModel.setDirty(true);
            }
            
        });

        produktColumn.setOnEditCommit(event -> {
            final String value = event.getNewValue();
            QuittungItemProperty prop = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if(prop.getProdukt() != value) {
                prop.setProdukt(value);
                quittungModel.calculateQuittungSumme();
                quittungModel.setDirty(true);

            }

        });

        artikelNummerColumn.setOnEditCommit(event -> {
            final String value = event.getNewValue();
            QuittungItemProperty prop = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if(prop.getArtikelNummer() != value) {
                prop.setArtikelNummer(value);
                quittungModel.calculateQuittungSumme();
                quittungModel.setDirty(true);

            }

        });

        lblNetto.valueProperty().bind(quittungModel.nettoSummeProperty());
        lblMvst.valueProperty().bind(quittungModel.mvstSummeProperty());
        lblGesamt.valueProperty().bind(quittungModel.gesamtSummeProperty());
        
        lblQuittungNummer.textProperty().bind(quittungModel.getQuittungNummerProperty());
        lblQuittungDatum.textProperty().bind(quittungModel.getQuittungDatumProperty());
        lblLiferdatum.textProperty().bind(quittungModel.getLiferDatumProperty());

        btnSave.disableProperty().bind(quittungModel.getDisableSaveProperty());
        btnPrint.disableProperty().bind(quittungModel.getDisablePrintProperty());
    }

	@Override
	public boolean isDirty() {
		
		return quittungModel.isDirty();
	}

    @Override
    public String getDirtyMessage() {
        return "Die Quittung-Daten ist geändert aber nicht gescpeichert!";
    }
}
