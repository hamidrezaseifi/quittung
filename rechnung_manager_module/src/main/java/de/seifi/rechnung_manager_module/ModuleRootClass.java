package de.seifi.rechnung_manager_module;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;

public class ModuleRootClass {

    public static String loadImageResource(String resource) {
        URL resourceUrl = ModuleRootClass.class.getResource("images/" + resource);

        return resourceUrl.toExternalForm();
    }
    public static String loadStyleResource(String resource) {
        URL resourceUrl = ModuleRootClass.class.getResource("styles/" + resource + ".css");

        return resourceUrl.toExternalForm();
    }

    public static FXMLLoader loadFxmlLoader(String fxml) {
        URL fxmlResource = ModuleRootClass.class.getResource("fxml/" + fxml + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlResource);
        return fxmlLoader;
    }

    public static Pane loadPane(String fxml) throws IOException {
        URL fxmlResource = ModuleRootClass.class.getResource("fxml/" + fxml + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlResource);
        Pane pane = fxmlLoader.load();
        return pane;
    }

}
