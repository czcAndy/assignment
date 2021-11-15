package org.vgcs.assignment.graphql.resolver;

import graphql.execution.DataFetcherResult;
import graphql.kickstart.execution.error.GenericGraphQLError;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;
import org.vgcs.assignment.graphql.helper.DtoFrom;
import org.vgcs.assignment.graphql.model.VehicleComplete;
import org.vgcs.assignment.persistance.repository.VehicleRepository;
import org.vgcs.assignment.restservice.VehicleService;
import org.vgcs.assignment.restservice.VehicleServicesService;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


@Component
public class QueryResolver implements GraphQLQueryResolver {

    private final VehicleService vehicleService;
    private final VehicleServicesService vehicleServicesService;

    public QueryResolver(VehicleService vehicleService, VehicleServicesService vehicleServicesService) {
        this.vehicleService = vehicleService;
        this.vehicleServicesService = vehicleServicesService;
    }

    public VehicleComplete query(String id) {
        VehicleComplete vehicleComplete = new VehicleComplete();
        vehicleComplete.setId(id);
        return vehicleComplete;
    }

    public CompletableFuture<DataFetcherResult<List<VehicleComplete>>> queryByPartialName(String name) {
        return CompletableFuture.supplyAsync(() -> {
            var result = DataFetcherResult.<List<VehicleComplete>>newResult();
            var vehicleListWrapper = DtoFrom.vehicleService(vehicleService);

            if (vehicleListWrapper.hasData()){
                var vehicleCompleteList = vehicleListWrapper.getData().stream()
                        .filter(v -> Objects.nonNull(v.name()) && v.name().contains(name)).map(v -> {
                            VehicleComplete vh = new VehicleComplete();
                            vh.setId(v.id());
                            return vh;
                        }).toList();

                result.data(vehicleCompleteList);
            }

            if (vehicleListWrapper.hasError()) {
                result.error(new GenericGraphQLError(vehicleListWrapper.getErrorMessage()));
            }

            return  result.build();
        });
    }


    public CompletableFuture<DataFetcherResult<List<VehicleComplete>>> queryByServiceStatus(String serviceName, String serviceStatus) {
        return CompletableFuture.supplyAsync(() -> {
            var result = DataFetcherResult.<List<VehicleComplete>>newResult();
            var vehicleListWrapper = DtoFrom.vehicleService(vehicleService);

            if (vehicleListWrapper.hasData()) {
                var vehicleDto = vehicleListWrapper.getData();

                var vehicles = vehicleDto.stream()
                        .filter(v -> {
                            var vehicleServicesDTOWrapper = DtoFrom.vehicleServicesService(vehicleServicesService, v.id());
                            if(vehicleServicesDTOWrapper.hasError()) {
                                result.error(new GenericGraphQLError(vehicleServicesDTOWrapper.getErrorMessage()));
                            }

                            if (vehicleServicesDTOWrapper.hasData()) {
                                var vehicleServicesDto = vehicleServicesDTOWrapper.getData();
                                if(vehicleServicesDto.services() != null)
                                    return vehicleServicesDto.services().stream()
                                            .anyMatch(s -> s.serviceName().equals(serviceName) && s.status().equals(serviceStatus));
                            }
                            return false;
                        })
                        .map(v -> {
                            VehicleComplete vehicleComplete = new VehicleComplete();
                            vehicleComplete.setId(v.id());
                            return vehicleComplete;
                        }).toList();

                result.data(vehicles);
            }

            return result.build();
        });
    }

}

