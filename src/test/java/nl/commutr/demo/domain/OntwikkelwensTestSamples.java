package nl.commutr.demo.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class OntwikkelwensTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Ontwikkelwens getOntwikkelwensSample1() {
        return new Ontwikkelwens().id(1L).code(1).naam("naam1").omschrijving("omschrijving1");
    }

    public static Ontwikkelwens getOntwikkelwensSample2() {
        return new Ontwikkelwens().id(2L).code(2).naam("naam2").omschrijving("omschrijving2");
    }

    public static Ontwikkelwens getOntwikkelwensRandomSampleGenerator() {
        return new Ontwikkelwens()
            .id(longCount.incrementAndGet())
            .code(intCount.incrementAndGet())
            .naam(UUID.randomUUID().toString())
            .omschrijving(UUID.randomUUID().toString());
    }
}
