package nl.commutr.demo.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AanbodTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Aanbod getAanbodSample1() {
        return new Aanbod().id(1L).naam("naam1").subdoelen("subdoelen1");
    }

    public static Aanbod getAanbodSample2() {
        return new Aanbod().id(2L).naam("naam2").subdoelen("subdoelen2");
    }

    public static Aanbod getAanbodRandomSampleGenerator() {
        return new Aanbod().id(longCount.incrementAndGet()).naam(UUID.randomUUID().toString()).subdoelen(UUID.randomUUID().toString());
    }
}
