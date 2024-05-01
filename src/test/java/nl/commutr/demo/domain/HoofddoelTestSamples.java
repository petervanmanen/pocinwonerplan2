package nl.commutr.demo.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HoofddoelTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Hoofddoel getHoofddoelSample1() {
        return new Hoofddoel().id(1L).naam("naam1");
    }

    public static Hoofddoel getHoofddoelSample2() {
        return new Hoofddoel().id(2L).naam("naam2");
    }

    public static Hoofddoel getHoofddoelRandomSampleGenerator() {
        return new Hoofddoel().id(longCount.incrementAndGet()).naam(UUID.randomUUID().toString());
    }
}
