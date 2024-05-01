package nl.commutr.demo.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AanbodActiviteitAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAanbodActiviteitAllPropertiesEquals(AanbodActiviteit expected, AanbodActiviteit actual) {
        assertAanbodActiviteitAutoGeneratedPropertiesEquals(expected, actual);
        assertAanbodActiviteitAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAanbodActiviteitAllUpdatablePropertiesEquals(AanbodActiviteit expected, AanbodActiviteit actual) {
        assertAanbodActiviteitUpdatableFieldsEquals(expected, actual);
        assertAanbodActiviteitUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAanbodActiviteitAutoGeneratedPropertiesEquals(AanbodActiviteit expected, AanbodActiviteit actual) {
        assertThat(expected)
            .as("Verify AanbodActiviteit auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAanbodActiviteitUpdatableFieldsEquals(AanbodActiviteit expected, AanbodActiviteit actual) {
        assertThat(expected)
            .as("Verify AanbodActiviteit relevant properties")
            .satisfies(e -> assertThat(e.getNaam()).as("check naam").isEqualTo(actual.getNaam()))
            .satisfies(e -> assertThat(e.getActief()).as("check actief").isEqualTo(actual.getActief()))
            .satisfies(e -> assertThat(e.getAfhandeltermijn()).as("check afhandeltermijn").isEqualTo(actual.getAfhandeltermijn()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAanbodActiviteitUpdatableRelationshipsEquals(AanbodActiviteit expected, AanbodActiviteit actual) {
        assertThat(expected)
            .as("Verify AanbodActiviteit relationships")
            .satisfies(e -> assertThat(e.getActiehouder()).as("check actiehouder").isEqualTo(actual.getActiehouder()))
            .satisfies(e -> assertThat(e.getAanbods()).as("check aanbods").isEqualTo(actual.getAanbods()));
    }
}
