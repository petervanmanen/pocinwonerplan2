package nl.commutr.demo.domain;

import static nl.commutr.demo.domain.AanbodTestSamples.*;
import static nl.commutr.demo.domain.HoofddoelTestSamples.*;
import static nl.commutr.demo.domain.InwonerplanTestSamples.*;
import static nl.commutr.demo.domain.SubdoelTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import nl.commutr.demo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InwonerplanTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Inwonerplan.class);
        Inwonerplan inwonerplan1 = getInwonerplanSample1();
        Inwonerplan inwonerplan2 = new Inwonerplan();
        assertThat(inwonerplan1).isNotEqualTo(inwonerplan2);

        inwonerplan2.setId(inwonerplan1.getId());
        assertThat(inwonerplan1).isEqualTo(inwonerplan2);

        inwonerplan2 = getInwonerplanSample2();
        assertThat(inwonerplan1).isNotEqualTo(inwonerplan2);
    }

    @Test
    void hoofddoelTest() throws Exception {
        Inwonerplan inwonerplan = getInwonerplanRandomSampleGenerator();
        Hoofddoel hoofddoelBack = getHoofddoelRandomSampleGenerator();

        inwonerplan.setHoofddoel(hoofddoelBack);
        assertThat(inwonerplan.getHoofddoel()).isEqualTo(hoofddoelBack);

        inwonerplan.hoofddoel(null);
        assertThat(inwonerplan.getHoofddoel()).isNull();
    }

    @Test
    void subdoelTest() throws Exception {
        Inwonerplan inwonerplan = getInwonerplanRandomSampleGenerator();
        Subdoel subdoelBack = getSubdoelRandomSampleGenerator();

        inwonerplan.addSubdoel(subdoelBack);
        assertThat(inwonerplan.getSubdoels()).containsOnly(subdoelBack);
        assertThat(subdoelBack.getInwonerplan()).isEqualTo(inwonerplan);

        inwonerplan.removeSubdoel(subdoelBack);
        assertThat(inwonerplan.getSubdoels()).doesNotContain(subdoelBack);
        assertThat(subdoelBack.getInwonerplan()).isNull();

        inwonerplan.subdoels(new HashSet<>(Set.of(subdoelBack)));
        assertThat(inwonerplan.getSubdoels()).containsOnly(subdoelBack);
        assertThat(subdoelBack.getInwonerplan()).isEqualTo(inwonerplan);

        inwonerplan.setSubdoels(new HashSet<>());
        assertThat(inwonerplan.getSubdoels()).doesNotContain(subdoelBack);
        assertThat(subdoelBack.getInwonerplan()).isNull();
    }

    @Test
    void aanbodTest() throws Exception {
        Inwonerplan inwonerplan = getInwonerplanRandomSampleGenerator();
        Aanbod aanbodBack = getAanbodRandomSampleGenerator();

        inwonerplan.setAanbod(aanbodBack);
        assertThat(inwonerplan.getAanbod()).isEqualTo(aanbodBack);

        inwonerplan.aanbod(null);
        assertThat(inwonerplan.getAanbod()).isNull();
    }
}
