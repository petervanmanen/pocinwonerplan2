package nl.commutr.demo.domain;

import static nl.commutr.demo.domain.AanbodTestSamples.*;
import static nl.commutr.demo.domain.InwonerplanActiviteitTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import nl.commutr.demo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InwonerplanActiviteitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InwonerplanActiviteit.class);
        InwonerplanActiviteit inwonerplanActiviteit1 = getInwonerplanActiviteitSample1();
        InwonerplanActiviteit inwonerplanActiviteit2 = new InwonerplanActiviteit();
        assertThat(inwonerplanActiviteit1).isNotEqualTo(inwonerplanActiviteit2);

        inwonerplanActiviteit2.setId(inwonerplanActiviteit1.getId());
        assertThat(inwonerplanActiviteit1).isEqualTo(inwonerplanActiviteit2);

        inwonerplanActiviteit2 = getInwonerplanActiviteitSample2();
        assertThat(inwonerplanActiviteit1).isNotEqualTo(inwonerplanActiviteit2);
    }

    @Test
    void aanbodTest() throws Exception {
        InwonerplanActiviteit inwonerplanActiviteit = getInwonerplanActiviteitRandomSampleGenerator();
        Aanbod aanbodBack = getAanbodRandomSampleGenerator();

        inwonerplanActiviteit.setAanbod(aanbodBack);
        assertThat(inwonerplanActiviteit.getAanbod()).isEqualTo(aanbodBack);

        inwonerplanActiviteit.aanbod(null);
        assertThat(inwonerplanActiviteit.getAanbod()).isNull();
    }
}
