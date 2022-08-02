package de.seifi.container_manager_app.controllers;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import de.seifi.container_manager_app.ContainerManagerFxApp;
import de.seifi.container_manager_app.plugin.PluginItem;
import de.seifi.container_manager_app.plugin.PluginLoader;
import de.seifi.rechnung_common.controllers.ControllerBase;
import de.seifi.rechnung_common.plugins.IViewLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.layout.*;


public class MainController implements Initializable, IViewLoader {

	@FXML private MenuBar menuBar;
	@FXML private Menu mnuFile;
	@FXML private Menu menuAdmin;

	@FXML private GridPane childBox;

	@FXML private Button btnShowModule;


	private ContextMenu contextMenu = new ContextMenu();

	private PluginLoader pluginLoader = new PluginLoader(this);

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		ContainerManagerFxApp.setMainController(this);

		try {
			pluginLoader.reloadPluginList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		for(String key: pluginLoader.getPluginKeys()){
			PluginItem plugin = pluginLoader.getPlugin(key);

			Menu topMenu = plugin.getTopMenu();
			if(topMenu != null){
				menuBar.getMenus().add(menuBar.getMenus().size() - 1, topMenu);
			}
			MenuItem menuItem = new MenuItem(plugin.getTitle());
			contextMenu.getItems().add(menuItem);
		}


		try {
			showHome();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	@FXML
	public void showHome() throws IOException {

		if(ContainerManagerFxApp.cannCurrentControllerClosed()) {
			clearChildren();

			childBox.getChildren().add(ContainerManagerFxApp.CURRENT_INSTANCE.getHomePane());
		}

	}

	@FXML
	public void showModule() throws IOException {

		contextMenu.show(btnShowModule, Side.RIGHT, 0, 30);

	}
	
    @FXML
    private void showAdmin() throws IOException {
    	
    	if(ContainerManagerFxApp.cannCurrentControllerClosed()) {
    		clearChildren();
        	
        	childBox.getChildren().add(ContainerManagerFxApp.CURRENT_INSTANCE.getAdminPane());
    	}
    	
    }
    
    private void clearChildren() {
    	while(childBox.getChildren().isEmpty() == false) {
    		childBox.getChildren().remove(0);	
    	}
    }


	@Override
	public void loadView(Pane view, ControllerBase controller) {
		if(ContainerManagerFxApp.cannCurrentControllerClosed()) {
			clearChildren();

			childBox.getChildren().add(view);
			ContainerManagerFxApp.CURRENT_INSTANCE.setCurrentController(controller);
		}
	}
}