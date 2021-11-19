package org.vgcs.assignment.restservice.impl;

import org.springframework.http.HttpStatus;
import org.vgcs.assignment.restservice.dto.VehicleDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.vgcs.assignment.restservice.VehicleService;
import org.vgcs.assignment.restservice.dto.VehicleResponseDTO;
import org.vgcs.assignment.restservice.exception.RestCallException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
                .map(VehicleResponseDTO::vehicles)
                .block();
    }

    @Override
    public VehicleDTO get(String id) {
        Optional<VehicleDTO> vehicle = getAll().stream().filter(v -> v.id().equals(id)).findFirst();
        return vehicle.orElse(null);
    }

    @Override
    public List<VehicleDTO> getAsync(List<String> ids) {
        CompletableFuture<List<VehicleDTO>> future = new CompletableFuture<>();
        List<VehicleDTO> res = new ArrayList<>();

        Flux.fromIterable(ids)
                .flatMap(id ->
                        webClient.get()
                                .uri("/vehicle/list/")
                                .retrieve()
                                .onStatus(HttpStatus::isError, clientResponse -> clientResponse
                                        .bodyToMono(String.class)
                                        .flatMap(error -> Mono.error(new RestCallException(error, clientResponse.rawStatusCode(), id))))
                                .bodyToMono(VehicleDTO.class)
                                .onErrorResume(e -> Mono.empty()))
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
