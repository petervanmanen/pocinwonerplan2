package nl.commutr.demo.repository;

import java.util.List;
import java.util.Optional;
import nl.commutr.demo.domain.Ontwikkelwens;
import org.springframework.data.domain.Page;

public interface OntwikkelwensRepositoryWithBagRelationships {
    Optional<Ontwikkelwens> fetchBagRelationships(Optional<Ontwikkelwens> ontwikkelwens);

    List<Ontwikkelwens> fetchBagRelationships(List<Ontwikkelwens> ontwikkelwens);

    Page<Ontwikkelwens> fetchBagRelationships(Page<Ontwikkelwens> ontwikkelwens);
}
