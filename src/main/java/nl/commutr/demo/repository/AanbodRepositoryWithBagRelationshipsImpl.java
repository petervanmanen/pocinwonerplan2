package nl.commutr.demo.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import nl.commutr.demo.domain.Aanbod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class AanbodRepositoryWithBagRelationshipsImpl implements AanbodRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String AANBODS_PARAMETER = "aanbods";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Aanbod> fetchBagRelationships(Optional<Aanbod> aanbod) {
        return aanbod.map(this::fetchActiviteits);
    }

    @Override
    public Page<Aanbod> fetchBagRelationships(Page<Aanbod> aanbods) {
        return new PageImpl<>(fetchBagRelationships(aanbods.getContent()), aanbods.getPageable(), aanbods.getTotalElements());
    }

    @Override
    public List<Aanbod> fetchBagRelationships(List<Aanbod> aanbods) {
        return Optional.of(aanbods).map(this::fetchActiviteits).orElse(Collections.emptyList());
    }

    Aanbod fetchActiviteits(Aanbod result) {
        return entityManager
            .createQuery("select aanbod from Aanbod aanbod left join fetch aanbod.activiteits where aanbod.id = :id", Aanbod.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Aanbod> fetchActiviteits(List<Aanbod> aanbods) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, aanbods.size()).forEach(index -> order.put(aanbods.get(index).getId(), index));
        List<Aanbod> result = entityManager
            .createQuery("select aanbod from Aanbod aanbod left join fetch aanbod.activiteits where aanbod in :aanbods", Aanbod.class)
            .setParameter(AANBODS_PARAMETER, aanbods)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
