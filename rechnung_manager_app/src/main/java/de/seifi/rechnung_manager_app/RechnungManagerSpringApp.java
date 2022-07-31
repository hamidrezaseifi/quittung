package de.seifi.rechnung_manager_app;

import javafx.application.Platform;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;

@SpringBootApplication
@ComponentScan(basePackages = {
		"de.seifi.rechnung_common.config", 
		"de.seifi.rechnung_manager_app.data_service.impl",
		"de.seifi.rechnung_manager_app.services.impl"})
@EnableJpaRepositories(basePackages="de.seifi.rechnung_common.repositories")
@EnableTransactionManagement
@EntityScan(basePackages="de.seifi.rechnung_common.entities")
public class RechnungManagerSpringApp {
	public static ConfigurableApplicationContext applicationContext;
	
	public static void start(String[] args, RechnungManagerFxApp fxApp) {
		
		applicationContext = SpringApplication.run(RechnungManagerSpringApp.class, args);

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				try {
					fxApp.showMainStage();
				} catch (IOException e) {

					throw new RuntimeException(e);
				}

			}
		});

	}

}
