package org.vgcs.assignment.restservice.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.vgcs.assignment.restservice.dto.VehicleInfoResponseDTO;
import org.vgcs.assignment.restservice.VehicleInfoService;
import org.vgcs.assignment.restservice.dto.VehicleInfoResponseWithIdDTO;
import org.vgcs.assignment.restservice.exception.RestCallException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class VehicleInfoServiceImpl implements VehicleInfoService {
    private final WebClient webClient;

    public VehicleInfoServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public VehicleInfoResponseWithIdDTO get(String id)  {
        return webClient
                .get()
                .uri("/vehicle/info?id={id}", id)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> clientResponse
                    .bodyToMono(String.class)
                            .flatMap(error -> Mono.error(new RestCallException(error, clientResponse.rawStatusCode(), id)))
                )
                .bodyToMono(VehicleInfoResponseDTO.class)
                .map(vehicleInfoResponseDTO -> new VehicleInfoResponseWithIdDTO(id, vehicleInfoResponseDTO))
                .block();
    }

    @Override
    public List<VehicleInfoResponseWithIdDTO> getAsync(List<String> ids) {
        CompletableFuture<List<VehicleInfoResponseWithIdDTO>> future = new CompletableFuture<>();
        List<VehicleInfoResponseWithIdDTO> res = new ArrayList<>();

        Flux.fromIterable(ids)
                .flatMap(id ->
                        webClient.get()
                            .uri("/vehicle/service?id={id}", id)
                            .retrieve()
                            .onStatus(HttpStatus::isError, clientResponse -> clientResponse
                                    .bodyToMono(String.class)
                                    .flatMap(error -> Mono.error(new RestCallException(error, clientResponse.rawStatusCode(), id))))
                            .bodyToMono(VehicleInfoResponseDTO.class)
                                .map(vehicleInfoResponseDTO -> new VehicleInfoResponseWithIdDTO(id, vehicleInfoResponseDTO))
                )
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
