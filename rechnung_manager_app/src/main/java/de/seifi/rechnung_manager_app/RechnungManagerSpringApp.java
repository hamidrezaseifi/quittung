package de.seifi.rechnung_manager_app;

import de.seifi.rechnung_common.utils.RepositoryProvider;
import de.seifi.rechnung_manager_app.services.ICustomerService;
import de.seifi.rechnung_manager_app.services.IProduktService;
import de.seifi.rechnung_manager_app.services.impl.JpaCustomerService;
import de.seifi.rechnung_manager_app.services.impl.JpaProduktService;
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


	private static ICustomerService customerService = null;

	private static IProduktService produktService = null;


	public static ICustomerService getCustomerService(){
		if(RechnungManagerSpringApp.customerService == null){
			RechnungManagerSpringApp.customerService =
					new JpaCustomerService(RepositoryProvider.getCustomerRepository(),
										   RepositoryProvider.getRechnungRepository());
		}
		return RechnungManagerSpringApp.customerService;
	}

	public static IProduktService getProduktService(){
		if(RechnungManagerSpringApp.produktService == null){
			RechnungManagerSpringApp.produktService = new JpaProduktService(RepositoryProvider.getProduktRepository());
		}
		return RechnungManagerSpringApp.produktService;
	}

	public static void start(String[] args, RechnungManagerFxApp fxApp) {
		
		applicationContext = SpringApplication.run(RechnungManagerSpringApp.class, args);

		RepositoryProvider.setApplicationContext(applicationContext);
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
