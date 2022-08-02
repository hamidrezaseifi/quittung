package de.seifi.rechnung_common.plugins;

import de.seifi.rechnung_common.controllers.ControllerBase;
import javafx.scene.layout.Pane;

public interface IViewLoader {

    void loadView(Pane view, ControllerBase controller);

}
