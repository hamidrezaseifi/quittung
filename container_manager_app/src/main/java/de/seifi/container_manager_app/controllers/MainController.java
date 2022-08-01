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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;


public class MainController implements Initializable {
	
	@FXML private GridPane childBox;

	@FXML private Button btnShowModule;


	private ContextMenu contextMenu = new ContextMenu();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		ContainerManagerFxApp.setMainController(this);

		List<String> pluginNames = null;
		try {
			pluginNames = getPluginNames();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		for(String plugin: pluginNames){
			MenuItem menuItem = new MenuItem(plugin);
			contextMenu.getItems().add(menuItem);
		}


		try {
			showHome();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	private List<String> getPluginNames() throws MalformedURLException, ClassNotFoundException, NoSuchMethodException,
												 InvocationTargetException, InstantiationException,
												 IllegalAccessException {

		URL[] jarUrls = new URL[1];
		File jarFile = new File("E:\\Hamid\\Projects\\java\\quittung\\plugings\\rechnung_manager_module-0.0.1.jar");
		jarUrls[0] = jarFile.toURI().toURL();
		URLClassLoader urlClassLoader = new URLClassLoader(jarUrls, this.getClass().getClassLoader());
		Thread.currentThread().setContextClassLoader(urlClassLoader);

		Package[] packageList = urlClassLoader.getDefinedPackages();
		Class pluginInfoClass = urlClassLoader.loadClass("de.seifi.rechnung_manager_plugin.PluginInfo");

		Method method = pluginInfoClass.getDeclaredMethod("getIngo");
		Object instance = pluginInfoClass.getDeclaredConstructors()[0].newInstance();
		Object result = method.invoke(instance);

		return Arrays.asList(result.toString());
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

		contextMenu.show(btnShowModule, Side.RIGHT, -40, 0);

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

    
}