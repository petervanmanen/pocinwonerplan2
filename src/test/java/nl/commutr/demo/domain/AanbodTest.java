package nl.commutr.demo.domain;

import static nl.commutr.demo.domain.AanbodTestSamples.*;
import static nl.commutr.demo.domain.AandachtspuntTestSamples.*;
import static nl.commutr.demo.domain.ActiviteitTestSamples.*;
import static nl.commutr.demo.domain.OntwikkelwensTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import nl.commutr.demo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AanbodTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Aanbod.class);
        Aanbod aanbod1 = getAanbodSample1();
        Aanbod aanbod2 = new Aanbod();
        assertThat(aanbod1).isNotEqualTo(aanbod2);

        aanbod2.setId(aanbod1.getId());
        assertThat(aanbod1).isEqualTo(aanbod2);

        aanbod2 = getAanbodSample2();
        assertThat(aanbod1).isNotEqualTo(aanbod2);
    }

    @Test
    void activiteitTest() throws Exception {
        Aanbod aanbod = getAanbodRandomSampleGenerator();
        Activiteit activiteitBack = getActiviteitRandomSampleGenerator();

        aanbod.addActiviteit(activiteitBack);
        assertThat(aanbod.getActiviteits()).containsOnly(activiteitBack);

        aanbod.removeActiviteit(activiteitBack);
        assertThat(aanbod.getActiviteits()).doesNotContain(activiteitBack);

        aanbod.activiteits(new HashSet<>(Set.of(activiteitBack)));
        assertThat(aanbod.getActiviteits()).containsOnly(activiteitBack);

        aanbod.setActiviteits(new HashSet<>());
        assertThat(aanbod.getActiviteits()).doesNotContain(activiteitBack);
    }

    @Test
    void aandachtspuntTest() throws Exception {
        Aanbod aanbod = getAanbodRandomSampleGenerator();
        Aandachtspunt aandachtspuntBack = getAandachtspuntRandomSampleGenerator();

        aanbod.addAandachtspunt(aandachtspuntBack);
        assertThat(aanbod.getAandachtspunts()).containsOnly(aandachtspuntBack);
        assertThat(aandachtspuntBack.getAanbods()).containsOnly(aanbod);

        aanbod.removeAandachtspunt(aandachtspuntBack);
        assertThat(aanbod.getAandachtspunts()).doesNotContain(aandachtspuntBack);
        assertThat(aandachtspuntBack.getAanbods()).doesNotContain(aanbod);

        aanbod.aandachtspunts(new HashSet<>(Set.of(aandachtspuntBack)));
        assertThat(aanbod.getAandachtspunts()).containsOnly(aandachtspuntBack);
        assertThat(aandachtspuntBack.getAanbods()).containsOnly(aanbod);

        aanbod.setAandachtspunts(new HashSet<>());
        assertThat(aanbod.getAandachtspunts()).doesNotContain(aandachtspuntBack);
        assertThat(aandachtspuntBack.getAanbods()).doesNotContain(aanbod);
    }

    @Test
    void ontwikkelwensTest() throws Exception {
        Aanbod aanbod = getAanbodRandomSampleGenerator();
        Ontwikkelwens ontwikkelwensBack = getOntwikkelwensRandomSampleGenerator();

        aanbod.addOntwikkelwens(ontwikkelwensBack);
        assertThat(aanbod.getOntwikkelwens()).containsOnly(ontwikkelwensBack);
        assertThat(ontwikkelwensBack.getAanbods()).containsOnly(aanbod);

        aanbod.removeOntwikkelwens(ontwikkelwensBack);
        assertThat(aanbod.getOntwikkelwens()).doesNotContain(ontwikkelwensBack);
        assertThat(ontwikkelwensBack.getAanbods()).doesNotContain(aanbod);

        aanbod.ontwikkelwens(new HashSet<>(Set.of(ontwikkelwensBack)));
        assertThat(aanbod.getOntwikkelwens()).containsOnly(ontwikkelwensBack);
        assertThat(ontwikkelwensBack.getAanbods()).containsOnly(aanbod);

        aanbod.setOntwikkelwens(new HashSet<>());
        assertThat(aanbod.getOntwikkelwens()).doesNotContain(ontwikkelwensBack);
        assertThat(ontwikkelwensBack.getAanbods()).doesNotContain(aanbod);
    }
}
