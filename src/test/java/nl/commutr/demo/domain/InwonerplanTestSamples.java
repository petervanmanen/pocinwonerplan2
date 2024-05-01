package nl.commutr.demo.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InwonerplanTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Inwonerplan getInwonerplanSample1() {
        return new Inwonerplan().id(1L).bsn("bsn1");
    }

    public static Inwonerplan getInwonerplanSample2() {
        return new Inwonerplan().id(2L).bsn("bsn2");
    }

    public static Inwonerplan getInwonerplanRandomSampleGenerator() {
        return new Inwonerplan().id(longCount.incrementAndGet()).bsn(UUID.randomUUID().toString());
    }
}
