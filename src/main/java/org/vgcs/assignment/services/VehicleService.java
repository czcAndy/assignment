package org.vgcs.assignment.services;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.vgcs.assignment.dto.VehicleInfoResponseDTO;
import org.vgcs.assignment.dto.VehicleResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class VehicleService {

    private final WebClient webClient;

    public VehicleService(WebClient webClient) {
        this.webClient = webClient;
    }

    public VehicleInfoResponseDTO getVehiclesById(String id) {
        return webClient
                .get()
                .uri("/vehicles/{id}", id)
                .retrieve()
                .onStatus(HttpStatus::isError, this::throwError)
                .bodyToMono(VehicleInfoResponseDTO.class)
                .block();
    }

    public VehicleResponseDTO getVehicleList() {
        return webClient
                .get()
                .uri("/list/")
                .retrieve()
                .onStatus(HttpStatus::isError, this::throwError)
                .bodyToMono(VehicleResponseDTO.class)
                .block();
    }

    private Mono<Throwable> throwError(ClientResponse clientResponse) {
        return clientResponse
                .bodyToMono(String.class)
                .flatMap(error -> Mono.error(new RuntimeException(error)));
    }
}
