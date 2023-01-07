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
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Window;

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

    @FXML private HBox imageBox;

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
        setTitle("Customer Fahrzeugschein hinzufügen...");
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
        if(!fsList.isEmpty()){

            String lastName = fsList.get(fsList.size() - 1).getName();
            String lastIdStr = lastName.split("-")[1];
            int lastId = Integer.parseInt(lastIdStr);
            newid = lastId + 1;
        }
        String newName = "Fahrzeugschein-" + newid;
        txtName.setText(newName);


        lblFahrzeugscheinPath.prefWidthProperty().bind(imageBox.widthProperty().subtract(30));


        validateModel();

        txtName.textProperty().addListener((obs, oldValue, newValue) -> {
            validateModel();
        });


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
