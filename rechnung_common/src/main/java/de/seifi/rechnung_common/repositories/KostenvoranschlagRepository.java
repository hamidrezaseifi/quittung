package de.seifi.rechnung_common.repositories;

import de.seifi.rechnung_common.entities.KostenvoranschlagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface KostenvoranschlagRepository extends JpaRepository<KostenvoranschlagEntity, UUID> {

    @Query("SELECT k FROM KostenvoranschlagEntity order by k.created desc limit 1")
    Optional<KostenvoranschlagEntity> getLastKostenvoranschlag();


}
