package de.seifi.rechnung_manager_module.services;

import de.seifi.rechnung_common.utils.RepositoryProvider;
import de.seifi.rechnung_manager_module.services.impl.JpaCustomerService;
import de.seifi.rechnung_manager_module.services.impl.JpaProduktService;

import java.util.UUID;

public class RechnungManagerServiceProvider {

    private static ICustomerService customerService = null;

    private static IProduktService produktService = null;

    public static ICustomerService getCustomerService(){
        if(RechnungManagerServiceProvider.customerService == null){
            RechnungManagerServiceProvider.customerService = new JpaCustomerService();
        }
        return RechnungManagerServiceProvider.customerService;
    }

    public static IProduktService getProduktService(){
        if(RechnungManagerServiceProvider.produktService == null){
            RechnungManagerServiceProvider.produktService = new JpaProduktService();
        }
        return RechnungManagerServiceProvider.produktService;
    }
}
