package nl.commutr.demo.repository;

import nl.commutr.demo.domain.Aandachtspunt;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Aandachtspunt entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AandachtspuntRepository extends JpaRepository<Aandachtspunt, Long> {}
