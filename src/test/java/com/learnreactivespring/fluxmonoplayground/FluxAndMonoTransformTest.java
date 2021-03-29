package com.learnreactivespring.fluxmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.StepVerifierOptions;

import java.util.Arrays;
import java.util.List;

import static reactor.core.scheduler.Schedulers.parallel;

public class FluxAndMonoTransformTest {

    List<String> names = Arrays.asList("adam", "anna", "jack", "jenny");

    @Test
    public void transformUsingMap() {
        Flux<String> stringFlux = Flux.fromIterable(names)
                .map(String::toUpperCase)
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("ADAM", "ANNA", "JACK", "JENNY")
                .verifyComplete();
    }

    @Test
    public void transformUsingMap_Length() {
        Flux<Integer> integerFlux = Flux.fromIterable(names)
                .map(String::length)
                .log();
        StepVerifier.create(integerFlux)
                .expectNext(4,4,4,5)
                .verifyComplete();
    }

    @Test
    public void transformUsingMap_Length_Repeat() {
        Flux<Integer> integerFlux = Flux.fromIterable(names)
                .map(String::length)
                .repeat(2)
                .log();
        StepVerifier.create(integerFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    @Test
    public void transformUsingMap_Filter() {
        Flux<String> stringFlux = Flux.fromIterable(names)
                .map(String::toUpperCase)
                .filter(s -> s.length() >4)
                .log();
        StepVerifier.create(stringFlux)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void transformUsingFlatMap() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
                //usage: when a db or external service call that returns a flux (s -> Flux<String)>
                .flatMap(s -> {
                    return Flux.fromIterable(convertToList(s));
                })
                .log();
        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    private List<String> convertToList(String s) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(s, "newValue");
    }

    @Test
    public void transformUsingFlatMap_usingParallel() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
                .window(2) //Flux<Flux<String>> -> (A,B), (C,D), (E,F)
                .flatMap(s ->
                    s.map(this::convertToList).subscribeOn(parallel()) // Flux<List<String>>
                    .flatMap(Flux::fromIterable)) // Flux<String>)
                .log();
        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    @Test
    public void transformUsingFlatMap_usingParallelOrdered() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
                .window(2) //Flux<Flux<String>> -> (A,B), (C,D), (E,F)
//                .concatMap(s ->
//                        s.map(this::convertToList).subscribeOn(parallel()) // Flux<List<String>>
                .flatMapSequential(s ->
                        s.map(this::convertToList).subscribeOn(parallel())
                                .flatMap(Flux::fromIterable)) // Flux<String>)
                .log();
        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }
}
