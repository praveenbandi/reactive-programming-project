package com.learnreactivespring.fluxmonoplayground;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoBackPressureTest {

    @Test
    public void backPressureTest() {
        Flux<Integer> integerFlux = Flux.range(1, 10)
                .log();
        StepVerifier.create(integerFlux)
                .expectSubscription()
                .thenRequest(5)
                .expectNextCount(5)
                .thenCancel()
                .verify();
    }

    @Test
    public void backPressure() {
        Flux<Integer> integerFlux = Flux.range(1, 10)
                .log();
        integerFlux.subscribe(e -> System.out.println("element is " + e)
                , e -> System.err.println("Exception is" + e)
                , () -> System.out.println("Done")
                , subscription -> subscription.request(2));
    }

    @Test
    public void backPressure_cancel() {
        Flux<Integer> integerFlux = Flux.range(1, 10)
                .log();
        integerFlux.subscribe(e -> System.out.println("element is " + e)
                , e -> System.err.println("Exception is" + e)
                , () -> System.out.println("Done")
                , Subscription::cancel);
    }

    @Test
    public void customized_backPressure() {
        Flux<Integer> integerFlux = Flux.range(1, 10)
                .log();
        integerFlux.subscribeWith(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnNext(Integer value) {
                super.hookOnNext(value);
                request(1);
                System.out.println("Value received is " + value);
                if (value == 4) cancel();
            }
        });
    }
}
