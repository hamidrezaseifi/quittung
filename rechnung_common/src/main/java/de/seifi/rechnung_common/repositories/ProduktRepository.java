package de.seifi.rechnung_common.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import de.seifi.rechnung_common.entities.ProduktEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ProduktRepository extends JpaRepository<ProduktEntity, UUID> {

    @Query("SELECT new ProduktEntity(id, produktName, lastPreis, created, max(updated)) FROM ProduktEntity p group by id,lastPreis,produktName,created order by produktName")
    List<ProduktEntity> getLastProduktPreis();
}
