package de.seifi.rechnung_manager_app.controllers;

import de.seifi.rechnung_common.entities.CustomerFahrzeugScheinEntity;
import de.seifi.rechnung_common.repositories.CustomerFahrzeugScheinRepository;
import de.seifi.rechnung_manager_app.RechnungManagerFxApp;
import de.seifi.rechnung_manager_app.RechnungManagerSpringApp;
import de.seifi.rechnung_manager_app.models.CustomerFahrzeugScheinModel;
import de.seifi.rechnung_manager_app.models.CustomerModel;
import de.seifi.rechnung_manager_app.models.CustomerModelProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class AddCustomerFahrzeugscheinDialog extends Dialog<CustomerFahrzeugScheinModel> {

    @FXML private GridPane rootPane;

    @FXML private TextField txtCustomer;

    @FXML private TextField txtName;

    @FXML private Label lblFahrzeugscheinPath;

    @FXML private ButtonType okButtonType;

    @FXML private HBox imageSelectBox;

    @FXML private VBox existingImagesBox;

    private Button okButton;

    private final CustomerModel customerModel;

    private final CustomerFahrzeugScheinRepository fahrzeugScheinRepository;


    public AddCustomerFahrzeugscheinDialog(Window owner,
                                           CustomerModel customerModel,
                                           CustomerFahrzeugScheinRepository fahrzeugScheinRepository) throws IOException {

        this.customerModel = customerModel;
        this.fahrzeugScheinRepository = fahrzeugScheinRepository;

        FXMLLoader loader = RechnungManagerFxApp.getCustomerFahrzeugscheinDialog();
		
        loader.setController(this);

        DialogPane dialogPane = loader.load();

        this.okButton = (Button)dialogPane.lookupButton(okButtonType);
        this.okButton.addEventFilter(ActionEvent.ACTION,
                             event -> {
                                 if(lblFahrzeugscheinPath.getText().isBlank()){
                                     event.consume();
                                 }

                             });

        initOwner(owner);
        initModality(Modality.APPLICATION_MODAL);

        setResizable(true);
        setTitle("Customer Fahrzeugschein ...");
        setDialogPane(dialogPane);

		setResultConverter(buttonType -> {
            if( buttonType.getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE){
                return null;
            }
            CustomerFahrzeugScheinEntity entity = new CustomerFahrzeugScheinEntity(customerModel.getId(),
                                                                                   txtName.getText());
            byte[] data = null;
            try {
                data = Files.readAllBytes(Paths.get(lblFahrzeugscheinPath.getText()));
                entity.setImageBytes(data);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            this.fahrzeugScheinRepository.save(entity);


            return new CustomerFahrzeugScheinModel(entity);

		});

        txtCustomer.setText(customerModel.getCustomerName());

        List<CustomerFahrzeugScheinEntity> fsList = this.fahrzeugScheinRepository.findAllByCustomerId(customerModel.getId());

        int newid = 1;
        for(CustomerFahrzeugScheinEntity entity: fsList){
            newid = getNewid(entity);
            addNewImageBox(new CustomerFahrzeugScheinModel(entity));
        }

        String newName = "Fahrzeugschein-" + newid;
        txtName.setText(newName);


        lblFahrzeugscheinPath.prefWidthProperty().bind(imageSelectBox.widthProperty().subtract(30));


        validateModel();

        txtName.textProperty().addListener((obs, oldValue, newValue) -> {
            validateModel();
        });


    }

    private void addNewImageBox(CustomerFahrzeugScheinModel model) {
        HBox existingImageBox = new HBox();
        existingImageBox.setSpacing(5);
        existingImageBox.setStyle("-fx-background-color: #EEFFFF; -fx-padding: 3 3 3 3;");
        Label lblName = new Label(model.getName());
        lblName.setPrefHeight(70);
        lblName.setPrefWidth(120);
        lblName.setAlignment(Pos.CENTER_LEFT);
        Image img = new Image(new ByteArrayInputStream(model.getImageBytes()));
        ImageView imgView = new ImageView();
        imgView.setImage(img);
        imgView.setFitWidth(100);
        imgView.setFitHeight(70);
        imgView.setUserData(model);
        imgView.setOnMouseClicked((event) -> {

            ImageView imgPreview = new ImageView();
            imgPreview.setImage(img);

            Dialog showImageDialog = new Dialog<>();
            showImageDialog.setTitle("Bildbetracht ...");
            showImageDialog.setResizable(true);
            showImageDialog.getDialogPane().setContent(imgPreview);
            imgPreview.fitWidthProperty().bind(showImageDialog.widthProperty().subtract(30));
            imgPreview.fitHeightProperty().bind(showImageDialog.heightProperty().subtract(80));
            ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            showImageDialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
            showImageDialog.setWidth(800);
            showImageDialog.setHeight(600);

            showImageDialog.showAndWait();
        });

        existingImageBox.getChildren().addAll(lblName, imgView);
        existingImageBox.setUserData(model);
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

        if(lblFahrzeugscheinPath.getText().isBlank()){
            this.okButton.setDisable(true);
            return;
        }

        File file = new File(lblFahrzeugscheinPath.getText());
        if(!file.exists()){
            this.okButton.setDisable(true);
            return;
        }


        this.okButton.setDisable(false);

    }
}
