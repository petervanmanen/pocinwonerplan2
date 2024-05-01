package nl.commutr.demo.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import nl.commutr.demo.domain.Ontwikkelwens;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class OntwikkelwensRepositoryWithBagRelationshipsImpl implements OntwikkelwensRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String ONTWIKKELWENS_PARAMETER = "ontwikkelwens";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Ontwikkelwens> fetchBagRelationships(Optional<Ontwikkelwens> ontwikkelwens) {
        return ontwikkelwens.map(this::fetchAanbods);
    }

    @Override
    public Page<Ontwikkelwens> fetchBagRelationships(Page<Ontwikkelwens> ontwikkelwens) {
        return new PageImpl<>(
            fetchBagRelationships(ontwikkelwens.getContent()),
            ontwikkelwens.getPageable(),
            ontwikkelwens.getTotalElements()
        );
    }

    @Override
    public List<Ontwikkelwens> fetchBagRelationships(List<Ontwikkelwens> ontwikkelwens) {
        return Optional.of(ontwikkelwens).map(this::fetchAanbods).orElse(Collections.emptyList());
    }

    Ontwikkelwens fetchAanbods(Ontwikkelwens result) {
        return entityManager
            .createQuery(
                "select ontwikkelwens from Ontwikkelwens ontwikkelwens left join fetch ontwikkelwens.aanbods where ontwikkelwens.id = :id",
                Ontwikkelwens.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Ontwikkelwens> fetchAanbods(List<Ontwikkelwens> ontwikkelwens) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, ontwikkelwens.size()).forEach(index -> order.put(ontwikkelwens.get(index).getId(), index));
        List<Ontwikkelwens> result = entityManager
            .createQuery(
                "select ontwikkelwens from Ontwikkelwens ontwikkelwens left join fetch ontwikkelwens.aanbods where ontwikkelwens in :ontwikkelwens",
                Ontwikkelwens.class
            )
            .setParameter(ONTWIKKELWENS_PARAMETER, ontwikkelwens)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
