package de.seifi.db_connection;

import de.seifi.db_connection.utils.AppConfig;
import de.seifi.rechnung_common.utils.ISingleInstanceRunnable;
import de.seifi.rechnung_common.utils.RunSingleInstance;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;

import it.sauronsoftware.junique.AlreadyLockedException;
import it.sauronsoftware.junique.JUnique;
import it.sauronsoftware.junique.MessageHandler;

public class DbConnectionFxApp extends Application implements ISingleInstanceRunnable {

	public static final AppConfig appConfig = new AppConfig();
	
	
    private static Scene scene;
    
    private static Stage stage;
    
    public static void closeApp() {
    	stage.close();
    }

    @Override
    public void runInstance(String[] args) {
        Application.launch(args);
    }

    public static void main(String[] args) {
        RunSingleInstance.runInstance(args, new DbConnectionFxApp(), "DbConnectionFxAppId");

    }

    @Override
    public void start(Stage stage) throws IOException {
        
    	DbConnectionFxApp.stage = stage;
        scene = new Scene(loadFXML("main"));
        scene.getStylesheets().add(getMainStyle());
        stage.setScene(scene);

        scene.getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
        stage.show();
    }

    public static String getMainStyle() {
        return DbConnectionFxApp.class.getResource("styles/styles.css").toExternalForm();
    }

    private void closeWindowEvent(WindowEvent event) {
        
    }

    public static FXMLLoader loadFXMLLoader(String fxml) throws IOException {
        URL fxmlResource = DbConnectionFxApp.class.getResource("fxml/" + fxml + ".fxml");
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
