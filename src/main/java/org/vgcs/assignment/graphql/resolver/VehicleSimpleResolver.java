package org.vgcs.assignment.graphql.resolver;

import graphql.execution.DataFetcherResult;
import graphql.kickstart.execution.error.GenericGraphQLError;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;
import org.vgcs.assignment.graphql.helper.DtoFrom;
import org.vgcs.assignment.graphql.model.VehicleComplete;
import org.vgcs.assignment.graphql.model.VehicleSimple;
import org.vgcs.assignment.restservice.VehicleService;

import java.util.concurrent.CompletableFuture;

@Component
public class VehicleSimpleResolver implements GraphQLResolver<VehicleComplete> {
    private final VehicleService vehicleService;

    public VehicleSimpleResolver(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    public CompletableFuture<DataFetcherResult<VehicleSimple>> vehicleSimple(VehicleComplete vehicleComplete) {
        return CompletableFuture.supplyAsync(() -> {
            var result = DataFetcherResult.<VehicleSimple>newResult();
            var vehicleListWrapper = DtoFrom.vehicleService(vehicleService);

            if (vehicleListWrapper.hasData()) {
                var vehicleDto = vehicleListWrapper.getData();

                var vehicleOptional = vehicleDto.vehicles().stream()
                        .filter(v -> v.id().equals(vehicleComplete.getId()))
                        .findFirst();

                if (vehicleOptional.isPresent()){
                    var vehicleSimple = new VehicleSimple();
                    vehicleSimple.setId(vehicleComplete.getId());
                    vehicleSimple.setName(vehicleOptional.get().name());
                    result.data(vehicleSimple);
                }
            }

            if (vehicleListWrapper.hasError()) {
                result.error(new GenericGraphQLError(vehicleListWrapper.getErrorMessage()));
            }

            return result.build();
        });
    }
}
