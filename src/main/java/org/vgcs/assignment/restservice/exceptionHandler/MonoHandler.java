package org.vgcs.assignment.restservice.exceptionHandler;

import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

public class MonoHandler {
    public static Mono<Throwable> throwRuntimeExceptionFromClientResponse(ClientResponse clientResponse) {
        return clientResponse
                .bodyToMono(String.class)
                .flatMap(error -> Mono.error(new RuntimeException(error)));
    }
}
