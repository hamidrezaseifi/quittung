package de.seifi.rechnung_manager_plugin;

import de.seifi.rechnung_common.controllers.ControllerBase;
import de.seifi.rechnung_common.plugins.IPluginInfo;
import de.seifi.rechnung_common.plugins.IViewLoader;
import de.seifi.rechnung_manager_module.ModuleRootClass;
import de.seifi.rechnung_manager_module.controllers.RechnungController;
import de.seifi.rechnung_manager_module.enums.RechnungType;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class PluginInfo implements IPluginInfo {

    private Map<String, Set<String>> views;

    private Menu topMenuItem;
    private MenuItem quittungMenuItem;
    private MenuItem rechnungMenuItem;
    private MenuItem reportMenuItem;
    private MenuItem customersMenuItem;

    public PluginInfo(){
        views = new HashMap<>();

        Set<String> set = new HashSet<>();
        set.add("quittung");
        set.add("rechnung");

        views.put("rechnungen", set);

        set = new HashSet<>();
        set.add("customers");

        views.put("admin", set);

        topMenuItem = new Menu("Rechnungen", getImageView("receipt.png"));

        quittungMenuItem = new MenuItem("Quittung", getImageView("bill.png"));
        quittungMenuItem.setOnAction(e ->{
            FXMLLoader fxmlLoader = ModuleRootClass.loadFxmlLoader("rechnung_base");
            RechnungController controller = new RechnungController(RechnungType.QUITTUNG);
            fxmlLoader.setController(controller);
            extractedAndSetPane(fxmlLoader);

        });

        rechnungMenuItem = new MenuItem("Rechnung", getImageView("receipt.png"));
        rechnungMenuItem.setOnAction(e ->{
            FXMLLoader fxmlLoader = ModuleRootClass.loadFxmlLoader("rechnung_base");
            RechnungController controller = new RechnungController(RechnungType.RECHNUNG);
            fxmlLoader.setController(controller);
            extractedAndSetPane(fxmlLoader);

        });

        reportMenuItem = new MenuItem("Bericht", getImageView("search.png"));
        reportMenuItem.setOnAction(e ->{
            FXMLLoader fxmlLoader = ModuleRootClass.loadFxmlLoader("report");

            extractedAndSetPane(fxmlLoader);

        });

        customersMenuItem = new MenuItem("Kunden-Verwaltung", getImageView("customer.png"));
        customersMenuItem.setOnAction(e ->{
            FXMLLoader fxmlLoader = ModuleRootClass.loadFxmlLoader("customers");

            extractedAndSetPane(fxmlLoader);

        });

        topMenuItem.getItems().addAll(quittungMenuItem, rechnungMenuItem, reportMenuItem, customersMenuItem);


    }

    private void extractedAndSetPane(FXMLLoader fxmlLoader) {
        try {
            Pane pane = fxmlLoader.load();

            IViewLoader viewLoader = (IViewLoader)topMenuItem.getUserData();
            viewLoader.loadView(pane, fxmlLoader.getController());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static ImageView getImageView(String imgName) {
        Image img = new Image(ModuleRootClass.loadImageResource(imgName));
        ImageView imageView = new ImageView();
        imageView.setFitWidth(16);
        imageView.setFitHeight(16);
        imageView.setImage(img);
        return imageView;
    }

    @Override
    public String getPluginName(){
        return "Rechnungen";
    }

    @Override
    public Set<String> getPluginViewKeys(String key){


        return views.get(key);
    }

    @Override
    public Menu getTopMenu(){
        return topMenuItem;
    }

}
