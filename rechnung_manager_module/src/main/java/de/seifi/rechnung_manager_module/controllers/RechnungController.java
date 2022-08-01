package de.seifi.rechnung_manager_module.controllers;

import de.seifi.rechnung_common.controllers.ControllerBase;
import de.seifi.rechnung_common.repositories.RechnungRepository;
import de.seifi.rechnung_common.utils.RepositoryProvider;
import de.seifi.rechnung_common.utils.UiUtils;
import de.seifi.rechnung_manager_module.data_service.IRechnungDataHelper;
import de.seifi.rechnung_manager_module.data_service.impl.RechnungDataHelper;
import de.seifi.rechnung_manager_module.enums.RechnungType;
import de.seifi.rechnung_manager_module.fx_services.RechnungBindingService;
import de.seifi.rechnung_manager_module.models.CustomerModel;
import de.seifi.rechnung_manager_module.models.RechnungItemProperty;
import de.seifi.rechnung_manager_module.models.RechnungModel;
import de.seifi.rechnung_manager_module.ui.FloatGeldLabel;
import de.seifi.rechnung_manager_module.ui.RechnungManagerUiUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

public class RechnungController implements Initializable, ControllerBase {

    @FXML private TableView<RechnungItemProperty> showItemsTableView;

    @FXML private FloatGeldLabel lblNetto;

    @FXML private FloatGeldLabel lblMvst;

    @FXML private FloatGeldLabel lblGesamt;

    @FXML private TableColumn<RechnungItemProperty, String> produktColumn;

    @FXML private TableColumn<RechnungItemProperty, String> artikelNummerColumn;

    @FXML private TableColumn<RechnungItemProperty, Integer> mengeColumn;

    @FXML private TableColumn<RechnungItemProperty, Float> nPreisColumn;

    @FXML private TableColumn<RechnungItemProperty, Float> bPreisColumn;

    @FXML private TableColumn<RechnungItemProperty, Float> gesamtColumn;

    @FXML private GridPane rootPane;

    @FXML private Label lblQuittung;
    
    @FXML private Label lblRechnungNummer;

    @FXML private Label lblRechnungDatum;

    @FXML private Label lblLiferdatum;

    @FXML private Button btnSave;

    @FXML private Button btnReset;

    @FXML private Button btnClose;

    @FXML private Button btnSelectCsutomer;

    @FXML private Button btnPrint;

    @FXML private GridPane bannerPane;

    @FXML private Label lblName;

    @FXML private Label lblStreet;

    @FXML private Label lblPlz;

    @FXML private Label lblCity;

    @FXML private Label lblHaus;

    @FXML private Label lblAddress2;

    @FXML private HBox toggleStatusBox;

    @FXML private Label lblStatusChange;

    @FXML private HBox nameBox;


    private RechnungBindingService rechnungBindingService;

    private final RechnungRepository rechnungRepository;

    private final IRechnungDataHelper rechnungDataHelper;

    private Stage stage;

    private final RechnungType rechnungType;

    public RechnungController(RechnungType rechnungType) {

        this.rechnungType = rechnungType;
        this.rechnungRepository = RepositoryProvider.getRechnungRepository();
        this.rechnungDataHelper = new RechnungDataHelper();

	}

    @FXML
    private void toggleBrerechnenZiel(){
        rechnungBindingService.toggleActiveBerechnenZiel();
    }

	@FXML
    private void speichern() throws IOException {
		showItemsTableView.setEditable(false);
		showItemsTableView.edit(-1, null);
		showItemsTableView.setDisable(true);
		
		showItemsTableView.setItems(null);
		
        if(rechnungBindingService.save()){

        }
		
		showItemsTableView.setItems(rechnungBindingService.getRechnungItems());
        showItemsTableView.setEditable(true);
        showItemsTableView.setDisable(false);
    }

    @FXML
    private void reload() throws IOException {

    	if(canResetData()) {
    		doReloadData();
    	}
    }

    @FXML
    private void selectCustomer() throws IOException {
    	//rechnungBindingService.test();
    	
    	SelectCustomerDialog dialog = new SelectCustomerDialog(stage, this.rechnungBindingService.getCustomerModel());

        Optional<CustomerModel> result = dialog.showAndWait();
        if(result.isPresent()){

            Platform.runLater(()->{
                CustomerModel model = result.get();
                rechnungBindingService.setCustomerModel(model);
            });
        }

    }
    
    
    private void doReloadData() {
    	showItemsTableView.setItems(null);
    	rechnungBindingService.reset();
        showItemsTableView.setItems(rechnungBindingService.getRechnungItems());
    }
    
    @FXML
    private void printRechnung() throws IOException {


    	showItemsTableView.setEditable(false);
    	showItemsTableView.edit(-1, null);

        RechnungManagerUiUtils.printRechnungItems(Arrays.asList(rechnungBindingService.getRechnungSavingModel()),
                                                  showItemsTableView.getScene().getWindow());
        showItemsTableView.setEditable(true);

    }

    @FXML
    private void closeRechnung() throws IOException {
        if(rechnungBindingService.isView()){
            this.stage.close();
            return;
        }
    	if(canResetData()) {
    		doReloadData();
            UiUtils.getMainAppProvider().showHome();
    	}
    	
    }

    private boolean canResetData() {
    	if(rechnungBindingService.isDirty()) {
    		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    	    alert.setTitle("Geändrte daten löschen ...");
    	    alert.setHeaderText("Die Daten von Rechnung ist geändert. Soll die Anderungen gelöscht werden?");
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

        UiUtils.getMainAppProvider().setCurrentController(this);

        rechnungBindingService = new RechnungBindingService(this.rechnungType,
                                                            this.rechnungRepository,
                                                            this.rechnungDataHelper);


        bannerPane.styleProperty().bind(rechnungBindingService.bannerBackColorProperty());

        produktColumn.prefWidthProperty().bind(
                showItemsTableView.widthProperty().subtract(
                        artikelNummerColumn.widthProperty()).subtract(
                            mengeColumn.widthProperty()).subtract(
                                    bPreisColumn.widthProperty()).subtract(
                                            nPreisColumn.widthProperty()).subtract(
                                                    gesamtColumn.widthProperty()).subtract(5)
                                                                       );

        showItemsTableView.setItems(rechnungBindingService.getRechnungItems());
        showItemsTableView.setUserData(rechnungBindingService);

        mengeColumn.setOnEditCommit(event -> {
            final Integer value = event.getNewValue();
            TablePosition<RechnungItemProperty,?> tPos = event.getTablePosition();
            rechnungBindingService.setNewMengeValue(tPos.getRow(), value);
        });

        bPreisColumn.setOnEditCommit(event -> {
            final Float value = event.getNewValue();
            TablePosition<RechnungItemProperty,?> tPos = event.getTablePosition();
            rechnungBindingService.setNewBrutoPreisValue(tPos.getRow(), value);
            rechnungBindingService.setVisbleToggleStatusBox(false);
        });

        produktColumn.setOnEditCommit(event -> {
            final String value = event.getNewValue();
            TablePosition<RechnungItemProperty,?> tPos = event.getTablePosition();
            rechnungBindingService.setNewProduktValue(tPos.getRow(), value);
        });

        artikelNummerColumn.setOnEditCommit(event -> {
            final String value = event.getNewValue();
            TablePosition<RechnungItemProperty,?> tPos = event.getTablePosition();
            rechnungBindingService.setNewArtikelNummerValue(tPos.getRow(), value);

        });

        lblNetto.valueProperty().bind(rechnungBindingService.nettoSummeProperty());
        lblMvst.valueProperty().bind(rechnungBindingService.mvstSummeProperty());
        lblGesamt.valueProperty().bind(rechnungBindingService.gesamtSummeProperty());
        
        lblRechnungNummer.textProperty().bind(rechnungBindingService.getRechnungNummerProperty());
        lblRechnungDatum.textProperty().bind(rechnungBindingService.getRechnungDatumProperty());
        lblLiferdatum.textProperty().bind(rechnungBindingService.getLiferDatumProperty());

        btnSave.disableProperty().bind(rechnungBindingService.getDisableSaveProperty());
        btnPrint.disableProperty().bind(rechnungBindingService.getDisablePrintProperty());
        toggleStatusBox.visibleProperty().bind(rechnungBindingService.getVisbleToggleStatusBox());

        if(this.rechnungType == RechnungType.QUITTUNG){
        	
        	for(Node child: bannerPane.getChildren()) {
        		if(GridPane.getColumnIndex(child) == 1 || GridPane.getColumnIndex(child) == 2) {
        			child.setVisible(false);
        		}
        		
        	}
            
        	lblQuittung.setVisible(true);
            //toggleStatusBox.setVisible(true);
        }

        if(this.rechnungType == RechnungType.RECHNUNG){

            lblName.textProperty().bind(rechnungBindingService.getCustomerModelProperty().getCustomerName());
            lblStreet.textProperty().bind(rechnungBindingService.getCustomerModelProperty().getStreet());
            lblPlz.textProperty().bind(rechnungBindingService.getCustomerModelProperty().getPlz());
            lblAddress2.textProperty().bind(rechnungBindingService.getCustomerModelProperty().getAddress2());
            lblCity.textProperty().bind(rechnungBindingService.getCustomerModelProperty().getCity());
            lblHaus.textProperty().bind(rechnungBindingService.getCustomerModelProperty().getHouseNumber());

            //toggleStatusBox.setVisible(false);
            lblQuittung.setVisible(false);
        }

    }

	@Override
	public boolean isDirty() {
		
		return rechnungBindingService.isDirty();
	}

    @Override
    public String getDirtyMessage() {
        return "Die Rechnung-Daten ist geändert aber nicht gescpeichert!";
    }

	public void loadModel(RechnungModel rechnungModel, boolean editable, Stage stage) {


		showItemsTableView.setEditable(editable);
		rechnungBindingService.setRechnungModel(rechnungModel);
		
		showItemsTableView.getColumns().forEach(c -> c.setEditable(editable));
        rechnungBindingService.setIsView(!editable);
        this.stage = stage;

		if(!editable) {
            Pane parent = (Pane)btnReset.getParent();
            parent.getChildren().remove(btnSave);
            parent.getChildren().remove(btnReset);

            parent = (Pane)toggleStatusBox.getParent();
            parent.getChildren().remove(toggleStatusBox);

			//btnSave.setVisible(false);
			//btnReset.setVisible(false);
            //btnSelectCsutomer.setVisible(false);

            if(this.rechnungType == RechnungType.RECHNUNG){
                //lblName.setCursor(Cursor.DEFAULT);
                lblName.prefWidthProperty().bind(nameBox.widthProperty());
                nameBox.getChildren().remove(btnSelectCsutomer);
            }

            if(this.rechnungType == RechnungType.QUITTUNG){
            	for(Node child: bannerPane.getChildren()) {
            		if(GridPane.getColumnIndex(child) == 1 || GridPane.getColumnIndex(child) == 2) {
            			child.setVisible(false);
            		}
            		
            	}

            }


        }
	}

}
