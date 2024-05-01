package nl.commutr.demo.repository;

import java.util.List;
import java.util.Optional;
import nl.commutr.demo.domain.Aandachtspunt;
import org.springframework.data.domain.Page;

public interface AandachtspuntRepositoryWithBagRelationships {
    Optional<Aandachtspunt> fetchBagRelationships(Optional<Aandachtspunt> aandachtspunt);

    List<Aandachtspunt> fetchBagRelationships(List<Aandachtspunt> aandachtspunts);

    Page<Aandachtspunt> fetchBagRelationships(Page<Aandachtspunt> aandachtspunts);
}
