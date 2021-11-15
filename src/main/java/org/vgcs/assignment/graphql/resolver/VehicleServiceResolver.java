package org.vgcs.assignment.graphql.resolver;

import graphql.execution.DataFetcherResult;
import graphql.kickstart.execution.error.GenericGraphQLError;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;
import org.vgcs.assignment.graphql.helper.DtoFrom;
import org.vgcs.assignment.graphql.helper.Mapper;
import org.vgcs.assignment.graphql.model.VehicleComplete;
import org.vgcs.assignment.graphql.model.VehicleServices;
import org.vgcs.assignment.restservice.VehicleServicesService;

import java.util.concurrent.CompletableFuture;

@Component
public class VehicleServiceResolver implements GraphQLResolver<VehicleComplete> {
    private final VehicleServicesService vehicleServicesService;

    public VehicleServiceResolver(VehicleServicesService vehicleServicesService) {
        this.vehicleServicesService = vehicleServicesService;
    }

    public CompletableFuture<DataFetcherResult<VehicleServices>> vehicleServices(VehicleComplete vehicleComplete) {
        return CompletableFuture.supplyAsync(() -> {
            var result = DataFetcherResult.<VehicleServices>newResult();
            var servicesDTOWrapper = DtoFrom.vehicleServicesService(vehicleServicesService, vehicleComplete.getId());

            if(servicesDTOWrapper.hasData()) {
                var vehicleServices = Mapper.from(servicesDTOWrapper.getData());
                result.data(vehicleServices);
            }

            if(servicesDTOWrapper.hasError()) {
                result.error(new GenericGraphQLError(servicesDTOWrapper.getErrorMessage()));
            }

            return result.build();
        });
    }
}

