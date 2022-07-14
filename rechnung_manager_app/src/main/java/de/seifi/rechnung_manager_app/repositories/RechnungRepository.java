package de.seifi.rechnung_manager_app.repositories;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import de.seifi.rechnung_manager_app.entities.RechnungEntity;

public interface RechnungRepository extends JpaRepository<RechnungEntity, Integer> {
	@Query("SELECT r FROM RechnungEntity r order by r.nummer")
	Optional<RechnungEntity> findMaxLastNummer();
	
	Optional<RechnungEntity> findTopByOrderByNummerDesc();

	@Query("SELECT r FROM RechnungEntity r where r.created between :from and :to  order by r.nummer desc")
	List<RechnungEntity> search(@Param("from") Timestamp from, @Param("to") Timestamp to);

	@Query("SELECT r FROM RechnungEntity r where r.created between :from and :to and r.nummer=:nummer  order by r.nummer desc")
	List<RechnungEntity> search(@Param("from") Timestamp from, @Param("to") Timestamp to, @Param("nummer") Integer nummer);

}
