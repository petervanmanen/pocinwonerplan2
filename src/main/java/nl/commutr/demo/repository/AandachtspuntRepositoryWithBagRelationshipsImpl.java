package nl.commutr.demo.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import nl.commutr.demo.domain.Aandachtspunt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class AandachtspuntRepositoryWithBagRelationshipsImpl implements AandachtspuntRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String AANDACHTSPUNTS_PARAMETER = "aandachtspunts";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Aandachtspunt> fetchBagRelationships(Optional<Aandachtspunt> aandachtspunt) {
        return aandachtspunt.map(this::fetchAanbods);
    }

    @Override
    public Page<Aandachtspunt> fetchBagRelationships(Page<Aandachtspunt> aandachtspunts) {
        return new PageImpl<>(
            fetchBagRelationships(aandachtspunts.getContent()),
            aandachtspunts.getPageable(),
            aandachtspunts.getTotalElements()
        );
    }

    @Override
    public List<Aandachtspunt> fetchBagRelationships(List<Aandachtspunt> aandachtspunts) {
        return Optional.of(aandachtspunts).map(this::fetchAanbods).orElse(Collections.emptyList());
    }

    Aandachtspunt fetchAanbods(Aandachtspunt result) {
        return entityManager
            .createQuery(
                "select aandachtspunt from Aandachtspunt aandachtspunt left join fetch aandachtspunt.aanbods where aandachtspunt.id = :id",
                Aandachtspunt.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Aandachtspunt> fetchAanbods(List<Aandachtspunt> aandachtspunts) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, aandachtspunts.size()).forEach(index -> order.put(aandachtspunts.get(index).getId(), index));
        List<Aandachtspunt> result = entityManager
            .createQuery(
                "select aandachtspunt from Aandachtspunt aandachtspunt left join fetch aandachtspunt.aanbods where aandachtspunt in :aandachtspunts",
                Aandachtspunt.class
            )
            .setParameter(AANDACHTSPUNTS_PARAMETER, aandachtspunts)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
