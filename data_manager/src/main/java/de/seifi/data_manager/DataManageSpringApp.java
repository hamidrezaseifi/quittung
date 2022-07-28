package de.seifi.data_manager;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javafx.application.Platform;


@SpringBootApplication
@ComponentScan(basePackages = {
		"de.seifi.rechnung_common.config", 
		"de.seifi.rechnung_manager_app.data_service.impl"})
@EnableJpaRepositories(basePackages="de.seifi.rechnung_common.repositories")
@EnableTransactionManagement
@EntityScan(basePackages="de.seifi.rechnung_common.entities")
public class DataManageSpringApp {
	public static ConfigurableApplicationContext applicationContext;
	
	public static void start(String[] args, DataManagerFxApp fxApp) {
		
		applicationContext = SpringApplication.run(DataManageSpringApp.class, args);
	    
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				try {
					fxApp.loadMainScene();
				} catch (IOException e) {
					
					throw new RuntimeException(e);
				}
				
			}
		});
		
		
	}

}
