package nl.commutr.demo.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class OntwikkelwensAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOntwikkelwensAllPropertiesEquals(Ontwikkelwens expected, Ontwikkelwens actual) {
        assertOntwikkelwensAutoGeneratedPropertiesEquals(expected, actual);
        assertOntwikkelwensAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOntwikkelwensAllUpdatablePropertiesEquals(Ontwikkelwens expected, Ontwikkelwens actual) {
        assertOntwikkelwensUpdatableFieldsEquals(expected, actual);
        assertOntwikkelwensUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOntwikkelwensAutoGeneratedPropertiesEquals(Ontwikkelwens expected, Ontwikkelwens actual) {
        assertThat(expected)
            .as("Verify Ontwikkelwens auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOntwikkelwensUpdatableFieldsEquals(Ontwikkelwens expected, Ontwikkelwens actual) {
        assertThat(expected)
            .as("Verify Ontwikkelwens relevant properties")
            .satisfies(e -> assertThat(e.getCode()).as("check code").isEqualTo(actual.getCode()))
            .satisfies(e -> assertThat(e.getNaam()).as("check naam").isEqualTo(actual.getNaam()))
            .satisfies(e -> assertThat(e.getOmschrijving()).as("check omschrijving").isEqualTo(actual.getOmschrijving()))
            .satisfies(e -> assertThat(e.getActief()).as("check actief").isEqualTo(actual.getActief()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOntwikkelwensUpdatableRelationshipsEquals(Ontwikkelwens expected, Ontwikkelwens actual) {
        assertThat(expected)
            .as("Verify Ontwikkelwens relationships")
            .satisfies(e -> assertThat(e.getAanbods()).as("check aanbods").isEqualTo(actual.getAanbods()));
    }
}
