package de.seifi.rechnung_manager_app;

import de.seifi.rechnung_manager_app.controllers.ControllerBse;
import de.seifi.rechnung_manager_app.controllers.MainController;
import de.seifi.rechnung_manager_app.ui.UiUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class RechnungManagerFxApp extends Application {

    private static Scene scene;

    private static MainController mainController;
    
    private static ControllerBse currentController;
    
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
        return RechnungManagerFxApp.class.getResource("styles/styles.css").toExternalForm();
    }

    private void closeWindowEvent(WindowEvent event) {
        if(isCurrentControllerDirty()){
            UiUtils.showError("Niche gespeicherte Änderungen ...", currentController.getDirtyMessage());

            event.consume();
            return;
        }

    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static Parent loadFXML(String fxml) throws IOException {
        URL fxmlResource = RechnungManagerFxApp.class.getResource("fxml/" + fxml + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlResource);
        return fxmlLoader.load();
    }

    public static Pair<Parent, FXMLLoader> loadFXMLLoader(String fxml) throws IOException {
        URL fxmlResource = RechnungManagerFxApp.class.getResource("fxml/" + fxml + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlResource);
        return new Pair(fxmlLoader.load(), fxmlLoader) ;
    }

    public static URL loadResource(String resourcePath) throws IOException {
        URL resourceUrl = RechnungManagerFxApp.class.getResource(resourcePath);
        return resourceUrl;
    }

    public static GridPane getRechnungPane() throws IOException {
    	
    	GridPane rechnungPane = (GridPane)loadFXML("rechnung");
    	return rechnungPane;
    }

    public static GridPane getHomePane() throws IOException {
    	/*if(homePane == null) {
    		homePane =  (GridPane)loadFXML("home");
    	}*/
    	GridPane homePane =  (GridPane)loadFXML("home");
    	return homePane;
    }

    public static GridPane getReportPane() throws IOException {
    	/*if(reportPane == null) {
    		reportPane =  (GridPane)loadFXML("report");
    	}*/
    	GridPane reportPane =  (GridPane)loadFXML("report");
    	return reportPane;
    }

    public static GridPane getAdminPane() throws IOException {
    	/*if(adminPane == null) {
    		adminPane =  (GridPane)loadFXML("admin");
    	}*/
    	GridPane adminPane =  (GridPane)loadFXML("admin");
    	return adminPane;
    }

    public static void main(String[] args) {
        launch();
    }

    public static MainController getMainController() {
        return mainController;
    }

    public static void setMainController(MainController mainController) {
        RechnungManagerFxApp.mainController = mainController;
    }

	public static ControllerBse getCurrentController() {
		return currentController;
	}

	public static boolean isCurrentControllerDirty() {
		return currentController != null? currentController.isDirty() : false;
	}

	public static void setCurrentController(ControllerBse currentController) {
		RechnungManagerFxApp.currentController = currentController;
	}
    
    
}