package com.learnreactivespring.fluxmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoFilterTest {

    List<String> names = Arrays.asList("adam", "anna", "jack", "jenny");

    @Test
    public void filterTest() {
        Flux<String> stringFlux = Flux.fromIterable(names)
                .filter(s -> s.startsWith("a"))
                .log();
        StepVerifier.create(stringFlux)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void filterLengthTest() {
        Flux<String> stringFlux = Flux.fromIterable(names)
                .filter(s -> s.length() > 4)
                .log();
        StepVerifier.create(stringFlux)
                .expectNextCount(1)
                .verifyComplete();
    }
}
