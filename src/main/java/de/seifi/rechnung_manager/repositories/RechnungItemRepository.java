package de.seifi.rechnung_manager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import de.seifi.rechnung_manager.entities.RechnungItemEntity;

public interface RechnungItemRepository extends JpaRepository<RechnungItemEntity, Integer> {

}
