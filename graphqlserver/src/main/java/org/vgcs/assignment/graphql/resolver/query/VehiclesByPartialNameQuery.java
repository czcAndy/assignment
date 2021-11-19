package org.vgcs.assignment.graphql.resolver.query;

import graphql.execution.DataFetcherResult;
import graphql.kickstart.execution.error.GenericGraphQLError;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;
import org.vgcs.assignment.graphql.datafetcher.WrapperGetAllResources;
import org.vgcs.assignment.graphql.datafetcher.WrapperGetResourcesByReflection;
import org.vgcs.assignment.persistance.VehicleRepo;
import org.vgcs.assignment.persistance.model.Vehicle;
import org.vgcs.assignment.persistance.model.VehicleComplete;
import org.vgcs.assignment.restservice.VehicleService;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
public class VehiclesByPartialNameQuery implements GraphQLQueryResolver {

    private static final String FIND_ALL_BY_NAME_CONTAINS = "findAllByNameContains";
    private final VehicleRepo vehicleRepo;
    private final VehicleService vehicleService;
    private final WrapperGetResourcesByReflection<Vehicle, VehicleRepo> vehicleRepoWrapper;
    private final WrapperGetAllResources<Vehicle, VehicleService> vehicleServiceWrapper;


    public VehiclesByPartialNameQuery(VehicleRepo vehicleRepo,
                                      VehicleService vehicleService,
                                      WrapperGetResourcesByReflection<Vehicle, VehicleRepo> vehicleRepoWrapper, WrapperGetAllResources<Vehicle, VehicleService> vehicleServiceWrapper) {
        this.vehicleRepo = vehicleRepo;
        this.vehicleService = vehicleService;
        this.vehicleRepoWrapper = vehicleRepoWrapper;
        this.vehicleServiceWrapper = vehicleServiceWrapper;
    }

    public CompletableFuture<DataFetcherResult<List<VehicleComplete>>> queryByPartialName(String name) {
        return CompletableFuture.supplyAsync(() -> {
            var result = DataFetcherResult.<List<VehicleComplete>>newResult();
            var resultMongo =  vehicleRepoWrapper.getAll(vehicleRepo, FIND_ALL_BY_NAME_CONTAINS, name);
            if (resultMongo.hasData() && !resultMongo.hasError()) {
                var vehicleList = resultMongo.getData();
                var vehicleCompleteList = vehicleList.stream().map(this::getVehicleComplete).toList();

                result.data(vehicleCompleteList);
            } else {
                var wrapper = vehicleServiceWrapper.getAll(vehicleService);
                if (wrapper.hasData()){
                    var vehicleList = wrapper.getData();
                    var vehicleCompleteList = getVehicleCompleteListByName(name, vehicleList);
                    result.data(vehicleCompleteList);
                }

                if (wrapper.hasError()) {
                    result.error(new GenericGraphQLError(wrapper.getErrorMessage()));
                }
            }
            return  result.build();
        });
    }

    private List<VehicleComplete> getVehicleCompleteListByName(String name, List<Vehicle> vehicleList) {
        return vehicleList.stream()
                .filter(v -> Objects.nonNull(v.getName()) && v.getName().contains(name))
                .map(this::getVehicleComplete)
                .toList();
    }

    private VehicleComplete getVehicleComplete(Vehicle v) {
        VehicleComplete vh = new VehicleComplete();
        vh.setVehicle(v);
        vh.setId(v.getId());
        return vh;
    }
}
