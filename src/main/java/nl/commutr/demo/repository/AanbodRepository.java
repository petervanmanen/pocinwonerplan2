package nl.commutr.demo.repository;

import java.util.List;
import java.util.Optional;
import nl.commutr.demo.domain.Aanbod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Aanbod entity.
 *
 * When extending this class, extend AanbodRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface AanbodRepository extends AanbodRepositoryWithBagRelationships, JpaRepository<Aanbod, Long> {
    default Optional<Aanbod> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Aanbod> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Aanbod> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
