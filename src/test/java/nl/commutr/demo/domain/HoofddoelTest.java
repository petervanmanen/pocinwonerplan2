package nl.commutr.demo.domain;

import static nl.commutr.demo.domain.HoofddoelTestSamples.*;
import static nl.commutr.demo.domain.InwonerplanTestSamples.*;
import static nl.commutr.demo.domain.SubdoelTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import nl.commutr.demo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HoofddoelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Hoofddoel.class);
        Hoofddoel hoofddoel1 = getHoofddoelSample1();
        Hoofddoel hoofddoel2 = new Hoofddoel();
        assertThat(hoofddoel1).isNotEqualTo(hoofddoel2);

        hoofddoel2.setId(hoofddoel1.getId());
        assertThat(hoofddoel1).isEqualTo(hoofddoel2);

        hoofddoel2 = getHoofddoelSample2();
        assertThat(hoofddoel1).isNotEqualTo(hoofddoel2);
    }

    @Test
    void subdoelTest() throws Exception {
        Hoofddoel hoofddoel = getHoofddoelRandomSampleGenerator();
        Subdoel subdoelBack = getSubdoelRandomSampleGenerator();

        hoofddoel.setSubdoel(subdoelBack);
        assertThat(hoofddoel.getSubdoel()).isEqualTo(subdoelBack);

        hoofddoel.subdoel(null);
        assertThat(hoofddoel.getSubdoel()).isNull();
    }

    @Test
    void inwonerplanTest() throws Exception {
        Hoofddoel hoofddoel = getHoofddoelRandomSampleGenerator();
        Inwonerplan inwonerplanBack = getInwonerplanRandomSampleGenerator();

        hoofddoel.setInwonerplan(inwonerplanBack);
        assertThat(hoofddoel.getInwonerplan()).isEqualTo(inwonerplanBack);
        assertThat(inwonerplanBack.getHoofddoel()).isEqualTo(hoofddoel);

        hoofddoel.inwonerplan(null);
        assertThat(hoofddoel.getInwonerplan()).isNull();
        assertThat(inwonerplanBack.getHoofddoel()).isNull();
    }
}
