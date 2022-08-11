package de.seifi.rechnung_common.repositories;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.seifi.rechnung_common.entities.RechnungEntity;

@Repository
public interface RechnungRepository extends JpaRepository<RechnungEntity, UUID> {

	@Query("SELECT r FROM RechnungEntity r order by r.nummer")
	Optional<RechnungEntity> findMaxLastNummer();
	
	Optional<RechnungEntity> findTopByOrderByNummerDesc();

	@Query("SELECT r FROM RechnungEntity r where r.created between :from and :to and r.status=:status  order by r.nummer desc")
	List<RechnungEntity> search(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to, @Param("status") int status);

	@Query("SELECT r FROM RechnungEntity r where r.created between :from and :to and r.nummer=:nummer and r.status=:status  order by r.nummer desc")
	List<RechnungEntity> search(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to, @Param("nummer") Integer nummer, @Param("status") int status);

	@Query("SELECT max(r.nummer) FROM RechnungEntity r where r.status=:status")
	Optional<Integer> getMaxNummer(@Param("status") int status);

	@Query("SELECT r FROM RechnungEntity r where r.customerId=:customerId")
	List<RechnungEntity> findAllByCustomer(@Param("customerId") UUID customerId);
	
	

}
