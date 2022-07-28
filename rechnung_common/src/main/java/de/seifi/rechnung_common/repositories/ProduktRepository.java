package de.seifi.rechnung_common.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import de.seifi.rechnung_common.entities.ProduktEntity;

import java.util.UUID;

public interface ProduktRepository extends JpaRepository<ProduktEntity, UUID> {
	
	
}
