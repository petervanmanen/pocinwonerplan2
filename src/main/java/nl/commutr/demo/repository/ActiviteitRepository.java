package nl.commutr.demo.repository;

import nl.commutr.demo.domain.Activiteit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Activiteit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActiviteitRepository extends JpaRepository<Activiteit, Long> {}
