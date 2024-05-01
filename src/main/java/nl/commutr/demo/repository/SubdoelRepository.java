package nl.commutr.demo.repository;

import nl.commutr.demo.domain.Subdoel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Subdoel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubdoelRepository extends JpaRepository<Subdoel, Long> {}
