package de.seifi.rechnung_manager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import de.seifi.rechnung_manager.entities.ProduktEntity;

public interface ProduktRepository extends JpaRepository<ProduktEntity, Integer> {
	
	
}
