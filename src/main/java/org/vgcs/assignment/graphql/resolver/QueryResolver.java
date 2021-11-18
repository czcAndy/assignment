package org.vgcs.assignment.graphql.resolver;

import graphql.execution.DataFetcherResult;
import graphql.kickstart.execution.error.GenericGraphQLError;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;
import org.vgcs.assignment.graphql.datafetcher.WrapperGetAllResources;
import org.vgcs.assignment.graphql.datafetcher.WrapperGetResource;
import org.vgcs.assignment.graphql.datafetcher.WrapperGetResourcesByReflection;
import org.vgcs.assignment.graphql.model.Vehicle;
import org.vgcs.assignment.graphql.model.VehicleComplete;
import org.vgcs.assignment.graphql.model.VehicleServices;
import org.vgcs.assignment.persistance.repository.VehicleRepo;
import org.vgcs.assignment.persistance.repository.VehicleServicesRepo;
import org.vgcs.assignment.restservice.VehicleService;
import org.vgcs.assignment.restservice.VehicleServicesService;


import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;


@Component
public class QueryResolver implements GraphQLQueryResolver {

    private final VehicleServicesService vehicleServicesService;
    private final VehicleServicesRepo vehicleServicesRepo;
    private final VehicleService vehicleService;
    private final VehicleRepo vehicleRepo;

    private final WrapperGetAllResources<Vehicle, VehicleService> vehicleServiceWrapper;
    private final WrapperGetResource<VehicleServices, VehicleServicesService, String> vehicleServicesServiceWrapper;
    private final WrapperGetResourcesByReflection<Vehicle, VehicleRepo> vehicleRepoWrapper;
    private final WrapperGetResourcesByReflection<VehicleServices, VehicleServicesRepo> vehicleServicesRepoWrapper;

    public QueryResolver(VehicleServicesService vehicleServicesService,
                         VehicleServicesRepo vehicleServicesRepo,
                         VehicleService vehicleService,
                         VehicleRepo vehicleRepo,
                         WrapperGetAllResources<Vehicle, VehicleService> vehicleServiceWrapper,
                         WrapperGetResource<VehicleServices, VehicleServicesService, String> vehicleServicesServiceWrapper,
                         WrapperGetResourcesByReflection<Vehicle, VehicleRepo> vehicleRepoWrapper,
                         WrapperGetResourcesByReflection<VehicleServices, VehicleServicesRepo> vehicleServicesRepoWrapper) {
        this.vehicleServicesService = vehicleServicesService;
        this.vehicleServicesRepo = vehicleServicesRepo;
        this.vehicleService = vehicleService;
        this.vehicleRepo = vehicleRepo;
        this.vehicleServiceWrapper = vehicleServiceWrapper;
        this.vehicleServicesServiceWrapper = vehicleServicesServiceWrapper;
        this.vehicleRepoWrapper = vehicleRepoWrapper;
        this.vehicleServicesRepoWrapper = vehicleServicesRepoWrapper;
    }


    public VehicleComplete query(String id) {
        VehicleComplete vehicleComplete = new VehicleComplete();
        vehicleComplete.setId(id);
        return vehicleComplete;
    }

    public CompletableFuture<DataFetcherResult<List<VehicleComplete>>> queryByPartialName(String name) {
        return CompletableFuture.supplyAsync(() -> {
            var result = DataFetcherResult.<List<VehicleComplete>>newResult();
            var resultMongo =  vehicleRepoWrapper.getAll(vehicleRepo,"findAllByNameContains", name);
            if (resultMongo.hasData() && !resultMongo.hasError()) {
                var vehicleList = resultMongo.getData();
                var vehicleCompleteList = vehicleList.stream().map(v -> {
                    VehicleComplete vh = new VehicleComplete();
                    vh.setId(v.getId());
                    return vh;
                }).toList();

                result.data(vehicleCompleteList);
            } else {
                var wrapper = vehicleServiceWrapper.getAll(vehicleService);
                if (wrapper.hasData()){
                    var vehicleList = wrapper.getData();
                    var vehicleCompleteList = vehicleList.stream()
                            .filter(v -> Objects.nonNull(v.getName()) && v.getName().contains(name)).map(v -> {
                                VehicleComplete vh = new VehicleComplete();
                                vh.setId(v.getId());
                                return vh;
                            }).toList();

                    result.data(vehicleCompleteList);
                }

                if (wrapper.hasError()) {
                    result.error(new GenericGraphQLError(wrapper.getErrorMessage()));
                }
            }

            return  result.build();
        });
    }


    public CompletableFuture<DataFetcherResult<List<VehicleComplete>>> queryByServiceStatus(String serviceName, String serviceStatus) {
        return CompletableFuture.supplyAsync(() -> {
            var result = DataFetcherResult.<List<VehicleComplete>>newResult();
            var resultMongo =  vehicleServicesRepoWrapper.getAll(vehicleServicesRepo,"findServicesByNameAndStatus", serviceName, serviceStatus);

            if (resultMongo.hasData() && !resultMongo.hasError()) {
                var vehicleList = resultMongo.getData();
                var vehicleCompleteList = vehicleList.stream().map(v -> {
                    VehicleComplete vh = new VehicleComplete();
                    vh.setId(v.getId());
                    return vh;
                }).toList();

                result.data(vehicleCompleteList);
            } else {
                var restWrapperVehicles = vehicleServiceWrapper.getAll(vehicleService);
                if (restWrapperVehicles.hasData()) {
                    var vehicleIds = restWrapperVehicles.getData().stream().map(Vehicle::getId).toList();
                    var vehicleServicesDTOWrapper = vehicleServicesServiceWrapper.getAsync(vehicleServicesService, vehicleIds);
                    if (vehicleServicesDTOWrapper.hasData()) {
                        var vehicles = vehicleServicesDTOWrapper.getData().stream()
                                .filter(service -> service.getServices().stream()
                                        .anyMatch(s -> s.getServiceName().equals(serviceName) && s.getStatus().equals(serviceStatus))
                                )
                                .map(s -> {
                                    VehicleComplete vehicleComplete = new VehicleComplete();
                                    vehicleComplete.setId(s.getId());
                                    return vehicleComplete;
                                }).toList();

                        result.data(vehicles);
                    }

                    if (vehicleServicesDTOWrapper.hasError()) {
                        result.error(new GenericGraphQLError(vehicleServicesDTOWrapper.getErrorMessage()));
                    }
                }
            }
            return result.build();
        });
    }
}

