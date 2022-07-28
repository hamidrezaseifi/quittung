package de.seifi.rechnung_common.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import de.seifi.rechnung_common.entities.RechnungItemEntity;

import java.util.UUID;

public interface RechnungItemRepository extends JpaRepository<RechnungItemEntity, UUID> {

}
