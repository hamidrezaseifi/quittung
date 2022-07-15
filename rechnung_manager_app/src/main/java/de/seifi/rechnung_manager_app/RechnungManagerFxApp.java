package de.seifi.rechnung_manager_app;

import de.seifi.rechnung_manager_app.controllers.ControllerBse;
import de.seifi.rechnung_manager_app.controllers.MainController;
import de.seifi.rechnung_manager_app.ui.UiUtils;
import it.sauronsoftware.junique.AlreadyLockedException;
import it.sauronsoftware.junique.JUnique;
import it.sauronsoftware.junique.MessageHandler;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.util.Pair;

import static de.seifi.rechnung_manager_app.RechnungManagerFxApp.getMainStyle;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.SpringApplication;

public class RechnungManagerFxApp extends Application implements Runnable {

    private static final int SPLASH_WIDTH = 770;

	private static final int SPLASH_HEIGHT = 582;

	private static Scene scene;
	
	private Stage splashStage;
	
	private GridPane splashPane;
	
	private Stage mainStage;
    
    private static String[] startArgs = null;

    private static Stage stage;

    private static MainController mainController;
    
    private static ControllerBse currentController;


    public static void main(String[] args) {
    	
    	String appId = "RechnungManagerAppId";
	    boolean alreadyRunning;
	    try {
	        JUnique.acquireLock(appId, new MessageHandler() {
	            public String handle(String message) {
	                // A brand new argument received! Handle it!
	                return null;
	            }
	        });
	        alreadyRunning = false;
	    } catch (AlreadyLockedException e) {
	        alreadyRunning = true;
	    }
	    if (!alreadyRunning) {
	    	

	    	RechnungManagerFxApp.startArgs = args;
	        Application.launch(args);
	    	
			
	    } else {
	        for (int i = 0; i < args.length; i++) {
	            JUnique.sendMessage(appId, args[0]);
	        }
	    }
	    
    }
    
    public void run(){
        RechnungManagerSpringApp.main(startArgs);
		
	    try {
			showMainStage();
		} catch (IOException e) {
			
			e.printStackTrace();
		}

    }
    
    private void showSplash(Stage initStage) throws IOException {
    	
    	splashStage = initStage;
    	
		splashPane = RechnungManagerFxApp.getSplashPane();

        Scene splashScene = new Scene(splashPane, SPLASH_WIDTH, SPLASH_HEIGHT);
        splashScene.getStylesheets().add(getMainStyle());
        initStage.initStyle(StageStyle.UNDECORATED);
        initStage.setOpacity(0.7);
        initStage.setScene(splashScene);
        initStage.show();
        
        initStage.toFront();
    }
    
    private void showMainStage() throws IOException {
    	
    	mainStage = new Stage(StageStyle.DECORATED);
        mainStage.setTitle("FX Experience");
        mainStage.setIconified(true);
        
        scene = new Scene(loadFXML("main"));
        scene.getStylesheets().add(getMainStyle());
        
        mainStage.setScene(scene);

        scene.getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
        
        mainStage.show();
        
        FadeTransition fadeSplash = new FadeTransition(Duration.seconds(0.7), splashPane);
        fadeSplash.setFromValue(1.0);
        fadeSplash.setToValue(0.0);
        
        fadeSplash.setOnFinished(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
            	mainStage.setIconified(false);
                splashStage.hide();
            }
        });
        fadeSplash.play();
    }

    
	@Override
	public void start(Stage primaryStage) throws Exception {
	    
		RechnungManagerFxApp.stage = primaryStage;
		
		showSplash(primaryStage);
		
		Platform.runLater(this);
		
	}

	
    public static Window getWindow() {
    	return scene.getWindow();
    }

    public static String getMainStyle() {
        return RechnungManagerFxApp.class.getResource("styles/styles.css").toExternalForm();
    }

    private void closeWindowEvent(WindowEvent event) {
        if(isCurrentControllerDirty()){
            UiUtils.showError("Niche gespeicherte Änderungen ...", currentController.getDirtyMessage());

            event.consume();
            return;
        }

    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static Parent loadFXML(String fxml) throws IOException {
        URL fxmlResource = RechnungManagerFxApp.class.getResource("fxml/" + fxml + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlResource);
        return fxmlLoader.load();
    }

    public static Pair<Parent, FXMLLoader> loadFXMLLoader(String fxml) throws IOException {
        URL fxmlResource = RechnungManagerFxApp.class.getResource("fxml/" + fxml + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlResource);
        return new Pair(fxmlLoader.load(), fxmlLoader) ;
    }

    public static URL loadResource(String resourcePath) throws IOException {
        URL resourceUrl = RechnungManagerFxApp.class.getResource(resourcePath);
        return resourceUrl;
    }

    public static GridPane getRechnungPane() throws IOException {
    	
    	GridPane rechnungPane = (GridPane)loadFXML("rechnung");
    	return rechnungPane;
    }

    public static GridPane getHomePane() throws IOException {
    	/*if(homePane == null) {
    		homePane =  (GridPane)loadFXML("home");
    	}*/
    	GridPane homePane =  (GridPane)loadFXML("home");
    	return homePane;
    }

    public static GridPane getReportPane() throws IOException {
    	/*if(reportPane == null) {
    		reportPane =  (GridPane)loadFXML("report");
    	}*/
    	GridPane reportPane =  (GridPane)loadFXML("report");
    	return reportPane;
    }

    public static GridPane getAdminPane() throws IOException {
    	/*if(adminPane == null) {
    		adminPane =  (GridPane)loadFXML("admin");
    	}*/
    	GridPane adminPane =  (GridPane)loadFXML("admin");
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

	public static ControllerBse getCurrentController() {
		return currentController;
	}

	public static boolean isCurrentControllerDirty() {
		return currentController != null? currentController.isDirty() : false;
	}

	public static void setCurrentController(ControllerBse currentController) {
		RechnungManagerFxApp.currentController = currentController;
	}
    
    
}
