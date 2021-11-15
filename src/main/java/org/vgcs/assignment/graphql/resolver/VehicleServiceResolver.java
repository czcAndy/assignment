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
import org.vgcs.assignment.graphql.model.VehicleServices;
import org.vgcs.assignment.persistance.repository.VehicleServicesRepo;
import org.vgcs.assignment.restservice.VehicleServicesService;

import java.util.concurrent.CompletableFuture;

@Component
public class VehicleServiceResolver implements GraphQLResolver<VehicleComplete> {
    Logger logger = LoggerFactory.getLogger(VehicleServiceResolver.class);

    private final VehicleServicesService vehicleServicesService;
    private final VehicleServicesRepo vehicleServicesRepo;
    private final WrapperGetResourceById wrapperVehicleServicesRest;
    private final WrapperGetResourceById wrapperVehicleServicesMongo;

    public VehicleServiceResolver(VehicleServicesService vehicleServicesService, VehicleServicesRepo vehicleServicesRepo,
                                  @Qualifier("restWrapper_getVehicleServices") WrapperGetResourceById wrapperVehicleServicesRest,
                                  @Qualifier("mongoWrapper_getVehicleServices") WrapperGetResourceById wrapperVehicleServicesMongo) {
        this.vehicleServicesService = vehicleServicesService;
        this.vehicleServicesRepo = vehicleServicesRepo;
        this.wrapperVehicleServicesRest = wrapperVehicleServicesRest;
        this.wrapperVehicleServicesMongo = wrapperVehicleServicesMongo;
    }


    public CompletableFuture<DataFetcherResult<VehicleServices>> vehicleServices(VehicleComplete vehicleComplete) {
        return CompletableFuture.supplyAsync(() -> {
            logger.info("GRAPHQL: Resolving dependencies for the VehicleService Object");
            logger.info("GRAPHQL: Looking in the DB first");
            var result = DataFetcherResult.<VehicleServices>newResult();
            var servicesMongoWrapper = wrapperVehicleServicesMongo.get(vehicleServicesRepo, vehicleComplete.getId());
            if(servicesMongoWrapper.hasData() && !servicesMongoWrapper.hasError()) {
                logger.info("GRAPHQL: Data fetched from the DB. Returning it!");
                result.data((VehicleServices) servicesMongoWrapper.getData());
            } else {
                logger.info("GRAPHQL: Data does not exist in the DB. Looking into the REST services!");
                var servicesDTOWrapper = wrapperVehicleServicesRest.get(vehicleServicesService, vehicleComplete.getId());
                if(servicesDTOWrapper.hasData()) {
                    logger.info("GRAPHQL: Data does not exist in the DB. Looking into the REST services!");
                    var vehicleServices = (VehicleServices)servicesDTOWrapper.getData();
                    vehicleServices.setId(vehicleComplete.getId());
                    logger.info("GRAPHQL: Saving the missing data in the DB!");
                    vehicleServicesRepo.save(vehicleServices);
                    logger.info("Saved successfully");
                    result.data((VehicleServices) vehicleServices);
                }

                if(servicesDTOWrapper.hasError()) {
                    result.error(new GenericGraphQLError(servicesDTOWrapper.getErrorMessage()));
                }
            }
            return result.build();
        });
    }
}

