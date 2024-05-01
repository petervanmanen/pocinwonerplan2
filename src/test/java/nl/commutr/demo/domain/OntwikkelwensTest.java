package nl.commutr.demo.domain;

import static nl.commutr.demo.domain.AanbodTestSamples.*;
import static nl.commutr.demo.domain.OntwikkelwensTestSamples.*;
import static nl.commutr.demo.domain.SubdoelTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import nl.commutr.demo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OntwikkelwensTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ontwikkelwens.class);
        Ontwikkelwens ontwikkelwens1 = getOntwikkelwensSample1();
        Ontwikkelwens ontwikkelwens2 = new Ontwikkelwens();
        assertThat(ontwikkelwens1).isNotEqualTo(ontwikkelwens2);

        ontwikkelwens2.setId(ontwikkelwens1.getId());
        assertThat(ontwikkelwens1).isEqualTo(ontwikkelwens2);

        ontwikkelwens2 = getOntwikkelwensSample2();
        assertThat(ontwikkelwens1).isNotEqualTo(ontwikkelwens2);
    }

    @Test
    void subdoelTest() throws Exception {
        Ontwikkelwens ontwikkelwens = getOntwikkelwensRandomSampleGenerator();
        Subdoel subdoelBack = getSubdoelRandomSampleGenerator();

        ontwikkelwens.addSubdoel(subdoelBack);
        assertThat(ontwikkelwens.getSubdoels()).containsOnly(subdoelBack);
        assertThat(subdoelBack.getOntwikkelwens()).isEqualTo(ontwikkelwens);

        ontwikkelwens.removeSubdoel(subdoelBack);
        assertThat(ontwikkelwens.getSubdoels()).doesNotContain(subdoelBack);
        assertThat(subdoelBack.getOntwikkelwens()).isNull();

        ontwikkelwens.subdoels(new HashSet<>(Set.of(subdoelBack)));
        assertThat(ontwikkelwens.getSubdoels()).containsOnly(subdoelBack);
        assertThat(subdoelBack.getOntwikkelwens()).isEqualTo(ontwikkelwens);

        ontwikkelwens.setSubdoels(new HashSet<>());
        assertThat(ontwikkelwens.getSubdoels()).doesNotContain(subdoelBack);
        assertThat(subdoelBack.getOntwikkelwens()).isNull();
    }

    @Test
    void aanbodTest() throws Exception {
        Ontwikkelwens ontwikkelwens = getOntwikkelwensRandomSampleGenerator();
        Aanbod aanbodBack = getAanbodRandomSampleGenerator();

        ontwikkelwens.addAanbod(aanbodBack);
        assertThat(ontwikkelwens.getAanbods()).containsOnly(aanbodBack);

        ontwikkelwens.removeAanbod(aanbodBack);
        assertThat(ontwikkelwens.getAanbods()).doesNotContain(aanbodBack);

        ontwikkelwens.aanbods(new HashSet<>(Set.of(aanbodBack)));
        assertThat(ontwikkelwens.getAanbods()).containsOnly(aanbodBack);

        ontwikkelwens.setAanbods(new HashSet<>());
        assertThat(ontwikkelwens.getAanbods()).doesNotContain(aanbodBack);
    }
}
