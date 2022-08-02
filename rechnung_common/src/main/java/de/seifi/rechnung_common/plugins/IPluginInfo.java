package de.seifi.rechnung_common.plugins;

import javafx.scene.control.Menu;

import java.util.Set;

public interface IPluginInfo {

    String getPluginName();

    Set<String> getPluginViewKeys(String key);

    Menu getTopMenu();
}
