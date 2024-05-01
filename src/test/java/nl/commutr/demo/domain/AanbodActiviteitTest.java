package nl.commutr.demo.domain;

import static nl.commutr.demo.domain.AanbodActiviteitTestSamples.*;
import static nl.commutr.demo.domain.AanbodTestSamples.*;
import static nl.commutr.demo.domain.ActiehouderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import nl.commutr.demo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AanbodActiviteitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AanbodActiviteit.class);
        AanbodActiviteit aanbodActiviteit1 = getAanbodActiviteitSample1();
        AanbodActiviteit aanbodActiviteit2 = new AanbodActiviteit();
        assertThat(aanbodActiviteit1).isNotEqualTo(aanbodActiviteit2);

        aanbodActiviteit2.setId(aanbodActiviteit1.getId());
        assertThat(aanbodActiviteit1).isEqualTo(aanbodActiviteit2);

        aanbodActiviteit2 = getAanbodActiviteitSample2();
        assertThat(aanbodActiviteit1).isNotEqualTo(aanbodActiviteit2);
    }

    @Test
    void actiehouderTest() throws Exception {
        AanbodActiviteit aanbodActiviteit = getAanbodActiviteitRandomSampleGenerator();
        Actiehouder actiehouderBack = getActiehouderRandomSampleGenerator();

        aanbodActiviteit.setActiehouder(actiehouderBack);
        assertThat(aanbodActiviteit.getActiehouder()).isEqualTo(actiehouderBack);

        aanbodActiviteit.actiehouder(null);
        assertThat(aanbodActiviteit.getActiehouder()).isNull();
    }

    @Test
    void aanbodTest() throws Exception {
        AanbodActiviteit aanbodActiviteit = getAanbodActiviteitRandomSampleGenerator();
        Aanbod aanbodBack = getAanbodRandomSampleGenerator();

        aanbodActiviteit.addAanbod(aanbodBack);
        assertThat(aanbodActiviteit.getAanbods()).containsOnly(aanbodBack);
        assertThat(aanbodBack.getAanbodActiviteits()).containsOnly(aanbodActiviteit);

        aanbodActiviteit.removeAanbod(aanbodBack);
        assertThat(aanbodActiviteit.getAanbods()).doesNotContain(aanbodBack);
        assertThat(aanbodBack.getAanbodActiviteits()).doesNotContain(aanbodActiviteit);

        aanbodActiviteit.aanbods(new HashSet<>(Set.of(aanbodBack)));
        assertThat(aanbodActiviteit.getAanbods()).containsOnly(aanbodBack);
        assertThat(aanbodBack.getAanbodActiviteits()).containsOnly(aanbodActiviteit);

        aanbodActiviteit.setAanbods(new HashSet<>());
        assertThat(aanbodActiviteit.getAanbods()).doesNotContain(aanbodBack);
        assertThat(aanbodBack.getAanbodActiviteits()).doesNotContain(aanbodActiviteit);
    }
}
