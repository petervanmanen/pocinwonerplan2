package nl.commutr.demo.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import nl.commutr.demo.domain.Activiteit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ActiviteitRepositoryWithBagRelationshipsImpl implements ActiviteitRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String ACTIVITEITS_PARAMETER = "activiteits";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Activiteit> fetchBagRelationships(Optional<Activiteit> activiteit) {
        return activiteit.map(this::fetchAanbods);
    }

    @Override
    public Page<Activiteit> fetchBagRelationships(Page<Activiteit> activiteits) {
        return new PageImpl<>(fetchBagRelationships(activiteits.getContent()), activiteits.getPageable(), activiteits.getTotalElements());
    }

    @Override
    public List<Activiteit> fetchBagRelationships(List<Activiteit> activiteits) {
        return Optional.of(activiteits).map(this::fetchAanbods).orElse(Collections.emptyList());
    }

    Activiteit fetchAanbods(Activiteit result) {
        return entityManager
            .createQuery(
                "select activiteit from Activiteit activiteit left join fetch activiteit.aanbods where activiteit.id = :id",
                Activiteit.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Activiteit> fetchAanbods(List<Activiteit> activiteits) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, activiteits.size()).forEach(index -> order.put(activiteits.get(index).getId(), index));
        List<Activiteit> result = entityManager
            .createQuery(
                "select activiteit from Activiteit activiteit left join fetch activiteit.aanbods where activiteit in :activiteits",
                Activiteit.class
            )
            .setParameter(ACTIVITEITS_PARAMETER, activiteits)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
