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
import org.vgcs.assignment.graphql.model.VehicleServices;
import org.vgcs.assignment.persistance.repository.VehicleServicesRepo;
import org.vgcs.assignment.restservice.VehicleServicesService;

import java.util.concurrent.CompletableFuture;

@Component
public class VehicleServiceResolver implements GraphQLResolver<VehicleComplete> {
    private static final String FIND_BY_ID = "findById";
    private final VehicleServicesService vehicleServicesService;
    private final VehicleServicesRepo vehicleServicesRepo;
    private final WrapperGetResourcesByReflection<VehicleServices, VehicleServicesRepo> mongoWrapper;
    private final WrapperGetResource<VehicleServices, VehicleServicesService, String> restWrapper;

    public VehicleServiceResolver(VehicleServicesService vehicleServicesService,
                                  VehicleServicesRepo vehicleServicesRepo,
                                  MongoWrapper<VehicleServices, VehicleServicesRepo> mongoWrapper,
                                  RestWrapperGetResource<VehicleServices, VehicleServicesService> restWrapper) {
        this.vehicleServicesService = vehicleServicesService;
        this.vehicleServicesRepo = vehicleServicesRepo;
        this.mongoWrapper = mongoWrapper;
        this.restWrapper = restWrapper;
    }

    public CompletableFuture<DataFetcherResult<VehicleServices>> vehicleServices(VehicleComplete vehicleComplete) {
        return CompletableFuture.supplyAsync(() -> {
            var result = DataFetcherResult.<VehicleServices>newResult();
            var resultMongo = mongoWrapper.get(vehicleServicesRepo, FIND_BY_ID, vehicleComplete.getId());
            if(resultMongo.hasData() && !resultMongo.hasError()) {
                result.data(resultMongo.getData());
            } else {
                var resultRest = restWrapper.get(vehicleServicesService, vehicleComplete.getId());
                if(resultRest.hasData()) {
                    var vehicleServices = resultRest.getData();
                    vehicleServices.setId(vehicleComplete.getId());
                    vehicleServicesRepo.save(vehicleServices);
                    result.data(vehicleServices);
                }

                if(resultRest.hasError()) {
                    result.error(new GenericGraphQLError(resultRest.getErrorMessage()));
                }
            }
            return result.build();
        });
    }
}

