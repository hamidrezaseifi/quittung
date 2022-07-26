package de.seifi.rechnung_manager_app.utils;

import de.seifi.rechnung_manager_app.RechnungManagerSpringApp;
import de.seifi.rechnung_manager_app.entities.CustomerEntity;
import de.seifi.rechnung_manager_app.entities.RechnungEntity;
import de.seifi.rechnung_manager_app.enums.CustomerStatus;
import de.seifi.rechnung_manager_app.models.CustomerModel;
import de.seifi.rechnung_manager_app.repositories.CustomerRepository;
import de.seifi.rechnung_manager_app.repositories.RechnungRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomerHelper {
    private static Map<Integer, CustomerModel> customerMap;
    private static List<CustomerModel> customerList;

    private static CustomerRepository customerRepository = null;
    
    private static RechnungRepository rechnungRepository = null;


    public static List<CustomerModel> getCustomerList() {
        if(customerList == null){
            reloadCustomerList();
        }
        return customerList;
    }

    public static Map<Integer, CustomerModel> getCustomerMap() {
        if(customerMap == null){
            reloadCustomerList();
        }
        return customerMap;
    }

    public static Optional<CustomerEntity> getById(Integer id){

        return getCustomerRepository().findById(id);
    }
    
    public static Optional<CustomerEntity> save(CustomerEntity entity){
        getCustomerRepository().save(entity);

        return getCustomerRepository().findById(entity.getId());
    } 
    
    public static void delete(CustomerEntity entity){
    	
        getCustomerRepository().delete(entity);

    }
    
    public static boolean hasRechnung(CustomerEntity entity){
    	
    	List<RechnungEntity> list = getRechnungRepository().findAllByCustomer(entity.getId());

    	return list.isEmpty() == false;
    }

    private static CustomerRepository getCustomerRepository() {
        if(customerRepository == null){
            customerRepository = RechnungManagerSpringApp.applicationContext.getBean(CustomerRepository.class);
        }
        return customerRepository;
    }


    private static RechnungRepository getRechnungRepository() {
        if(rechnungRepository == null){
        	rechnungRepository = RechnungManagerSpringApp.applicationContext.getBean(RechnungRepository.class);
        }
        return rechnungRepository;
    }


    public static void reloadCustomerList() {
        List<CustomerEntity> entityList = getCustomerRepository().findAllByStatus(CustomerStatus.ACTIVE.getValue());
        customerList = entityList.stream().map(e -> e.toModel()).collect(Collectors.toList());

        customerMap = customerList.stream().collect(Collectors.toMap(p -> p.getId(), p -> p));
    }
    
    
}
