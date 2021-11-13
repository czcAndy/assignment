package org.vgcs.assignment.restservice.impl;

import org.springframework.http.HttpStatus;
import org.vgcs.assignment.restservice.dto.VehicleResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.vgcs.assignment.restservice.exceptionHandler.MonoHandler;
import org.vgcs.assignment.restservice.VehicleService;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final WebClient webClient;

    public VehicleServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public VehicleResponseDTO getVehicles() {
        return webClient
                .get()
                .uri("/vehicle/list/")
                .retrieve()
                .onStatus(HttpStatus::isError, MonoHandler::throwRuntimeExceptionFromClientResponse)
                .bodyToMono(VehicleResponseDTO.class)
                .block();
    }
}
