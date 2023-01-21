package de.seifi.rechnung_manager_app.ui;

import java.io.IOException;
import java.util.Optional;

import de.seifi.rechnung_manager_app.RechnungManagerFxApp;
import de.seifi.rechnung_manager_app.controllers.KostenvoranschlagController;
import de.seifi.rechnung_manager_app.controllers.RechnungController;
import de.seifi.rechnung_manager_app.models.KostenvoranschlagModel;
import de.seifi.rechnung_manager_app.models.RechnungModel;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UiUtils {
	private static final Logger logger = LoggerFactory.getLogger(UiUtils.class);

	public static void showError(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText(null);
        alert.showAndWait();
    }

    public static Optional<ButtonType> showAsking(String title, String message){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText(null);
        return alert.showAndWait();
    }

    public static void showRechnungViewDialog(RechnungModel rechnungModel) {
    	try {
			Stage stage = new Stage();
			FXMLLoader fxmlLoader = RechnungManagerFxApp.getRechnungFxmlLoader();
			RechnungController controller = new RechnungController(rechnungModel.getRechnungType());
			fxmlLoader.setController(controller);

			GridPane rechnungPane = fxmlLoader.load();
			controller.startView(rechnungModel, stage);
	        rechnungPane.setPadding(new Insets(10));
	        
	        Scene scene = new Scene(rechnungPane, 1200, 1000);
			scene.getStylesheets().add(RechnungManagerFxApp.getMainStyle());

	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.setScene(scene);
	        stage.setTitle(String.format("Rechnung Nummer %s Ansehen", rechnungModel.getNummer()));
	        stage.showAndWait();

	        
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	public static void showKostenvoranschlagViewDialog(KostenvoranschlagModel kostenvoranschlagModel) {
		try {
			Stage stage = new Stage();
			FXMLLoader fxmlLoader = RechnungManagerFxApp.getKostenvoranschlagFxmlLoader();
			GridPane rechnungPane = fxmlLoader.load();

			KostenvoranschlagController controller = fxmlLoader.getController();
			controller.startView(kostenvoranschlagModel, stage);
			rechnungPane.setPadding(new Insets(10));

			Scene scene = new Scene(rechnungPane, 1200, 1000);
			scene.getStylesheets().add(RechnungManagerFxApp.getMainStyle());

			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(scene);
			stage.setTitle(String.format("Kostenvoranschlag Nummer %s Ansehen", kostenvoranschlagModel.getNummer()));
			stage.showAndWait();


		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Dialog createImageViewDialog(Image img) {
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
		return showImageDialog;
	}

}
