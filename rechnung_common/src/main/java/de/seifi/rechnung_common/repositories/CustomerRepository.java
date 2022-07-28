package de.seifi.rechnung_common.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import de.seifi.rechnung_common.entities.CustomerEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID> {

    @Query("SELECT c FROM CustomerEntity c where c.status=:status  order by c.status asc")
    List<CustomerEntity> findAllByStatus(@Param("status") Integer status);

}
