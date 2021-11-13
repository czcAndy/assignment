package org.vgcs.assignment.restservice.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.vgcs.assignment.restservice.dto.VehicleServicesResponseDTO;
import org.vgcs.assignment.restservice.exceptionHandler.MonoHandler;

@Service
public class VehicleServicesServiceImpl {
    private final WebClient webClient;

    public VehicleServicesServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    public VehicleServicesResponseDTO getVehicleServices(String id) {
        return webClient
                .get()
                .uri("/services/{id}", id)
                .retrieve()
                .onStatus(HttpStatus::isError, MonoHandler::throwRuntimeExceptionFromClientResponse)
                .bodyToMono(VehicleServicesResponseDTO.class)
                .block();
    }
}
