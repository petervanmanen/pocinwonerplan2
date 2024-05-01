package nl.commutr.demo.domain;

import static nl.commutr.demo.domain.AanbodTestSamples.*;
import static nl.commutr.demo.domain.ActiviteitTestSamples.*;
import static nl.commutr.demo.domain.SubdoelTestSamples.*;
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
    void subdoelTest() throws Exception {
        Aanbod aanbod = getAanbodRandomSampleGenerator();
        Subdoel subdoelBack = getSubdoelRandomSampleGenerator();

        aanbod.addSubdoel(subdoelBack);
        assertThat(aanbod.getSubdoels()).containsOnly(subdoelBack);

        aanbod.removeSubdoel(subdoelBack);
        assertThat(aanbod.getSubdoels()).doesNotContain(subdoelBack);

        aanbod.subdoels(new HashSet<>(Set.of(subdoelBack)));
        assertThat(aanbod.getSubdoels()).containsOnly(subdoelBack);

        aanbod.setSubdoels(new HashSet<>());
        assertThat(aanbod.getSubdoels()).doesNotContain(subdoelBack);
    }

    @Test
    void activiteitTest() throws Exception {
        Aanbod aanbod = getAanbodRandomSampleGenerator();
        Activiteit activiteitBack = getActiviteitRandomSampleGenerator();

        aanbod.addActiviteit(activiteitBack);
        assertThat(aanbod.getActiviteits()).containsOnly(activiteitBack);
        assertThat(activiteitBack.getAanbods()).containsOnly(aanbod);

        aanbod.removeActiviteit(activiteitBack);
        assertThat(aanbod.getActiviteits()).doesNotContain(activiteitBack);
        assertThat(activiteitBack.getAanbods()).doesNotContain(aanbod);

        aanbod.activiteits(new HashSet<>(Set.of(activiteitBack)));
        assertThat(aanbod.getActiviteits()).containsOnly(activiteitBack);
        assertThat(activiteitBack.getAanbods()).containsOnly(aanbod);

        aanbod.setActiviteits(new HashSet<>());
        assertThat(aanbod.getActiviteits()).doesNotContain(activiteitBack);
        assertThat(activiteitBack.getAanbods()).doesNotContain(aanbod);
    }
}
