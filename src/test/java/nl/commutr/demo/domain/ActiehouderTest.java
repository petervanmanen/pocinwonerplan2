package nl.commutr.demo.domain;

import static nl.commutr.demo.domain.AanbodActiviteitTestSamples.*;
import static nl.commutr.demo.domain.ActiehouderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import nl.commutr.demo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ActiehouderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Actiehouder.class);
        Actiehouder actiehouder1 = getActiehouderSample1();
        Actiehouder actiehouder2 = new Actiehouder();
        assertThat(actiehouder1).isNotEqualTo(actiehouder2);

        actiehouder2.setId(actiehouder1.getId());
        assertThat(actiehouder1).isEqualTo(actiehouder2);

        actiehouder2 = getActiehouderSample2();
        assertThat(actiehouder1).isNotEqualTo(actiehouder2);
    }

    @Test
    void aanbodActiviteitTest() throws Exception {
        Actiehouder actiehouder = getActiehouderRandomSampleGenerator();
        AanbodActiviteit aanbodActiviteitBack = getAanbodActiviteitRandomSampleGenerator();

        actiehouder.addAanbodActiviteit(aanbodActiviteitBack);
        assertThat(actiehouder.getAanbodActiviteits()).containsOnly(aanbodActiviteitBack);
        assertThat(aanbodActiviteitBack.getActiehouder()).isEqualTo(actiehouder);

        actiehouder.removeAanbodActiviteit(aanbodActiviteitBack);
        assertThat(actiehouder.getAanbodActiviteits()).doesNotContain(aanbodActiviteitBack);
        assertThat(aanbodActiviteitBack.getActiehouder()).isNull();

        actiehouder.aanbodActiviteits(new HashSet<>(Set.of(aanbodActiviteitBack)));
        assertThat(actiehouder.getAanbodActiviteits()).containsOnly(aanbodActiviteitBack);
        assertThat(aanbodActiviteitBack.getActiehouder()).isEqualTo(actiehouder);

        actiehouder.setAanbodActiviteits(new HashSet<>());
        assertThat(actiehouder.getAanbodActiviteits()).doesNotContain(aanbodActiviteitBack);
        assertThat(aanbodActiviteitBack.getActiehouder()).isNull();
    }
}
