package org.vgcs.assignment.restservice.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.vgcs.assignment.restservice.dto.VehicleServicesResponseDTO;
import org.vgcs.assignment.restservice.VehicleServicesService;
import org.vgcs.assignment.restservice.dto.VehicleServicesResponseWithIdDTO;
import org.vgcs.assignment.restservice.exception.RestCallException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class VehicleServicesServiceImpl implements VehicleServicesService {
    private final WebClient webClient;

    public VehicleServicesServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    public VehicleServicesResponseWithIdDTO get(String id) {
        return webClient
                .get()
                .uri("/vehicle/services?id={id}", id)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> clientResponse
                        .bodyToMono(String.class)
                        .flatMap(error -> Mono.error(new RestCallException(error, clientResponse.rawStatusCode(), id))))
                .bodyToMono(VehicleServicesResponseDTO.class)
                .map(vehicleServicesResponseDTO -> new VehicleServicesResponseWithIdDTO(id, vehicleServicesResponseDTO))
                .block();
    }

    @Override
    public List<VehicleServicesResponseWithIdDTO> getAsync(List<String> ids) throws RestCallException {
        CompletableFuture<List<VehicleServicesResponseWithIdDTO>> future = new CompletableFuture<>();
        List<VehicleServicesResponseWithIdDTO> res = new ArrayList<>();

        Flux.fromIterable(ids)
                .flatMap(id ->
                        webClient.get()
                                .uri("/vehicle/services?id={id}", id)
                                .retrieve()
                                .onStatus(HttpStatus::isError, clientResponse -> clientResponse
                                        .bodyToMono(String.class)
                                        .flatMap(error -> Mono.error(new RestCallException(error, clientResponse.rawStatusCode(), id))))
                                .bodyToMono(VehicleServicesResponseDTO.class)
                                .onErrorResume(e -> Mono.empty())
                                .map(vehicleServicesResponseDTO -> new VehicleServicesResponseWithIdDTO(id, vehicleServicesResponseDTO)))
                .doOnComplete(() -> future.complete(res))
                .subscribe(response -> {
                    if(res != null)
                        res.add(response);
                });


        try {
            return future.get();
        } catch (InterruptedException | ExecutionException ex) {
            return null;
        }

    }
}
