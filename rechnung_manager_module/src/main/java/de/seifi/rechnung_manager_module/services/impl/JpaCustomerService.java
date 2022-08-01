package de.seifi.rechnung_manager_module.services.impl;

import de.seifi.rechnung_common.entities.CustomerEntity;
import de.seifi.rechnung_common.entities.RechnungEntity;
import de.seifi.rechnung_common.repositories.CustomerRepository;
import de.seifi.rechnung_common.repositories.RechnungRepository;
import de.seifi.rechnung_common.utils.RepositoryProvider;
import de.seifi.rechnung_manager_module.adapter.CustomerAdapter;
import de.seifi.rechnung_manager_module.enums.CustomerStatus;
import de.seifi.rechnung_manager_module.models.CustomerModel;
import de.seifi.rechnung_manager_module.services.ICustomerService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class JpaCustomerService implements ICustomerService {

    private final Map<UUID, CustomerModel> customerMap;

    private final List<CustomerModel> customerList;

    private final CustomerRepository customerRepository;

    private final RechnungRepository rechnungRepository;

    private final CustomerAdapter customerAdapter = new CustomerAdapter();

    public JpaCustomerService() {
        this.customerRepository = RepositoryProvider.getCustomerRepository();
        this.rechnungRepository = RepositoryProvider.getRechnungRepository();

        customerMap = new HashMap<>();
        customerList = new ArrayList<>();
    }


    @Override
    public List<CustomerModel> getCustomerList() {
        if(customerList.isEmpty()){
            reloadCustomerList();
        }
        return customerList;
    }

    @Override
    public Map<UUID, CustomerModel> getCustomerMap() {
        if(customerMap.isEmpty()){
            reloadCustomerList();
        }
        return customerMap;
    }

    @Override
    public Optional<CustomerModel> getById(UUID id) {
        Optional<CustomerModel> customerModelOptional = Optional.empty();
        Optional<CustomerEntity> customerEntityOptional = customerRepository.findById(id);
        if(customerEntityOptional.isPresent()){
            customerModelOptional = Optional.of(customerAdapter.toModel(customerEntityOptional.get()));
        }

        return customerModelOptional;
    }

    @Override
    public Optional<CustomerModel> save(CustomerModel model) {
        CustomerEntity entity = customerAdapter.toEntity(model);
        customerRepository.save(entity);

        return getById(entity.getId());
    }

    @Override
    public void delete(CustomerModel model) {
        CustomerEntity entity = customerAdapter.toEntity(model);
        customerRepository.delete(entity);
    }

    @Override
    public boolean hasRechnung(CustomerModel model) {
        List<RechnungEntity> list = rechnungRepository.findAllByCustomer(model.getId());

        return list.isEmpty() == false;
    }

    @Override
    public void reloadCustomerList() {
        List<CustomerEntity> entityList = customerRepository.findAllByStatus(CustomerStatus.ACTIVE.getValue());
        customerList.clear();
        customerList.addAll(entityList.stream().map(e -> customerAdapter.toModel(e)).collect(Collectors.toList()));

        customerMap.clear();
        customerMap.putAll(customerList.stream().collect(Collectors.toMap(p -> p.getId(), p -> p)));
    }
}
