package org.vgcs.assignment.graphql.resolver;

import graphql.execution.DataFetcherResult;
import graphql.kickstart.execution.error.GenericGraphQLError;
import graphql.kickstart.tools.GraphQLResolver;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.vgcs.assignment.graphql.datafetcher.WrapperGetAllResources;
import org.vgcs.assignment.graphql.datafetcher.WrapperGetResourcesByReflection;
import org.vgcs.assignment.graphql.datafetcher.mongo.MongoWrapper;
import org.vgcs.assignment.graphql.datafetcher.rest.RestWrapperGetAllResources;
import org.vgcs.assignment.graphql.model.VehicleComplete;
import org.vgcs.assignment.graphql.model.Vehicle;
import org.vgcs.assignment.persistance.repository.VehicleRepo;
import org.vgcs.assignment.restservice.VehicleService;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
public class VehicleResolver implements GraphQLResolver<VehicleComplete> {
    private final VehicleService vehicleService;
    private final VehicleRepo vehicleRepo;
    private final WrapperGetResourcesByReflection<Vehicle, VehicleRepo> mongoWrapper;
    private final WrapperGetAllResources<Vehicle, VehicleService> restWrapper;

    public VehicleResolver(VehicleService vehicleService,
                           VehicleRepo vehicleRepo,
                           MongoWrapper<Vehicle, VehicleRepo> mongoWrapper,
                           RestWrapperGetAllResources<Vehicle, VehicleService> restWrapper) {
        this.vehicleService = vehicleService;
        this.vehicleRepo = vehicleRepo;
        this.restWrapper = restWrapper;
        this.mongoWrapper = mongoWrapper;
    }


    public CompletableFuture<DataFetcherResult<Vehicle>> vehicle(VehicleComplete vehicleComplete) {
        return CompletableFuture.supplyAsync(() -> {
            var id = vehicleComplete.getId();
            var result = DataFetcherResult.<Vehicle>newResult();

            var mongoResponse = mongoWrapper.get(vehicleRepo, "findById", id);
            if (mongoResponse.hasData() && !mongoResponse.hasError()) {
                result.data(mongoResponse.getData());
            } else {
                var restResponse  = restWrapper.getAll(vehicleService);
                if (restResponse.hasData()) {
                    Optional<Vehicle> vehicleOptional = getVehicle(restResponse.getData(), id);
                    if (vehicleOptional.isPresent()) {
                        Vehicle v = vehicleOptional.get();
                        vehicleRepo.save(v);
                        result.data(v);
                    }

                    if (restResponse.hasError()) {
                        result.error(new GenericGraphQLError(restResponse.getErrorMessage()));
                    }
                }
            }

            return result.build();
        });
    }

    @NotNull
    private Optional<Vehicle> getVehicle(List<Vehicle> vehicleList, String id) {
        return vehicleList.stream()
                .filter(v -> v.getId().equals(id))
                .findFirst();
    }
}
