package org.vgcs.assignment.graphql.resolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;
import org.vgcs.assignment.graphql.model.VehicleComplete;
import org.vgcs.assignment.restservice.VehicleService;
import org.vgcs.assignment.restservice.VehicleServicesService;
import org.vgcs.assignment.restservice.dto.VehicleResponseDTO;
import org.vgcs.assignment.restservice.dto.VehicleServicesResponseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class VehicleResolver implements GraphQLQueryResolver {

    private final VehicleService vehicleService;
    private final VehicleServicesService vehicleServicesService;

    public VehicleResolver(VehicleService vehicleService, VehicleServicesService vehicleServicesService) {
        this.vehicleService = vehicleService;
        this.vehicleServicesService = vehicleServicesService;
    }

    public VehicleComplete vehicle(String id) {
        VehicleComplete vehicleComplete = new VehicleComplete();
        vehicleComplete.setId(id);

        return vehicleComplete;
    }

    public CompletableFuture<List<VehicleComplete>> vehiclesByPartialName(String name) {
        return CompletableFuture.supplyAsync(() -> {
            VehicleResponseDTO vehicles = vehicleService.getVehicles();
            return vehicles.vehicles().stream().filter(v -> Objects.nonNull(v.name()) && v.name().contains(name)).map(v -> {
                VehicleComplete vh = new VehicleComplete();
                vh.setId(v.id());
                return vh;
            }).collect(Collectors.toList());
        });
    }

    public CompletableFuture<List<VehicleComplete>> vehiclesByServiceStatus(String serviceName, String serviceStatus) {
        return CompletableFuture.supplyAsync(() -> {
            VehicleResponseDTO vehicles = vehicleService.getVehicles();
            List<String> ids = vehicles.vehicles().stream()
                    .filter(v -> {
                            VehicleServicesResponseDTO vehicleServices = vehicleServicesService.getVehicleServices(v.id());
                                return vehicleServices.services().stream()
                                        .filter(s -> s.serviceName().equals(serviceName) && s.status().equals(serviceStatus))
                                        .findAny()
                                        .isPresent();
                    })
                    .map(v -> v.id())
                    .collect(Collectors.toList());


            return  ids.stream().map(id -> {
                VehicleComplete vehicleComplete = new VehicleComplete();
                vehicleComplete.setId(id);
                return vehicleComplete;
            }).collect(Collectors.toList());
        });
    }
}

