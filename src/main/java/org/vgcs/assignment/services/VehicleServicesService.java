package org.vgcs.assignment.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.vgcs.assignment.dto.VehicleServicesResponseDTO;
import reactor.core.publisher.Mono;

@Component
public class VehicleServicesService {
    private final WebClient webClient;

    public VehicleServicesService(WebClient webClient) {
        this.webClient = webClient;
    }

    public VehicleServicesResponseDTO getVehicleServices(String id) {
        return webClient
                .get()
                .uri("/services/{id}", id)
                .retrieve()
                .onStatus(HttpStatus::isError, this::throwError)
                .bodyToMono(VehicleServicesResponseDTO.class)
                .block();
    }

    private Mono<Throwable> throwError(ClientResponse clientResponse) {
        return clientResponse
                .bodyToMono(String.class)
                .flatMap(error -> Mono.error(new RuntimeException(error)));
    }
}
