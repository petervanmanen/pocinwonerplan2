package nl.commutr.demo.domain;

import java.util.UUID;

public class ActiehouderTestSamples {

    public static Actiehouder getActiehouderSample1() {
        return new Actiehouder().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).naam("naam1");
    }

    public static Actiehouder getActiehouderSample2() {
        return new Actiehouder().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).naam("naam2");
    }

    public static Actiehouder getActiehouderRandomSampleGenerator() {
        return new Actiehouder().id(UUID.randomUUID()).naam(UUID.randomUUID().toString());
    }
}
