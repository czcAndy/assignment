package org.vgcs.assignment.graphql.resolver.vehicle;

import graphql.execution.DataFetcherResult;
import graphql.kickstart.execution.error.GenericGraphQLError;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;
import org.vgcs.assignment.graphql.datafetcher.WrapperGetResource;
import org.vgcs.assignment.graphql.datafetcher.WrapperGetResourcesByReflection;
import org.vgcs.assignment.graphql.datafetcher.mongo.MongoWrapper;
import org.vgcs.assignment.graphql.datafetcher.rest.RestWrapperGetResource;
import org.vgcs.assignment.graphql.model.VehicleComplete;
import org.vgcs.assignment.graphql.model.Vehicle;
import org.vgcs.assignment.persistance.repository.VehicleRepo;
import org.vgcs.assignment.restservice.VehicleService;
;
import java.util.concurrent.CompletableFuture;

@Component
public class VehicleResolver implements GraphQLResolver<VehicleComplete> {
    private static final String FIND_BY_ID = "findById";
    private final VehicleService vehicleService;
    private final VehicleRepo vehicleRepo;
    private final WrapperGetResourcesByReflection<Vehicle, VehicleRepo> mongoWrapper;
    private final WrapperGetResource<Vehicle, VehicleService, String> restWrapper;

    public VehicleResolver(VehicleService vehicleService,
                           VehicleRepo vehicleRepo,
                           MongoWrapper<Vehicle, VehicleRepo> mongoWrapper,
                           RestWrapperGetResource<Vehicle, VehicleService> restWrapper) {
        this.vehicleService = vehicleService;
        this.vehicleRepo = vehicleRepo;
        this.restWrapper = restWrapper;
        this.mongoWrapper = mongoWrapper;
    }


    public CompletableFuture<DataFetcherResult<Vehicle>> vehicle(VehicleComplete vehicleComplete) {
        return CompletableFuture.supplyAsync(() -> {
            var id = vehicleComplete.getId();
            var result = DataFetcherResult.<Vehicle>newResult();

            var mongoResponse = mongoWrapper.get(vehicleRepo, FIND_BY_ID, id);
            if (mongoResponse.hasData() && !mongoResponse.hasError()) {
                result.data(mongoResponse.getData());
            } else {
                var restResponse  = restWrapper.get(vehicleService, id);
                if (restResponse.hasData()) {
                        Vehicle v = restResponse.getData();
                        vehicleRepo.save(v);
                        result.data(v);
                    }

                    if (restResponse.hasError()) {
                        result.error(new GenericGraphQLError(restResponse.getErrorMessage()));
                    }
                }

            return result.build();
        });
    }
}
