package nl.commutr.demo.domain;

import static nl.commutr.demo.domain.AanbodTestSamples.*;
import static nl.commutr.demo.domain.AandachtspuntTestSamples.*;
import static nl.commutr.demo.domain.OntwikkelwensTestSamples.*;
import static nl.commutr.demo.domain.SubdoelTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import nl.commutr.demo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubdoelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Subdoel.class);
        Subdoel subdoel1 = getSubdoelSample1();
        Subdoel subdoel2 = new Subdoel();
        assertThat(subdoel1).isNotEqualTo(subdoel2);

        subdoel2.setId(subdoel1.getId());
        assertThat(subdoel1).isEqualTo(subdoel2);

        subdoel2 = getSubdoelSample2();
        assertThat(subdoel1).isNotEqualTo(subdoel2);
    }

    @Test
    void aandachtspuntTest() throws Exception {
        Subdoel subdoel = getSubdoelRandomSampleGenerator();
        Aandachtspunt aandachtspuntBack = getAandachtspuntRandomSampleGenerator();

        subdoel.setAandachtspunt(aandachtspuntBack);
        assertThat(subdoel.getAandachtspunt()).isEqualTo(aandachtspuntBack);

        subdoel.aandachtspunt(null);
        assertThat(subdoel.getAandachtspunt()).isNull();
    }

    @Test
    void ontwikkelwensTest() throws Exception {
        Subdoel subdoel = getSubdoelRandomSampleGenerator();
        Ontwikkelwens ontwikkelwensBack = getOntwikkelwensRandomSampleGenerator();

        subdoel.setOntwikkelwens(ontwikkelwensBack);
        assertThat(subdoel.getOntwikkelwens()).isEqualTo(ontwikkelwensBack);

        subdoel.ontwikkelwens(null);
        assertThat(subdoel.getOntwikkelwens()).isNull();
    }

    @Test
    void aanbodTest() throws Exception {
        Subdoel subdoel = getSubdoelRandomSampleGenerator();
        Aanbod aanbodBack = getAanbodRandomSampleGenerator();

        subdoel.addAanbod(aanbodBack);
        assertThat(subdoel.getAanbods()).containsOnly(aanbodBack);
        assertThat(aanbodBack.getSubdoels()).containsOnly(subdoel);

        subdoel.removeAanbod(aanbodBack);
        assertThat(subdoel.getAanbods()).doesNotContain(aanbodBack);
        assertThat(aanbodBack.getSubdoels()).doesNotContain(subdoel);

        subdoel.aanbods(new HashSet<>(Set.of(aanbodBack)));
        assertThat(subdoel.getAanbods()).containsOnly(aanbodBack);
        assertThat(aanbodBack.getSubdoels()).containsOnly(subdoel);

        subdoel.setAanbods(new HashSet<>());
        assertThat(subdoel.getAanbods()).doesNotContain(aanbodBack);
        assertThat(aanbodBack.getSubdoels()).doesNotContain(subdoel);
    }
}
