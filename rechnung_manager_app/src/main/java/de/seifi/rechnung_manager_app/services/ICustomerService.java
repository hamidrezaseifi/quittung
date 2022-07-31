package de.seifi.rechnung_manager_app.services;

import de.seifi.rechnung_common.entities.CustomerEntity;
import de.seifi.rechnung_manager_app.models.CustomerModel;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ICustomerService {

    List<CustomerModel> getCustomerList();

    Map<UUID, CustomerModel> getCustomerMap();

    Optional<CustomerModel> getById(UUID id);

    Optional<CustomerModel> save(CustomerModel entity);

    void delete(CustomerModel model);

    boolean hasRechnung(CustomerModel model);

    void reloadCustomerList();
}
