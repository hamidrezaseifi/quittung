package de.seifi.rechnung_manager_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import de.seifi.rechnung_manager_app.entities.RechnungItemEntity;

public interface RechnungItemRepository extends JpaRepository<RechnungItemEntity, Integer> {

}
