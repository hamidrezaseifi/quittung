package de.seifi.rechnung_manager_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"de.seifi.rechnung_common.config"})
public class RechnungManagerSpringApp {
	public static ConfigurableApplicationContext applicationContext;
	
	public static void main(String[] args) {
		
		applicationContext = SpringApplication.run(RechnungManagerSpringApp.class, args);
	    
		
	}

}
