package nl.commutr.demo.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ActiviteitTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Activiteit getActiviteitSample1() {
        return new Activiteit().id(1L).code(1).naam("naam1").actiehouder("actiehouder1").afhandeltermijn(1);
    }

    public static Activiteit getActiviteitSample2() {
        return new Activiteit().id(2L).code(2).naam("naam2").actiehouder("actiehouder2").afhandeltermijn(2);
    }

    public static Activiteit getActiviteitRandomSampleGenerator() {
        return new Activiteit()
            .id(longCount.incrementAndGet())
            .code(intCount.incrementAndGet())
            .naam(UUID.randomUUID().toString())
            .actiehouder(UUID.randomUUID().toString())
            .afhandeltermijn(intCount.incrementAndGet());
    }
}
