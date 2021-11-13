package org.vgcs.assignment.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;
import org.vgcs.assignment.graphql.model.Service;
import org.vgcs.assignment.graphql.model.VehicleComplete;
import org.vgcs.assignment.graphql.model.VehicleServicesWithCommStatus;
import org.vgcs.assignment.restservice.VehicleServicesService;
import org.vgcs.assignment.restservice.dto.VehicleServicesResponseDTO;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class VehicleServiceWithCommStatusResolver implements GraphQLResolver<VehicleComplete> {
    private final VehicleServicesService vehicleServicesService;

    public VehicleServiceWithCommStatusResolver(VehicleServicesService vehicleServicesService) {
        this.vehicleServicesService = vehicleServicesService;
    }

    public CompletableFuture<VehicleServicesWithCommStatus> vehicleServicesWithCommStatus(VehicleComplete vehicleComplete) {
        return CompletableFuture.supplyAsync(() -> {
            var startTime = System.currentTimeMillis();
            VehicleServicesResponseDTO vehicleServices = vehicleServicesService.getVehicleServices(vehicleComplete.getId());
            var endTime = System.currentTimeMillis();
            System.out.println("In vehicleServices" + (endTime - startTime));
            List<Service> serviceList = vehicleServices.services().stream().map(service -> {
                Service graphQlServiceModel = new Service();
                graphQlServiceModel.setServiceName(service.serviceName());
                graphQlServiceModel.setStatus(service.status());
                graphQlServiceModel.setLastUpdated(ZonedDateTime.now());
                return graphQlServiceModel;
            }).collect(Collectors.toList());

            VehicleServicesWithCommStatus vehicleServicesWithCommStatus = new VehicleServicesWithCommStatus();
            vehicleServicesWithCommStatus.setVehicleServiceList(serviceList);
            vehicleServicesWithCommStatus.setCommunicationStatus(vehicleServices.communicationStatus());

            return vehicleServicesWithCommStatus;
        });
    }

}

