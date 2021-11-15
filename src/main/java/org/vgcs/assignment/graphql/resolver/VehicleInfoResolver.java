package org.vgcs.assignment.graphql.resolver;

import graphql.execution.DataFetcherResult;
import graphql.kickstart.execution.error.GenericGraphQLError;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;
import org.vgcs.assignment.graphql.helper.DtoFrom;
import org.vgcs.assignment.graphql.helper.Mapper;
import org.vgcs.assignment.graphql.model.VehicleComplete;
import org.vgcs.assignment.graphql.model.VehicleInfo;
import org.vgcs.assignment.restservice.VehicleInfoService;

import java.util.concurrent.CompletableFuture;

@Component
public class VehicleInfoResolver implements GraphQLResolver<VehicleComplete> {
    private final VehicleInfoService vehicleInfoService;

    public VehicleInfoResolver(VehicleInfoService vehicleInfoService) {
        this.vehicleInfoService = vehicleInfoService;
    }

    public CompletableFuture<DataFetcherResult<VehicleInfo>> vehicleInfo(VehicleComplete vehicleComplete) {
        return CompletableFuture.supplyAsync(() -> {
            var result = DataFetcherResult.<VehicleInfo>newResult();
            var vehicleInfoWrapper = DtoFrom.vehicleInfoService(vehicleInfoService, vehicleComplete.getId().toString());

            if (vehicleInfoWrapper.hasData()) {
                var vehicleInfo = Mapper.from(vehicleInfoWrapper.getData());
                result.data(vehicleInfo);
            }

            if (vehicleInfoWrapper.hasError()) {
                result.error(new GenericGraphQLError(vehicleInfoWrapper.getErrorMessage()));
            }

            return result.build();
        });
    }
}
