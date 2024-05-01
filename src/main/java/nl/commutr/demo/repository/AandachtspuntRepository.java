package nl.commutr.demo.repository;

import java.util.List;
import java.util.Optional;
import nl.commutr.demo.domain.Aandachtspunt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Aandachtspunt entity.
 *
 * When extending this class, extend AandachtspuntRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface AandachtspuntRepository extends AandachtspuntRepositoryWithBagRelationships, JpaRepository<Aandachtspunt, Long> {
    default Optional<Aandachtspunt> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Aandachtspunt> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Aandachtspunt> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
