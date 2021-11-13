package org.vgcs.assignment.services.impl;

import org.springframework.http.HttpStatus;
import org.vgcs.assignment.dto.VehicleInfoResponseDTO;
import org.vgcs.assignment.dto.VehicleResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.vgcs.assignment.exceptionHandlers.MonoHandler;
import org.vgcs.assignment.services.VehicleService;

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
                .uri("/list/")
                .retrieve()
                .onStatus(HttpStatus::isError, MonoHandler::throwRuntimeExceptionFromClientResponse)
                .bodyToMono(VehicleResponseDTO.class)
                .block();
    }
}
