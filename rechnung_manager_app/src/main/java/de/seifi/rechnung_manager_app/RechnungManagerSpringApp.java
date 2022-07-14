package de.seifi.rechnung_manager_app;

import it.sauronsoftware.junique.AlreadyLockedException;
import it.sauronsoftware.junique.JUnique;
import it.sauronsoftware.junique.MessageHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import javafx.application.Application;

@SpringBootApplication
@ComponentScan(basePackages = {"de.seifi.rechnung_common.config"})
public class RechnungManagerSpringApp {
	public static ConfigurableApplicationContext applicationContext;
	
	public static void main(String[] args) {
		
		String appId = "RechnungManagerSpringAppId";
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
	    	
	    	applicationContext = SpringApplication.run(RechnungManagerSpringApp.class, args);
	    	
			Application.launch(RechnungManagerFxApp.class, args);
			
	    } else {
	        for (int i = 0; i < args.length; i++) {
	            JUnique.sendMessage(appId, args[0]);
	        }
	    }
	    
		
	}

}
