package org.vgcs.assignment.graphql.resolver;

import graphql.execution.DataFetcherResult;
import graphql.kickstart.execution.error.GenericGraphQLError;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;
import org.vgcs.assignment.graphql.helper.DtoFrom;
import org.vgcs.assignment.graphql.model.Service;
import org.vgcs.assignment.graphql.model.VehicleComplete;
import org.vgcs.assignment.graphql.model.VehicleServicesWithCommStatus;
import org.vgcs.assignment.persistance.repository.VehicleRepository;
import org.vgcs.assignment.restservice.VehicleServicesService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

@Component
public class VehicleServiceWithCommStatusResolver implements GraphQLResolver<VehicleComplete> {
    private final VehicleServicesService vehicleServicesService;
    private final VehicleRepository vehicleRepository;

    public VehicleServiceWithCommStatusResolver(VehicleServicesService vehicleServicesService, VehicleRepository vehicleRepository) {
        this.vehicleServicesService = vehicleServicesService;
        this.vehicleRepository = vehicleRepository;
    }

    public CompletableFuture<DataFetcherResult<VehicleServicesWithCommStatus>> vehicleServicesWithCommStatus(VehicleComplete vehicleComplete) {
        return CompletableFuture.supplyAsync(() -> {
            var result = DataFetcherResult.<VehicleServicesWithCommStatus>newResult();
            var servicesDTOWrapper = DtoFrom.vehicleServicesService(vehicleServicesService, vehicleComplete.getId().toString());

            if(servicesDTOWrapper.hasData()) {
                var servicesDTO = servicesDTOWrapper.getData();
                var services = servicesDTO.services().stream()
                        .map(service -> {
                    Service graphQlServiceModel = new Service();
                    graphQlServiceModel.setServiceName(service.serviceName());
                    graphQlServiceModel.setStatus(service.status());
                    graphQlServiceModel.setLastUpdated(LocalDateTime.parse(service.lastUpdate(), DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.of("Europe/Stockholm"))));
                    return graphQlServiceModel;
                }).toList();

                var vehicleServicesWithCommStatus = new VehicleServicesWithCommStatus();
                vehicleServicesWithCommStatus.setVehicleServiceList(services);
                vehicleServicesWithCommStatus.setCommunicationStatus(servicesDTO.communicationStatus());

                result.data(vehicleServicesWithCommStatus);
                vehicleComplete.setVehicleServicesWithCommStatus(vehicleServicesWithCommStatus);
                vehicleRepository.save(vehicleComplete);
            }

            if(servicesDTOWrapper.hasError()) {
                result.error(new GenericGraphQLError(servicesDTOWrapper.getErrorMessage()));
            }

            return result.build();
        });
    }
}

