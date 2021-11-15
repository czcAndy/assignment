package org.vgcs.assignment.graphql.resolver;

import graphql.execution.DataFetcherResult;
import graphql.kickstart.execution.error.GenericGraphQLError;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.vgcs.assignment.graphql.datafetcher.WrapperGetAllResources;
import org.vgcs.assignment.graphql.datafetcher.WrapperGetResourceById;
import org.vgcs.assignment.graphql.datafetcher.WrapperGetResourcesByProperties;
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

    private final VehicleService vehicleService;
    private final VehicleServicesService vehicleServicesService;
    private final VehicleRepo vehicleRepo;
    private final VehicleServicesRepo vehicleServicesRepo;
    private final WrapperGetAllResources wrapperAllVehiclesRest;
    private final WrapperGetResourceById wrapperVehicleServiceRest;
    private final WrapperGetResourceById wrapperVehiclesWithPartialNames;
    private final WrapperGetResourcesByProperties wrapperServicesByNameAndStatus;

    public QueryResolver(VehicleService vehicleService,
                         VehicleServicesService vehicleServicesService,
                         VehicleRepo vehicleRepo,
                         VehicleServicesRepo vehicleServicesRepo,
                         @Qualifier("restWrapper_getAllVehicles") WrapperGetAllResources wrapperAllVehiclesRest,
                         @Qualifier("restWrapper_getVehicleServices") WrapperGetResourceById wrapperVehicleServiceRest,
                         @Qualifier("mongoWrapper_getVehiclePartialName") WrapperGetResourceById wrapperVehiclesWithPartialNames,
                         @Qualifier("mongoWrapper_serviceFromProperties") WrapperGetResourcesByProperties wrapperServicesByNameAndStatus) {
        this.vehicleService = vehicleService;
        this.vehicleServicesService = vehicleServicesService;
        this.vehicleRepo = vehicleRepo;
        this.vehicleServicesRepo = vehicleServicesRepo;
        this.wrapperAllVehiclesRest = wrapperAllVehiclesRest;
        this.wrapperVehicleServiceRest = wrapperVehicleServiceRest;
        this.wrapperVehiclesWithPartialNames = wrapperVehiclesWithPartialNames;
        this.wrapperServicesByNameAndStatus = wrapperServicesByNameAndStatus;
    }

    public VehicleComplete query(String id) {
        VehicleComplete vehicleComplete = new VehicleComplete();
        vehicleComplete.setId(id);
        return vehicleComplete;
    }

    public CompletableFuture<DataFetcherResult<List<VehicleComplete>>> queryByPartialName(String name) {
        return CompletableFuture.supplyAsync(() -> {
            var result = DataFetcherResult.<List<VehicleComplete>>newResult();
            var mongoWrapper =  wrapperVehiclesWithPartialNames.get(vehicleRepo, name);
            if (mongoWrapper.hasData() && !mongoWrapper.hasError()) {
                var vehicleList = (List<Vehicle>)mongoWrapper.getData();
                var vehicleCompleteList = vehicleList.stream().map(v -> {
                    VehicleComplete vh = new VehicleComplete();
                    vh.setId(v.getId());
                    return vh;
                }).toList();

                result.data(vehicleCompleteList);
            } else {
                var wrapper = wrapperAllVehiclesRest.getAll(vehicleService);
                if (wrapper.hasData()){
                    var vehicleList = (List<Vehicle>)wrapper.getData();
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
            var mongoWrapper =  wrapperServicesByNameAndStatus.get(vehicleServicesRepo, serviceName, serviceStatus);

            if (mongoWrapper.hasData() && !mongoWrapper.hasError()) {
                var vehicleList = (List<VehicleServices>)mongoWrapper.getData();
                var vehicleCompleteList = vehicleList.stream().map(v -> {
                    VehicleComplete vh = new VehicleComplete();
                    vh.setId(v.getId());
                    return vh;
                }).toList();

                result.data(vehicleCompleteList);
            } else {
                var restWrapper = wrapperAllVehiclesRest.getAll(vehicleService);
                if (restWrapper.hasData()) {
                    var vehicleDto = (List<Vehicle>) restWrapper.getData();
                    var vehicles = vehicleDto.stream()
                            .filter(v -> {
                                var vehicleServicesDTOWrapper = wrapperVehicleServiceRest.get(vehicleServicesService, v.getId());
                                if (vehicleServicesDTOWrapper.hasError()) {
                                    result.error(new GenericGraphQLError(vehicleServicesDTOWrapper.getErrorMessage()));
                                }
                                if (vehicleServicesDTOWrapper.hasData()) {
                                    var vehicleServicesDto = (VehicleServices) vehicleServicesDTOWrapper.getData();
                                    if (vehicleServicesDto != null)
                                        return vehicleServicesDto.getServices().stream()
                                                .anyMatch(s -> s.getServiceName().equals(serviceName) && s.getStatus().equals(serviceStatus));
                                }
                                return false;
                            })
                            .map(v -> {
                                VehicleComplete vehicleComplete = new VehicleComplete();
                                vehicleComplete.setId(v.getId());
                                return vehicleComplete;
                            }).toList();

                    result.data(vehicles);
                }
            }
            return result.build();
        });
    }
}

