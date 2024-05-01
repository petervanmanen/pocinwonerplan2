package nl.commutr.demo.repository;

import java.util.List;
import java.util.Optional;
import nl.commutr.demo.domain.Ontwikkelwens;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Ontwikkelwens entity.
 *
 * When extending this class, extend OntwikkelwensRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface OntwikkelwensRepository extends OntwikkelwensRepositoryWithBagRelationships, JpaRepository<Ontwikkelwens, Long> {
    default Optional<Ontwikkelwens> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Ontwikkelwens> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Ontwikkelwens> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
