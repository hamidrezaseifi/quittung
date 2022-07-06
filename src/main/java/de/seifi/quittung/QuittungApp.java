package de.seifi.quittung;

import de.seifi.quittung.controllers.ControllerBse;
import de.seifi.quittung.controllers.MainController;
import de.seifi.quittung.db.DbConnection;
import de.seifi.quittung.exception.DataSqlException;
import de.seifi.quittung.ui.UiUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class QuittungApp extends Application {

    public static float BerechnenFaktorBasis = 1.4f;
    public static float BerechnenFaktorZiel = 1.2f;

    private static Scene scene;

    private static MainController mainController;
    
    private static ControllerBse currentController;
    

    @Override
    public void start(Stage stage) throws IOException {
        try {
            DbConnection.initialDbIfNotExists();
        } catch (DataSqlException e) {
            UiUtils.showError("Erstellen von Datenbank ...", e.getMessage());
        }

        scene = new Scene(loadFXML("main"));
        scene.getStylesheets().add(getClass().getResource("styles/styles.css").toExternalForm());
        stage.setScene(scene);

        scene.getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
        stage.show();
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
        URL fxmlResource = QuittungApp.class.getResource("fxml/" + fxml + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlResource);
        return fxmlLoader.load();
    }

    public static Pair<Parent, FXMLLoader> loadFXMLLoader(String fxml) throws IOException {
        URL fxmlResource = QuittungApp.class.getResource("fxml/" + fxml + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlResource);
        return new Pair(fxmlLoader.load(), fxmlLoader) ;
    }

    public static URL loadResource(String resourcePath) throws IOException {
        URL resourceUrl = QuittungApp.class.getResource(resourcePath);
        return resourceUrl;
    }

    public static GridPane getQuittungPane() throws IOException {
    	/*if(quittungPane == null) {
    		GridPane quittungPane =  (GridPane)loadFXML("quittung");
    	}*/
    	GridPane quittungPane =  (GridPane)loadFXML("quittung");
    	return quittungPane;
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
        QuittungApp.mainController = mainController;
    }

	public static ControllerBse getCurrentController() {
		return currentController;
	}

	public static boolean isCurrentControllerDirty() {
		return currentController != null? currentController.isDirty() : false;
	}

	public static void setCurrentController(ControllerBse currentController) {
		QuittungApp.currentController = currentController;
	}
    
    
}
