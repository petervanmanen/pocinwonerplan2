package nl.commutr.demo.repository;

import java.util.List;
import java.util.Optional;
import nl.commutr.demo.domain.Aanbod;
import org.springframework.data.domain.Page;

public interface AanbodRepositoryWithBagRelationships {
    Optional<Aanbod> fetchBagRelationships(Optional<Aanbod> aanbod);

    List<Aanbod> fetchBagRelationships(List<Aanbod> aanbods);

    Page<Aanbod> fetchBagRelationships(Page<Aanbod> aanbods);
}
