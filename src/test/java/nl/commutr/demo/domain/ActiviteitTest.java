package nl.commutr.demo.domain;

import static nl.commutr.demo.domain.AanbodTestSamples.*;
import static nl.commutr.demo.domain.ActiviteitTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import nl.commutr.demo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ActiviteitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Activiteit.class);
        Activiteit activiteit1 = getActiviteitSample1();
        Activiteit activiteit2 = new Activiteit();
        assertThat(activiteit1).isNotEqualTo(activiteit2);

        activiteit2.setId(activiteit1.getId());
        assertThat(activiteit1).isEqualTo(activiteit2);

        activiteit2 = getActiviteitSample2();
        assertThat(activiteit1).isNotEqualTo(activiteit2);
    }

    @Test
    void aanbodTest() throws Exception {
        Activiteit activiteit = getActiviteitRandomSampleGenerator();
        Aanbod aanbodBack = getAanbodRandomSampleGenerator();

        activiteit.addAanbod(aanbodBack);
        assertThat(activiteit.getAanbods()).containsOnly(aanbodBack);

        activiteit.removeAanbod(aanbodBack);
        assertThat(activiteit.getAanbods()).doesNotContain(aanbodBack);

        activiteit.aanbods(new HashSet<>(Set.of(aanbodBack)));
        assertThat(activiteit.getAanbods()).containsOnly(aanbodBack);

        activiteit.setAanbods(new HashSet<>());
        assertThat(activiteit.getAanbods()).doesNotContain(aanbodBack);
    }
}
