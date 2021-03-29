package com.learnreactivespring.fluxmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoTimeTest {

    @Test
    public void infiniteSequence() throws InterruptedException {
        Flux<Long> infiniteFlux = Flux.interval(Duration.ofMillis(100))
                .log();
        infiniteFlux.subscribe((element -> System.out.println("Value is " + element)));
        Thread.sleep(3000);
    }

    @Test
    public void infiniteSequenceTest() {
        Flux<Long> longFlux = Flux.interval(Duration.ofMillis(100))
                .take(4)
                .log();

        StepVerifier.create(longFlux)
                .expectSubscription()
                .expectNext(0L, 1L, 2L, 3L)
                .verifyComplete();
    }

    @Test
    public void infiniteSequenceTest_withMap() {
        Flux<Integer> longFlux = Flux.interval(Duration.ofMillis(100))
                .map(Long::intValue)
                .take(4)
                .log();

        StepVerifier.create(longFlux)
                .expectSubscription()
                .expectNext(0, 1, 2, 3)
                .verifyComplete();
    }

    @Test
    public void infiniteSequenceTest_withDelay() {
        Flux<Integer> longFlux = Flux.interval(Duration.ofMillis(100))
                .delayElements(Duration.ofMillis(500))
                .map(Long::intValue)
                .take(4)
                .log();

        StepVerifier.create(longFlux)
                .expectSubscription()
                .expectNext(0, 1, 2, 3)
                .verifyComplete();
    }
}
