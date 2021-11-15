package org.vgcs.assignment.restservice.impl;

import org.springframework.http.HttpStatus;
import org.vgcs.assignment.restservice.dto.VehicleDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.vgcs.assignment.restservice.VehicleService;
import org.vgcs.assignment.restservice.dto.VehicleResponseDTO;
import org.vgcs.assignment.restservice.exception.RestCallException;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final WebClient webClient;

    public VehicleServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public List<VehicleDTO> getAll() {
        return webClient
                .get()
                .uri("/vehicle/list/")
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> clientResponse
                        .bodyToMono(String.class)
                        .flatMap(error -> Mono.error(new RestCallException(error, clientResponse.rawStatusCode()))))
                .bodyToMono(VehicleResponseDTO.class)
                .map(vehicleResponseDTO -> vehicleResponseDTO.vehicles())
                .block();
    }
}
