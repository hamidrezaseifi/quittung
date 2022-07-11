package de.seifi.rechnung_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import it.sauronsoftware.junique.AlreadyLockedException;
import it.sauronsoftware.junique.JUnique;
import it.sauronsoftware.junique.MessageHandler;
import javafx.application.Application;

@SpringBootApplication
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
