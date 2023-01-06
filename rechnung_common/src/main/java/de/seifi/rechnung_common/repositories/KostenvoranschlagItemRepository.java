package de.seifi.rechnung_common.repositories;

import de.seifi.rechnung_common.entities.KostenvoranschlagItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface KostenvoranschlagItemRepository extends JpaRepository<KostenvoranschlagItemEntity, UUID> {

}
