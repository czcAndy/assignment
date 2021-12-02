package org.vgcs.assignment.graphql.resolver.query;

import graphql.execution.DataFetcherResult;
import graphql.kickstart.execution.error.GenericGraphQLError;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.vgcs.assignment.graphql.datafetcher.WrapperGetAllResources;
import org.vgcs.assignment.graphql.datafetcher.WrapperGetResourcesAsync;
import org.vgcs.assignment.graphql.datafetcher.WrapperGetResourcesByReflection;
import org.vgcs.assignment.persistance.VehicleServicesRepo;
import org.vgcs.assignment.persistance.model.Vehicle;
import org.vgcs.assignment.persistance.model.VehicleComplete;
import org.vgcs.assignment.persistance.model.VehicleServices;
import org.vgcs.assignment.restservice.VehicleService;
import org.vgcs.assignment.restservice.VehicleServicesService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class VehiclesByServiceNameAndStatusQuery implements GraphQLQueryResolver {

    private static final String FIND_SERVICES_BY_NAME_AND_STATUS = "findServicesByNameAndStatus";
    private final VehicleServicesService vehicleServicesService;
    private final VehicleServicesRepo vehicleServicesRepo;
    private final VehicleService vehicleService;

    private final WrapperGetAllResources<Vehicle, VehicleService> vehicleServiceWrapper;
    private final WrapperGetResourcesAsync<VehicleServices, VehicleServicesService, String> vehicleServicesServiceWrapper;
    private final WrapperGetResourcesByReflection<VehicleServices, VehicleServicesRepo> vehicleServicesRepoWrapper;

    public VehiclesByServiceNameAndStatusQuery(VehicleServicesService vehicleServicesService,
                                               VehicleServicesRepo vehicleServicesRepo,
                                               VehicleService vehicleService,
                                               WrapperGetAllResources<Vehicle, VehicleService> vehicleServiceWrapper,
                                               WrapperGetResourcesAsync<VehicleServices, VehicleServicesService, String> vehicleServicesServiceWrapper,
                                               WrapperGetResourcesByReflection<VehicleServices, VehicleServicesRepo> vehicleServicesRepoWrapper) {
        this.vehicleServicesService = vehicleServicesService;
        this.vehicleServicesRepo = vehicleServicesRepo;
        this.vehicleService = vehicleService;
        this.vehicleServiceWrapper = vehicleServiceWrapper;
        this.vehicleServicesServiceWrapper = vehicleServicesServiceWrapper;
        this.vehicleServicesRepoWrapper = vehicleServicesRepoWrapper;
    }

    public CompletableFuture<DataFetcherResult<List<VehicleComplete>>> queryByServiceStatus(String serviceName, String serviceStatus) {
        if (serviceName.isEmpty() || serviceStatus.isEmpty())
            return CompletableFuture.completedFuture(
                    DataFetcherResult.<List<VehicleComplete>>newResult()
                            .error(new GenericGraphQLError("Value for service name or service status is blank. This is not allowed"))
                            .build());

        return CompletableFuture.supplyAsync(() -> {
            var result = DataFetcherResult.<List<VehicleComplete>>newResult();
            var resultMongo =  vehicleServicesRepoWrapper.getAll(vehicleServicesRepo, FIND_SERVICES_BY_NAME_AND_STATUS, serviceName, serviceStatus);

            if (resultMongo.hasData() && !resultMongo.hasError()) {
                var vehicleServicesList = resultMongo.getData();
                var vehicleCompleteList = vehicleServicesList.stream().map(this::getVehicleComplete).toList();

                result.data(vehicleCompleteList);
            } else {
                var restWrapperVehicles = vehicleServiceWrapper.getAll(vehicleService);
                if (restWrapperVehicles.hasData()) {
                    var vehicleIds = restWrapperVehicles.getData().stream().map(Vehicle::getId).toList();
                    var vehicleServicesDTOWrapper = vehicleServicesServiceWrapper.getAsync(vehicleServicesService, vehicleIds);
                    if (vehicleServicesDTOWrapper.hasData()) {
                        var vehicleServices = vehicleServicesDTOWrapper.getData();
                        var vehicles = getVehiclesFromServices(serviceName, serviceStatus, vehicleServices);
                        result.data(vehicles);

                        vehicleServicesRepo.saveAll(vehicleServices);
                    }

                    if (vehicleServicesDTOWrapper.hasError()) {
                        result.error(new GenericGraphQLError(vehicleServicesDTOWrapper.getErrorMessage()));
                    }
                }
            }
            return result.build();
        });
    }

    @NotNull
    private VehicleComplete getVehicleComplete(VehicleServices v) {
        VehicleComplete vc = new VehicleComplete();
        vc.setVehicleServicesWithCommStatus(new VehicleServices(v.getId(), v.getCommunicationStatus(), v.getServices()));
        vc.setId(v.getId());
        return vc;
    }

    private List<VehicleComplete> getVehiclesFromServices(String serviceName, String serviceStatus, List<VehicleServices> vehicleServices) {
        return vehicleServices.stream()
                .filter(service -> service.getServices().stream()
                        .anyMatch(s -> s.getServiceName() != null && s.getServiceName().equals(serviceName) && s.getStatus().equals(serviceStatus))
                )
                .map(this::getVehicleComplete)
                .toList();
    }
}
