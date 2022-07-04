package de.seifi.quittung;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class QuittungApp extends Application {

    private static Scene scene;
    
    private static GridPane homePane = null;
    private static GridPane quittungPane = null;
    private static GridPane reportPane = null;
    private static GridPane adminPane = null;
    

    @Override
    public void start(Stage stage) throws IOException {
    	
        scene = new Scene(loadFXML("main"));
        scene.getStylesheets().add(getClass().getResource("styles/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static Parent loadFXML(String fxml) throws IOException {
        URL fxmlResource = QuittungApp.class.getResource("fxml/" + fxml + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlResource);
        return fxmlLoader.load();
    }

    public static URL loadResource(String resourcePath) throws IOException {
        URL resourceUrl = QuittungApp.class.getResource(resourcePath);
        return resourceUrl;
    }

    public static GridPane getQuittungPane() throws IOException {
    	if(quittungPane == null) {
    		quittungPane =  (GridPane)loadFXML("quittung");
    	}
    	
    	return quittungPane;
    }

    public static GridPane getHomePane() throws IOException {
    	if(homePane == null) {
    		homePane =  (GridPane)loadFXML("home");
    	}
    	
    	return homePane;
    }

    public static GridPane getReportPane() throws IOException {
    	if(reportPane == null) {
    		reportPane =  (GridPane)loadFXML("report");
    	}
    	
    	return reportPane;
    }

    public static GridPane getAdminPane() throws IOException {
    	if(adminPane == null) {
    		adminPane =  (GridPane)loadFXML("admin");
    	}
    	
    	return adminPane;
    }
    
    public static void main(String[] args) {
        launch();
    }

}
