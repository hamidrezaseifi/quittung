package de.seifi.rechnung_manager.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import de.seifi.rechnung_manager.entities.RechnungEntity;

public interface RechnungRepository extends JpaRepository<RechnungEntity, Integer> {
	@Query("SELECT r FROM RechnungEntity r order by r.nummer")
	Optional<RechnungEntity> findMaxLastNummer();
	
	Optional<RechnungEntity> findTopByOrderByNummerDesc();
}
