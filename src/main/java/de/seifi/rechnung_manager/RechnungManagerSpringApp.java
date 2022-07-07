package de.seifi.rechnung_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;

@SpringBootApplication
public class RechnungManagerSpringApp {
	public static ConfigurableApplicationContext applicationContext;
	
	public static void main(String[] args) {
		applicationContext = SpringApplication.run(RechnungManagerSpringApp.class, args);
		Application.launch(RechnungManagerFxApp.class, args);
	}

}
