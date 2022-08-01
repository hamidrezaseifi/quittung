package de.seifi.rechnung_common.utils;

import de.seifi.rechnung_common.controllers.ControllerBase;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Window;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;

public interface IMainAppProvider {

    void setCurrentController(ControllerBase currentController);

    URL loadResource(String resourcePath) throws IOException;

    Pair<Parent, FXMLLoader> loadFXMLLoader(String fxml) throws IOException;

    Parent loadFXML(String fxml) throws IOException;

    Window getWindow();

    void showHome() throws IOException;


}
