package de.seifi.quittung;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class QuittungApp extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("quittung"));
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        URL fxmlResource = QuittungApp.class.getResource(fxml + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlResource);
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}
