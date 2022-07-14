package de.seifi.db_connection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;

public class DbConnectionFxApp extends Application {

    private static Scene scene;
    
    public static Window getWindow() {
    	return scene.getWindow();
    }

    @Override
    public void start(Stage stage) throws IOException {
        
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

    public static Parent loadFXML(String fxml) throws IOException {
        URL fxmlResource = DbConnectionFxApp.class.getResource("fxml/" + fxml + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlResource);
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    
    
    
}