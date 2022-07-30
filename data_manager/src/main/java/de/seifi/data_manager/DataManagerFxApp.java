package de.seifi.data_manager;

import de.seifi.data_manager.utils.AppConfig;
import de.seifi.rechnung_common.utils.ISingleInstanceRunnable;
import de.seifi.rechnung_common.utils.RunSingleInstance;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;

public class DataManagerFxApp extends Application implements ISingleInstanceRunnable {

	public static final AppConfig appConfig = new AppConfig();
	
	
    private static Scene mainScene;

    private static Scene loadingScene;

    public static Stage mainStage;
    
    private static String[] startArgs = null;

    public static void closeApp() {
    	mainStage.close();
    }

    @Override
    public void runInstance(String[] args) {
        Application.launch(args);
    }

    public static void main(String[] args) {
    	DataManagerFxApp.startArgs = args;
    	
        RunSingleInstance.runInstance(args, new DataManagerFxApp(), "DataManagerFxAppId");

    }

    @Override
    public void start(Stage stage) throws IOException {

        createLoadingScene(stage);

        DataManagerFxApp fxApp = this;
        Thread thread = new Thread(() -> {

            try {
                DataManageSpringApp.start(DataManagerFxApp.startArgs);
            }
            catch (Exception ex){
                Platform.runLater(() -> {
                    showError("Anwendung starten ...", "Fehler beim Starten der Anwendung!");
                });


            }
            Platform.runLater(() -> {
                try {
                    fxApp.loadMainScene();
                } catch (IOException e) {

                    throw new RuntimeException(e);
                }

            });
        });

        thread.start();
          
    }

    private void createLoadingScene(Stage stage) throws IOException {
        URL logoUrl = DataManagerFxApp.class.getResource("images/logo_icon_repair.png");
        Image iconImage = new Image(logoUrl.toExternalForm());

        DataManagerFxApp.mainStage = stage;

        DataManagerFxApp.mainStage.getIcons().clear();
        DataManagerFxApp.mainStage.getIcons().add(iconImage);

        loadingScene = new Scene(loadFXML("loading"), 700, 600);
        loadingScene.getStylesheets().add(getMainStyle());
        stage.setScene(loadingScene);

        loadingScene.getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
        stage.setTitle("Daten Manager ...");
        stage.show();
    }

    public void loadMainScene() throws IOException {
    	mainScene = new Scene(loadFXML("main"), 700, 600);
		mainScene.getStylesheets().add(getMainStyle());
		mainStage.setScene(mainScene);

        mainScene.getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
    }

    public static String getMainStyle() {
        return DataManagerFxApp.class.getResource("styles/styles.css").toExternalForm();
    }

    private void closeWindowEvent(WindowEvent event) {
        
    }

    public static FXMLLoader loadFXMLLoader(String fxml) throws IOException {
        URL fxmlResource = DataManagerFxApp.class.getResource("fxml/" + fxml + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlResource);
        return fxmlLoader;
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = loadFXMLLoader(fxml);
        return fxmlLoader.load();
    }

    public static void showError(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText(null);
        alert.showAndWait();

    }
}
