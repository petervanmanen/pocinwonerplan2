package nl.commutr.demo.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SubdoelTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Subdoel getSubdoelSample1() {
        return new Subdoel().id(1L).code(1).naam("naam1");
    }

    public static Subdoel getSubdoelSample2() {
        return new Subdoel().id(2L).code(2).naam("naam2");
    }

    public static Subdoel getSubdoelRandomSampleGenerator() {
        return new Subdoel().id(longCount.incrementAndGet()).code(intCount.incrementAndGet()).naam(UUID.randomUUID().toString());
    }
}
