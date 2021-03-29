package com.learnreactivespring.fluxmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class ColdAndHotPublisherTest {

    @Test
    public void coldPublisherTest() throws InterruptedException {
        Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E", "F")
                .delayElements(Duration.ofSeconds(1))
                .log();
        stringFlux.subscribe(s -> System.out.println("Sub 1 : " + s)); //emits value from beginning
        Thread.sleep(2000);
        stringFlux.subscribe(s -> System.out.println("Sub 2 : " + s)); //emits value from beginning
        Thread.sleep(4000);
    }

    @Test
    public void hotPublisherTest() throws InterruptedException {

        Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E", "F")
                .delayElements(Duration.ofSeconds(1))
                .log();
        ConnectableFlux<String> connectableFlux = stringFlux.publish();
        connectableFlux.connect();
        connectableFlux.subscribe(s -> System.out.println("Sub 1 : " + s)); //emits value from beginning
        Thread.sleep(2000);
        connectableFlux.subscribe(s -> System.out.println("Sub 2 : " + s)); //does not emit value from beginning
        Thread.sleep(4000);
    }
}
