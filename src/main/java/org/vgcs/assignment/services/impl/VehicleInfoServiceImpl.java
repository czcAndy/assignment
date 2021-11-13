package org.vgcs.assignment.services.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.vgcs.assignment.dto.VehicleInfoResponseDTO;
import org.vgcs.assignment.exceptionHandlers.MonoHandler;
import org.vgcs.assignment.services.VehicleInfoService;

@Service
public class VehicleInfoServiceImpl implements VehicleInfoService {
    private final WebClient webClient;

    public VehicleInfoServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public VehicleInfoResponseDTO getVehiclesById(String id) {
        return webClient
                .get()
                .uri("/vehicles/{id}", id)
                .retrieve()
                .onStatus(HttpStatus::isError, MonoHandler::throwRuntimeExceptionFromClientResponse)
                .bodyToMono(VehicleInfoResponseDTO.class)
                .block();
    }
}
