package de.seifi.rechnung_manager_app;

import de.seifi.rechnung_manager_app.services.ICustomerService;
import de.seifi.rechnung_manager_app.services.IProduktService;
import javafx.application.Platform;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.info.BuildProperties;
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


	private static ICustomerService customerService = null;

	private static BuildProperties buildProperties;

	private static IProduktService produktService = null;


	public static ICustomerService getCustomerService(){
		if(RechnungManagerSpringApp.customerService == null){
			RechnungManagerSpringApp.customerService = RechnungManagerSpringApp.applicationContext.getBean(ICustomerService.class);
		}
		return RechnungManagerSpringApp.customerService;
	}

	public static IProduktService getProduktService(){
		if(RechnungManagerSpringApp.produktService == null){
			RechnungManagerSpringApp.produktService = RechnungManagerSpringApp.applicationContext.getBean(IProduktService.class);
		}
		return RechnungManagerSpringApp.produktService;
	}

	public static void start(String[] args, RechnungManagerFxApp fxApp) {
		
		applicationContext = SpringApplication.run(RechnungManagerSpringApp.class, args);

		Platform.runLater(() -> {
			try {
				fxApp.showMainStage();
			} catch (IOException e) {

				throw new RuntimeException(e);
			}

		});

	}

	public static BuildProperties getBuildProperties(){
		if(RechnungManagerSpringApp.buildProperties == null){
			RechnungManagerSpringApp.buildProperties = RechnungManagerSpringApp.applicationContext.getBean(BuildProperties.class);
		}
		return RechnungManagerSpringApp.buildProperties;
	}

}
