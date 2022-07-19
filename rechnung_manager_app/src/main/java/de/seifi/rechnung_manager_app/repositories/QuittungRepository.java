package de.seifi.rechnung_manager_app.repositories;

import de.seifi.rechnung_manager_app.entities.QuittungEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface QuittungRepository extends JpaRepository<QuittungEntity, Integer> {
	@Query("SELECT r FROM QuittungEntity r order by r.nummer")
	Optional<QuittungEntity> findMaxLastNummer();
	
	Optional<QuittungEntity> findTopByOrderByNummerDesc();

	@Query("SELECT r FROM QuittungEntity r where r.created between :from and :to  order by r.nummer desc")
	List<QuittungEntity> search(@Param("from") Timestamp from, @Param("to") Timestamp to);

	@Query("SELECT r FROM QuittungEntity r where r.created between :from and :to and r.nummer=:nummer  order by r.nummer desc")
	List<QuittungEntity> search(@Param("from") Timestamp from, @Param("to") Timestamp to, @Param("nummer") Integer nummer);

	@Query("SELECT max(r.nummer) FROM QuittungEntity r where r.status=:status")
	Optional<Integer> getMaxNummer(@Param("status") int status);

}
