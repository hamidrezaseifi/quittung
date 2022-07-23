package de.seifi.rechnung_manager_app.repositories;

import de.seifi.rechnung_manager_app.entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {

    @Query("SELECT c FROM CustomerEntity c where c.status=:status  order by c.status asc")
    List<CustomerEntity> findAllByStatus(@Param("status") Integer status);

}
