package nl.commutr.demo.repository;

import nl.commutr.demo.domain.Ontwikkelwens;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Ontwikkelwens entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OntwikkelwensRepository extends JpaRepository<Ontwikkelwens, Long> {}
