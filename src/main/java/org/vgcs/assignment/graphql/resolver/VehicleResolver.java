package org.vgcs.assignment.graphql.resolver;

import graphql.execution.DataFetcherResult;
import graphql.kickstart.execution.error.GenericGraphQLError;
import graphql.kickstart.tools.GraphQLResolver;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.vgcs.assignment.graphql.datafetcher.WrapperGetAllResources;
import org.vgcs.assignment.graphql.datafetcher.DtoWrapper;
import org.vgcs.assignment.graphql.datafetcher.mongo.WrapperGetVehicleWithIdFromMongo;
import org.vgcs.assignment.graphql.model.VehicleComplete;
import org.vgcs.assignment.graphql.model.Vehicle;
import org.vgcs.assignment.persistance.repository.VehicleRepo;
import org.vgcs.assignment.restservice.VehicleService;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
public class VehicleResolver implements GraphQLResolver<VehicleComplete> {
    Logger logger = LoggerFactory.getLogger(VehicleResolver.class);

    private final VehicleService vehicleService;
    private final VehicleRepo vehicleRepo;
    private final WrapperGetAllResources wrapperAllVehiclesRest;
    private final WrapperGetVehicleWithIdFromMongo wrapperGetVehicleWithIdFromMongo;

    public VehicleResolver(VehicleService vehicleService, VehicleRepo vehicleRepo,
                           @Qualifier("restWrapper_getAllVehicles") WrapperGetAllResources wrapperAllVehiclesRest,
                           @Qualifier("mongoWrapper_getVehicleById") WrapperGetVehicleWithIdFromMongo wrapperGetVehicleWithIdFromMongo) {
        this.vehicleService = vehicleService;
        this.vehicleRepo = vehicleRepo;
        this.wrapperAllVehiclesRest = wrapperAllVehiclesRest;
        this.wrapperGetVehicleWithIdFromMongo = wrapperGetVehicleWithIdFromMongo;
    }


    public CompletableFuture<DataFetcherResult<Vehicle>> vehicle(VehicleComplete vehicleComplete) {
        return CompletableFuture.supplyAsync(() -> {
            logger.info("GRAPHQL: Resolving dependencies for the Vehicle Object");
            logger.info("GRAPHQL: Looking in the DB first");
            var id = vehicleComplete.getId();
            var result = DataFetcherResult.<Vehicle>newResult();
            var mongoWrapper = wrapperGetVehicleWithIdFromMongo.get(vehicleRepo, id);
            if (mongoWrapper.hasData() && !mongoWrapper.hasError()) {
                logger.info("GRAPHQL: Data fetched from the DB. Returning it!");
                result.data(mongoWrapper.getData());
            }
            else {
                logger.info("GRAPHQL: Data does not exist in the DB. Looking into the REST services!");
                var restWrapper = wrapperAllVehiclesRest.getAll(vehicleService);
                if (restWrapper.hasData()) {
                    Optional<Vehicle> vehicleOptional = getVehicle(restWrapper, id);
                    if (vehicleOptional.isPresent()){
                        Vehicle v = vehicleOptional.get();
                        logger.info("GRAPHQL: Saving the missing data in the DB!");
                        vehicleRepo.save(v);
                        logger.info("Saved successfully");
                        result.data(v);
                    }

                    if (restWrapper.hasError()) {
                        result.error(new GenericGraphQLError(restWrapper.getErrorMessage()));
                    }
                }
            }

            return result.build();
        });
    }

    @NotNull
    private Optional<Vehicle> getVehicle(DtoWrapper<List<Vehicle>> vehicleListWrapper, String id) {
        var vehicleDto = vehicleListWrapper.getData();
        var vehicleOptional = vehicleDto.stream()
                .filter(v -> v.getId().equals(id))
                .findFirst();
        return vehicleOptional;
    }
}
