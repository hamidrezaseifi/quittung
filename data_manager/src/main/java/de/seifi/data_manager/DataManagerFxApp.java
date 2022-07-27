package de.seifi.data_manager;

import de.seifi.data_manager.utils.AppConfig;
import de.seifi.rechnung_common.utils.ISingleInstanceRunnable;
import de.seifi.rechnung_common.utils.RunSingleInstance;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;

public class DataManagerFxApp extends Application implements ISingleInstanceRunnable {

	public static final AppConfig appConfig = new AppConfig();
	
	
    private static Scene scene;

    public static Stage mainStage;
    
    public static void closeApp() {
    	mainStage.close();
    }

    @Override
    public void runInstance(String[] args) {
        Application.launch(args);
    }

    public static void main(String[] args) {
        RunSingleInstance.runInstance(args, new DataManagerFxApp(), "DataManagerFxAppId");

    }

    @Override
    public void start(Stage stage) throws IOException {

        DataManagerFxApp.mainStage = stage;
        scene = new Scene(loadFXML("main"));
        scene.getStylesheets().add(getMainStyle());
        stage.setScene(scene);

        scene.getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
        stage.show();
    }

    public static String getMainStyle() {
        return DataManagerFxApp.class.getResource("styles/styles.css").toExternalForm();
    }

    private void closeWindowEvent(WindowEvent event) {
        
    }

    public static FXMLLoader loadFXMLLoader(String fxml) throws IOException {
        URL fxmlResource = DataManagerFxApp.class.getResource("fxml/" + fxml + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlResource);
        return fxmlLoader;
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = loadFXMLLoader(fxml);
        return fxmlLoader.load();
    }

    public static void showError(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText(null);
        alert.showAndWait();

    }
}
