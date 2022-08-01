package de.seifi.rechnung_common.utils;

import de.seifi.rechnung_common.repositories.CustomerRepository;
import de.seifi.rechnung_common.repositories.ProduktRepository;
import de.seifi.rechnung_common.repositories.RechnungItemRepository;
import de.seifi.rechnung_common.repositories.RechnungRepository;
import org.springframework.context.ConfigurableApplicationContext;

public class RepositoryProvider {

    private static CustomerRepository customerRepository = null;

    private static ProduktRepository produktRepository = null;

    private static RechnungRepository rechnungRepository;

    private static RechnungItemRepository rechnungItemRepository;

    private static ConfigurableApplicationContext applicationContext;


    public static void setApplicationContext(ConfigurableApplicationContext applicationContext){
        RepositoryProvider.applicationContext = applicationContext;

        RepositoryProvider.customerRepository = applicationContext.getBean(CustomerRepository.class);
        RepositoryProvider.produktRepository = applicationContext.getBean(ProduktRepository.class);
        RepositoryProvider.rechnungRepository = applicationContext.getBean(RechnungRepository.class);
        RepositoryProvider.rechnungItemRepository = applicationContext.getBean(RechnungItemRepository.class);

    }

    public static CustomerRepository getCustomerRepository() {
        return customerRepository;
    }

    public static ProduktRepository getProduktRepository() {
        return produktRepository;
    }

    public static RechnungRepository getRechnungRepository() {
        return rechnungRepository;
    }

    public static RechnungItemRepository getRechnungItemRepository() {
        return rechnungItemRepository;
    }
}
