package de.seifi.rechnung_common.repositories;

import de.seifi.rechnung_common.entities.CustomerFahrzeugScheinEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;


public interface CustomerFahrzeugScheinRepository extends JpaRepository<CustomerFahrzeugScheinEntity, UUID> {

    @Query("SELECT cf FROM CustomerFahrzeugScheinEntity cf where cf.customerId=:customerId  order by cf.name asc")
    List<CustomerFahrzeugScheinEntity> findAllByCustomerId(@Param("customerId") UUID customerId);

}
