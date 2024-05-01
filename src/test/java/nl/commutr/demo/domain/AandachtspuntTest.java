package nl.commutr.demo.domain;

import static nl.commutr.demo.domain.AandachtspuntTestSamples.*;
import static nl.commutr.demo.domain.SubdoelTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import nl.commutr.demo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AandachtspuntTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Aandachtspunt.class);
        Aandachtspunt aandachtspunt1 = getAandachtspuntSample1();
        Aandachtspunt aandachtspunt2 = new Aandachtspunt();
        assertThat(aandachtspunt1).isNotEqualTo(aandachtspunt2);

        aandachtspunt2.setId(aandachtspunt1.getId());
        assertThat(aandachtspunt1).isEqualTo(aandachtspunt2);

        aandachtspunt2 = getAandachtspuntSample2();
        assertThat(aandachtspunt1).isNotEqualTo(aandachtspunt2);
    }

    @Test
    void subdoelTest() throws Exception {
        Aandachtspunt aandachtspunt = getAandachtspuntRandomSampleGenerator();
        Subdoel subdoelBack = getSubdoelRandomSampleGenerator();

        aandachtspunt.addSubdoel(subdoelBack);
        assertThat(aandachtspunt.getSubdoels()).containsOnly(subdoelBack);
        assertThat(subdoelBack.getAandachtspunt()).isEqualTo(aandachtspunt);

        aandachtspunt.removeSubdoel(subdoelBack);
        assertThat(aandachtspunt.getSubdoels()).doesNotContain(subdoelBack);
        assertThat(subdoelBack.getAandachtspunt()).isNull();

        aandachtspunt.subdoels(new HashSet<>(Set.of(subdoelBack)));
        assertThat(aandachtspunt.getSubdoels()).containsOnly(subdoelBack);
        assertThat(subdoelBack.getAandachtspunt()).isEqualTo(aandachtspunt);

        aandachtspunt.setSubdoels(new HashSet<>());
        assertThat(aandachtspunt.getSubdoels()).doesNotContain(subdoelBack);
        assertThat(subdoelBack.getAandachtspunt()).isNull();
    }
}
