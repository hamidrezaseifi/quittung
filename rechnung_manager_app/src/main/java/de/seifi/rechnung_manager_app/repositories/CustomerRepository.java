package de.seifi.rechnung_manager_app.repositories;

import de.seifi.rechnung_manager_app.entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {

}
