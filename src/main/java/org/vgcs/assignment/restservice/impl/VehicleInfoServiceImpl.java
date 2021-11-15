package org.vgcs.assignment.restservice.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.vgcs.assignment.restservice.dto.VehicleInfoResponseDTO;
import org.vgcs.assignment.restservice.VehicleInfoService;
import org.vgcs.assignment.restservice.exception.RestCallException;
import reactor.core.publisher.Mono;

@Service
public class VehicleInfoServiceImpl implements VehicleInfoService {
    private final WebClient webClient;

    public VehicleInfoServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public VehicleInfoResponseDTO get(String id)  {
        return webClient
                .get()
                .uri("/vehicle/info?id={id}", id)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> clientResponse
                    .bodyToMono(String.class)
                            .flatMap(error -> Mono.error(new RestCallException(error, clientResponse.rawStatusCode(), id)))
                )
                .bodyToMono(VehicleInfoResponseDTO.class)
                .block();
    }
}
