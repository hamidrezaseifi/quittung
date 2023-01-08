package de.seifi.rechnung_manager_app.controllers;

import de.seifi.rechnung_common.entities.CustomerFahrzeugScheinEntity;
import de.seifi.rechnung_common.repositories.CustomerFahrzeugScheinRepository;
import de.seifi.rechnung_manager_app.RechnungManagerFxApp;
import de.seifi.rechnung_manager_app.models.CustomerFahrzeugScheinModel;
import de.seifi.rechnung_manager_app.models.CustomerModel;
import de.seifi.rechnung_manager_app.ui.UiUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class SelectCustomerFahrzeugscheinDialog extends Dialog<CustomerFahrzeugScheinModel> {
    private static final Logger logger = LoggerFactory.getLogger(SelectCustomerFahrzeugscheinDialog.class);

    @FXML private GridPane rootPane;

    @FXML private TextField txtCustomer;

    @FXML private TextField txtName;

    @FXML private Label lblFahrzeugscheinPath;

    @FXML private ButtonType okButtonType;

    @FXML private HBox imageSelectBox;

    @FXML private VBox existingImagesBox;

    @FXML private TabPane tabPane;

    private Button okButton;

    private final CustomerModel customerModel;

    private final ToggleGroup selectGroup;

    private final CustomerFahrzeugScheinRepository fahrzeugScheinRepository;

    private CustomerFahrzeugScheinModel selectedModel;

    public SelectCustomerFahrzeugscheinDialog(Window owner,
                                              CustomerModel customerModel,
                                              CustomerFahrzeugScheinModel selectedModel,
                                              CustomerFahrzeugScheinRepository fahrzeugScheinRepository) throws IOException {

        this.customerModel = customerModel;
        this.fahrzeugScheinRepository = fahrzeugScheinRepository;
        this.selectedModel = selectedModel;

        FXMLLoader loader = RechnungManagerFxApp.getCustomerFahrzeugscheinDialog();
		
        loader.setController(this);

        DialogPane dialogPane = loader.load();

        this.okButton = (Button)dialogPane.lookupButton(okButtonType);
        this.okButton.addEventFilter(ActionEvent.ACTION,
                             event -> {
                                 if(!isDataValid()){
                                     event.consume();
                                 }

                             });

        initOwner(owner);
        initModality(Modality.APPLICATION_MODAL);

        setResizable(true);
        setTitle("Customer Fahrzeugschein ...");
        setDialogPane(dialogPane);

        txtCustomer.setText(customerModel.getCustomerName());

        this.selectGroup = new ToggleGroup();
        this.selectGroup.selectedToggleProperty().addListener((arg, oldVal, newVal) ->{
            validateModel();

        });

        List<CustomerFahrzeugScheinEntity> fsList = this.fahrzeugScheinRepository.findAllByCustomerId(customerModel.getId());

        int newid = 1;

        for(CustomerFahrzeugScheinEntity entity: fsList){
            newid = getNewid(entity);
            addNewImageBox(new CustomerFahrzeugScheinModel(entity));
        }

        String newName = "Fahrzeugschein-" + newid;
        txtName.setText(newName);


        lblFahrzeugscheinPath.prefWidthProperty().bind(imageSelectBox.widthProperty().subtract(30));

        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            validateModel();
        });

        txtName.textProperty().addListener((obs, oldValue, newValue) -> {
            validateModel();
        });


        setResultConverter(buttonType -> {
            if( buttonType.getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE){
                return null;
            }

            CustomerFahrzeugScheinModel resultModel = null;
            if(isNewTab()){
                CustomerFahrzeugScheinEntity entity = new CustomerFahrzeugScheinEntity(customerModel.getId(),
                                                                                       txtName.getText());
                byte[] data = null;
                String filePath = lblFahrzeugscheinPath.getText();
                try {
                    data = Files.readAllBytes(Paths.get(filePath));
                    entity.setImageBytes(data);
                } catch (IOException e) {
                    logger.error("Fehler bei lesen '" + filePath + "': " + e.getLocalizedMessage(), e);
                    UiUtils.showError("Fehler beim lesen von Datei!",
                                      "Fehler beim Lesen von Datei '" + filePath + "': " + e.getLocalizedMessage());
                    throw new RuntimeException(e);
                }

                try {
                    this.fahrzeugScheinRepository.save(entity);
                }
                catch (Exception e){
                    logger.error("Fehler bei Speichern Daten in Datenbank: " + e.getLocalizedMessage(), e);
                    UiUtils.showError("Fehler bei Speichern Daten",
                                      "Fehler bei Speichern Daten in Datenbank: " + e.getLocalizedMessage());
                    throw new RuntimeException(e);

                }


                resultModel = new CustomerFahrzeugScheinModel(entity);
            }
            if(isExistingTab()){
                resultModel = (CustomerFahrzeugScheinModel)this.selectGroup.getSelectedToggle().getUserData();

            }

            return resultModel;
        });

        validateModel();

    }

    private void addNewImageBox(CustomerFahrzeugScheinModel model) {
        HBox existingImageBox = new HBox();
        existingImageBox.setSpacing(5);
        existingImageBox.setStyle("-fx-background-color: #EEFFFF; -fx-padding: 3 3 3 3;");
        RadioButton rbName = new RadioButton(model.getName());
        rbName.setPrefHeight(70);
        rbName.setPrefWidth(120);
        rbName.setAlignment(Pos.CENTER_LEFT);
        rbName.setToggleGroup(this.selectGroup);
        rbName.setUserData(model);
        if(this.selectedModel != null && selectedModel.getId().equals(model.getId())){

            rbName.setSelected(true);
        }
        Image img = new Image(new ByteArrayInputStream(model.getImageBytes()));
        ImageView imgView = new ImageView();
        imgView.setImage(img);
        imgView.setFitWidth(100);
        imgView.setFitHeight(70);
        imgView.setCursor(Cursor.HAND);
        imgView.setOnMouseClicked((event) -> {

            Dialog showImageDialog = UiUtils.createImageViewDialog(img);

            showImageDialog.showAndWait();
        });

        existingImageBox.getChildren().addAll(rbName, imgView);
        existingImagesBox.getChildren().add(existingImageBox);
    }

    private int getNewid(CustomerFahrzeugScheinEntity entity) {
        int newid;
        String entityName = entity.getName();
        String lastIdStr = entityName.split("-")[1];
        int lastId = Integer.parseInt(lastIdStr);
        newid = lastId + 1;
        return newid;
    }

    @FXML
    private void selectImage(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Fahrzeugschein auswählen ...");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
                                                );
        File file = fileChooser.showOpenDialog(getOwner());
        if (file != null) {
            lblFahrzeugscheinPath.setText(file.getAbsolutePath());
            validateModel();
        }
    }

    private void validateModel(){
        this.okButton.setDisable(!isDataValid());
    }

    private boolean isDataValid(){
        if(isNewTab()){
            if(lblFahrzeugscheinPath.getText().isBlank()){
                return false;
            }

            File file = new File(lblFahrzeugscheinPath.getText());
            if(!file.exists()){
                return false;
            }
        }
        if(isExistingTab()){
            if(this.selectGroup.getSelectedToggle() == null){
                return false;
            }
        }

        return true;
    }

    private boolean isExistingTab() {
        return tabPane.getSelectionModel().getSelectedIndex() == 0;
    }

    private boolean isNewTab() {
        return tabPane.getSelectionModel().getSelectedIndex() == 1;
    }
}
