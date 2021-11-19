package org.vgcs.assignment.graphql.resolver.vehicle;

import graphql.execution.DataFetcherResult;
import graphql.kickstart.execution.error.GenericGraphQLError;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

import org.vgcs.assignment.graphql.datafetcher.WrapperGetResource;
import org.vgcs.assignment.graphql.datafetcher.WrapperGetResourcesByReflection;
import org.vgcs.assignment.persistance.VehicleInfoRepo;
import org.vgcs.assignment.persistance.model.VehicleComplete;
import org.vgcs.assignment.persistance.model.VehicleInfo;
import org.vgcs.assignment.restservice.VehicleInfoService;

import java.util.concurrent.CompletableFuture;

@Component
public class VehicleInfoResolver implements GraphQLResolver<VehicleComplete> {
    private static final String FIND_BY_ID = "findById";
    private final VehicleInfoRepo vehicleInfoRepo;
    private final VehicleInfoService vehicleInfoService;
    private final WrapperGetResourcesByReflection<VehicleInfo, VehicleInfoRepo> mongoWrapper;
    private final WrapperGetResource<VehicleInfo, VehicleInfoService, String> restWrapper;

    public VehicleInfoResolver(VehicleInfoRepo vehicleInfoRepo,
                               VehicleInfoService vehicleInfoService,
                               WrapperGetResourcesByReflection<VehicleInfo, VehicleInfoRepo> mongoWrapper,
                               WrapperGetResource<VehicleInfo, VehicleInfoService, String> restWrapper) {
        this.vehicleInfoRepo = vehicleInfoRepo;
        this.vehicleInfoService = vehicleInfoService;
        this.mongoWrapper = mongoWrapper;
        this.restWrapper = restWrapper;
    }


    public CompletableFuture<DataFetcherResult<VehicleInfo>> vehicleInfo(VehicleComplete vehicleComplete) {
        return CompletableFuture.supplyAsync(() -> {
            var result = DataFetcherResult.<VehicleInfo>newResult();
            var responseMongo = mongoWrapper.get(vehicleInfoRepo, FIND_BY_ID, vehicleComplete.getId());
            if(responseMongo.hasData() && !responseMongo.hasError()){
                result.data(responseMongo.getData());
            } else {
                var responseRest = restWrapper.get(vehicleInfoService, vehicleComplete.getId());
                if (responseRest.hasData()) {
                    var vehicleInfo = responseRest.getData();
                    vehicleInfo.setId(vehicleComplete.getId());
                    vehicleInfoRepo.save(vehicleInfo);
                    result.data(vehicleInfo);
                }

                if (responseRest.hasError()) {
                    result.error(new GenericGraphQLError(responseRest.getErrorMessage()));
                }
            }
                return result.build();
        });
    }
}
