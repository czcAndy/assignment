package org.vgcs.assignment.graphql.resolver;

import graphql.execution.DataFetcherResult;
import graphql.kickstart.execution.error.GenericGraphQLError;
import graphql.kickstart.tools.GraphQLResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.vgcs.assignment.graphql.datafetcher.WrapperGetResourceById;
import org.vgcs.assignment.graphql.model.VehicleComplete;
import org.vgcs.assignment.graphql.model.VehicleInfo;
import org.vgcs.assignment.persistance.repository.VehicleInfoRepo;
import org.vgcs.assignment.restservice.VehicleInfoService;

import java.util.concurrent.CompletableFuture;

@Component
public class VehicleInfoResolver implements GraphQLResolver<VehicleComplete> {
    Logger logger = LoggerFactory.getLogger(VehicleServiceResolver.class);

    private final VehicleInfoService vehicleInfoService;
    private final VehicleInfoRepo vehicleInfoRepo;
    private final WrapperGetResourceById wrapperVehicleInfoRest;
    private final WrapperGetResourceById wrapperVehicleInfoMongo;

    public VehicleInfoResolver(VehicleInfoService vehicleInfoService, VehicleInfoRepo vehicleInfoRepo,
                               @Qualifier("restWrapper_getVehicleInfo") WrapperGetResourceById wrapperVehicleInfoRest,
                               @Qualifier("mongoWrapper_getVehicleInfo") WrapperGetResourceById wrapperVehicleInfoMongo) {
        this.vehicleInfoService = vehicleInfoService;
        this.vehicleInfoRepo = vehicleInfoRepo;
        this.wrapperVehicleInfoRest = wrapperVehicleInfoRest;
        this.wrapperVehicleInfoMongo = wrapperVehicleInfoMongo;
    }

    public CompletableFuture<DataFetcherResult<VehicleInfo>> vehicleInfo(VehicleComplete vehicleComplete) {
        return CompletableFuture.supplyAsync(() -> {
            logger.info("GRAPHQL: Resolving dependencies for the VehicleInfo Object");
            logger.info("GRAPHQL: Looking in the DB first");
            var result = DataFetcherResult.<VehicleInfo>newResult();
            var mongoWrapper = wrapperVehicleInfoMongo.get(vehicleInfoRepo, vehicleComplete.getId());
            if(mongoWrapper.hasData() && !mongoWrapper.hasError()){
                logger.info("GRAPHQL: Data fetched from the DB. Returning it!");
                result.data((VehicleInfo) mongoWrapper.getData());
            } else {
                logger.info("GRAPHQL: Data does not exist in the DB. Looking into the REST services!");
                var restWrapper = wrapperVehicleInfoRest.get(vehicleInfoService, vehicleComplete.getId());

                if (restWrapper.hasData()) {
                    var vehicleInfo = (VehicleInfo)restWrapper.getData();
                    vehicleInfo.setId(vehicleComplete.getId());
                    logger.info("GRAPHQL: Saving the missing data in the DB!");
                    vehicleInfoRepo.save(vehicleInfo);
                    logger.info("Saved successfully");
                    result.data((VehicleInfo) vehicleInfo);
                }

                if (restWrapper.hasError()) {
                    result.error(new GenericGraphQLError(restWrapper.getErrorMessage()));
                }
            }
                return result.build();
        });
    }
}
