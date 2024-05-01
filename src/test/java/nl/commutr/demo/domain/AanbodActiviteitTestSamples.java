package nl.commutr.demo.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class AanbodActiviteitTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AanbodActiviteit getAanbodActiviteitSample1() {
        return new AanbodActiviteit().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).naam("naam1").afhandeltermijn(1);
    }

    public static AanbodActiviteit getAanbodActiviteitSample2() {
        return new AanbodActiviteit().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).naam("naam2").afhandeltermijn(2);
    }

    public static AanbodActiviteit getAanbodActiviteitRandomSampleGenerator() {
        return new AanbodActiviteit().id(UUID.randomUUID()).naam(UUID.randomUUID().toString()).afhandeltermijn(intCount.incrementAndGet());
    }
}
