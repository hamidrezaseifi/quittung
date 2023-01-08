package de.seifi.rechnung_manager_app;

import de.seifi.rechnung_common.utils.ISingleInstanceRunnable;
import de.seifi.rechnung_common.utils.RunSingleInstance;
import de.seifi.rechnung_manager_app.controllers.ControllerBase;
import de.seifi.rechnung_manager_app.controllers.MainController;
import de.seifi.rechnung_manager_app.controllers.RechnungController;
import de.seifi.rechnung_manager_app.enums.RechnungType;
import de.seifi.rechnung_manager_app.models.CustomerModel;
import de.seifi.rechnung_manager_app.models.RechnungModel;
import de.seifi.rechnung_manager_app.ui.UiUtils;
import de.seifi.rechnung_manager_app.utils.RechUncaughtExceptionHandler;
import de.seifi.rechnung_manager_app.utils.RechnungManagerAppConfig;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RechnungManagerFxApp extends Application implements ISingleInstanceRunnable {

    private static final Logger logger = LoggerFactory.getLogger(RechnungManagerFxApp.class);

    private static final int SPLASH_WIDTH = 770;

	private static final int SPLASH_HEIGHT = 770;

    public static Scene mainScene;
	
	private static Stage splashStage;
	
	private static GridPane splashPane;
	
	public static Stage mainStage;
    
    private static String[] startArgs = null;


    private static MainController mainController;
    
    private static ControllerBase currentController;

    public static RechnungManagerAppConfig appConfig = new RechnungManagerAppConfig();
    private Image iconImage;

    public static UUID loggedUser = null;

    private static String localJasperPrintLogoPath = null;

    @Override
    public void runInstance(String[] args) {
        Application.launch(args);
    }

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(new RechUncaughtExceptionHandler());

        logger.info("Start Main Application");
        RechnungManagerFxApp.startArgs = args;

        copyPrintLogo();

        RunSingleInstance.runInstance(args, new RechnungManagerFxApp(), "RechnungManagerAppId");
    }

    private static void copyPrintLogo() {

        InputStream stream = null;
        OutputStream resStreamOut = null;
        try {
            URL logoJarPath = getJasperPrintLogoPath();

            Path currentRelativePath = Paths.get("");
            String currentRelativePathStr = currentRelativePath.toAbsolutePath().toString();
            File logoLocalFile = Paths.get(currentRelativePathStr, "print_logo.png").toFile();

            stream = logoJarPath.openStream();
            resStreamOut = new FileOutputStream(logoLocalFile);

            if(logoLocalFile.exists()){
                logoLocalFile.delete();
            }

            localJasperPrintLogoPath = logoLocalFile.getAbsolutePath();

            int readBytes;
            byte[] buffer = new byte[4096];
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                stream.close();
                resStreamOut.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }

    private void showSplash(Stage initStage) throws IOException {
    	
    	splashStage = initStage;
        splashStage.getIcons().clear();
        splashStage.getIcons().add(iconImage);

    	splashStage.setTitle("Rechnung Manager ...");
    	
		splashPane = RechnungManagerFxApp.getSplashPane();

        Scene splashScene = new Scene(splashPane, SPLASH_WIDTH, SPLASH_HEIGHT);
        splashScene.getStylesheets().add(getMainStyle());
        initStage.initStyle(StageStyle.UNDECORATED);
        initStage.setOpacity(0.7);
        initStage.setScene(splashScene);
        initStage.show();
        
        initStage.toFront();
    }

    public void showMainStage() throws IOException {
        logger.info("Start Main Stage");

        mainStage = new Stage(StageStyle.DECORATED);
    	mainStage.setTitle("Rechnung Manager ...");
        mainStage.setIconified(true);
        mainStage.getIcons().clear();
        mainStage.getIcons().add(iconImage);


        mainScene = new Scene(loadFXML("main"));
        mainScene.getStylesheets().add(getMainStyle());
        
        mainStage.setScene(mainScene);

        mainScene.getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
        
        mainStage.show();
        
        FadeTransition fadeSplash = new FadeTransition(Duration.seconds(0.7), splashPane);
        fadeSplash.setFromValue(0.7);
        fadeSplash.setToValue(0.0);
        
        fadeSplash.setOnFinished(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
            	mainStage.setIconified(false);
                splashStage.hide();
                mainStage.setMaximized(true);
            }
        });
        fadeSplash.play();
        Platform.runLater(() -> {
            try {
                UiUtils.loadPrintControllers();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    
	@Override
	public void start(Stage primaryStage) throws Exception {

        URL logoUrl = RechnungManagerFxApp.class.getResource("images/logo_icon.png");
        this.iconImage = new Image(logoUrl.toExternalForm());

		showSplash(primaryStage);


        logger.info("Start Spring Application");

        RechnungManagerFxApp fxApp = this;
        Thread thread = new Thread(() -> RechnungManagerSpringApp.start(RechnungManagerFxApp.startArgs, fxApp));

        thread.start();


        //Platform.runLater(this);
		
	}

	
    public static Window getWindow() {
    	return mainScene.getWindow();
    }

    public static String getMainStyle() {
        return RechnungManagerFxApp.class.getResource("styles/styles.css").toExternalForm();
    }

    public static String getPrintStyle() {
        return RechnungManagerFxApp.class.getResource("styles/print_styles.css").toExternalForm();
    }

    public static InputStream getJasperFilePath(String fileName) throws IOException {
        URL url = RechnungManagerFxApp.class.getResource("jasper/" + fileName + ".jasper");

        return url.openStream();
    }

    private static URL getJasperPrintLogoPath() throws IOException {
        URL url = RechnungManagerFxApp.class.getResource("images/logo_small_1065.png");

        return url;
    }

    public static String getLocalJasperPrintLogoPath(){
        return localJasperPrintLogoPath;

    }

    private void closeWindowEvent(WindowEvent event) {
        if(isCurrentControllerDirty()){
            UiUtils.showError("Niche gespeicherte Änderungen ...", currentController.getDirtyMessage());

            event.consume();
            return;
        }

    }

    public static void setRoot(String fxml) throws IOException {
        mainScene.setRoot(loadFXML(fxml));
    }

    public static Parent loadFXML(String fxml) throws IOException {
        URL fxmlResource = RechnungManagerFxApp.class.getResource("fxml/" + fxml + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlResource);
        return fxmlLoader.load();
    }

    public static Image loadLoadingImage() throws IOException {
        URL resource = RechnungManagerFxApp.class.getResource("images/loading.png");
        resource.openStream();
        try (InputStream stream = resource.openStream()) {
            return new Image(stream, 100, 100, false, false);
        }
    }

    public static FXMLLoader getSelectCustomerDialog() throws IOException {
        URL fxmlResource = RechnungManagerFxApp.class.getResource("fxml/select_customer.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlResource);

        return fxmlLoader;
    }

    public static FXMLLoader getCustomerFahrzeugscheinDialog() throws IOException {
        URL fxmlResource = RechnungManagerFxApp.class.getResource("fxml/customer_fahrzeugschein.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlResource);

        return fxmlLoader;
    }
    
    public static Pair<Parent, FXMLLoader> loadFXMLLoader(String fxml) throws IOException {
        URL fxmlResource = RechnungManagerFxApp.class.getResource("fxml/" + fxml + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlResource);
        return new Pair<Parent, FXMLLoader>(fxmlLoader.load(), fxmlLoader) ;
    }

    public static URL loadResource(String resourcePath) throws IOException {
        URL resourceUrl = RechnungManagerFxApp.class.getResource(resourcePath);
        return resourceUrl;
    }

    public static FXMLLoader getRechnungFxmlLoader() throws IOException {

        URL fxmlResource = RechnungManagerFxApp.class.getResource("fxml/rechnung_base.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlResource);

        return fxmlLoader;
    }

    public static FXMLLoader getQuittungPrintFxmlLoader() throws IOException {

        URL fxmlResource = RechnungManagerFxApp.class.getResource("fxml/rechnung_print.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlResource);

        return fxmlLoader;
    }

    public static Pair<GridPane, RechnungController> getRechnungPane(RechnungType rechnungType) throws IOException {

        URL fxmlResource = RechnungManagerFxApp.class.getResource("fxml/rechnung_base.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlResource);
        RechnungController controller = new RechnungController(rechnungType);
        fxmlLoader.setController(controller);
        GridPane rechnungPane = (GridPane)fxmlLoader.load();

        return new Pair<>(rechnungPane, controller);
    }
    public static GridPane getKostenvoranschlag() throws IOException {

        GridPane reportPane =  (GridPane)loadFXML("kostenvoranschlag");
        return reportPane;
    }


    public static GridPane getHomePane() throws IOException {
    	/*if(homePane == null) {
    		homePane =  (GridPane)loadFXML("home");
    	}*/
    	GridPane homePane =  (GridPane)loadFXML("home");
    	return homePane;
    }

    public static GridPane getReportPane() throws IOException {
        GridPane reportPane =  (GridPane)loadFXML("report");
        return reportPane;
    }

    public static GridPane getCustomersPane() throws IOException {
        GridPane reportPane =  (GridPane)loadFXML("customers");
        return reportPane;
    }

    public static GridPane getAdminPane() throws IOException {
    	/*if(adminPane == null) {
    		adminPane =  (GridPane)loadFXML("admin");
    	}*/
        GridPane adminPane =  (GridPane)loadFXML("admin");
        return adminPane;
    }

    public static GridPane getAboutPane() throws IOException {
    	/*if(adminPane == null) {
    		adminPane =  (GridPane)loadFXML("admin");
    	}*/
        GridPane adminPane =  (GridPane)loadFXML("about");
        return adminPane;
    }

    public static GridPane getSplashPane() throws IOException {
    	
    	GridPane splashPane =  (GridPane)loadFXML("splash");
    	return splashPane;
    }

    public static MainController getMainController() {
        return mainController;
    }

    public static void setMainController(MainController mainController) {
        RechnungManagerFxApp.mainController = mainController;
    }

	public static ControllerBase getCurrentController() {
		return currentController;
	}

	private static boolean isCurrentControllerDirty() {
		
		return currentController != null? currentController.isDirty() : false;
	}

	public static boolean cannCurrentControllerClosed() {
		if(isCurrentControllerDirty()){
            UiUtils.showError("Niche gespeicherte Änderungen ...", currentController.getDirtyMessage());

            return false;
        }
		
		return true;
	}

	public static void setCurrentController(ControllerBase currentController) {
		RechnungManagerFxApp.currentController = currentController;
	}

	public static void startEditRechnung(RechnungModel rechnungModel, CustomerModel customerModel) throws IOException {
		logger.debug("Start editing rechnung: " + rechnungModel.getId());
		
		mainController.startEditRechnung(rechnungModel, customerModel);
	}

}
