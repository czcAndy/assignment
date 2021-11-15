package org.vgcs.assignment.graphql.resolver;

import graphql.execution.DataFetcherResult;
import graphql.kickstart.execution.error.GenericGraphQLError;
import graphql.kickstart.tools.GraphQLResolver;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.vgcs.assignment.graphql.helper.DtoFrom;
import org.vgcs.assignment.graphql.helper.DtoWrapper;
import org.vgcs.assignment.graphql.helper.Mapper;
import org.vgcs.assignment.graphql.model.VehicleComplete;
import org.vgcs.assignment.graphql.model.Vehicle;
import org.vgcs.assignment.restservice.VehicleService;
import org.vgcs.assignment.restservice.dto.VehicleDTO;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
public class VehicleResolver implements GraphQLResolver<VehicleComplete> {
    private final VehicleService vehicleService;

    public VehicleResolver(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    public CompletableFuture<DataFetcherResult<Vehicle>> vehicle(VehicleComplete vehicleComplete) {
        return CompletableFuture.supplyAsync(() -> {
            var result = DataFetcherResult.<Vehicle>newResult();
            var vehicleListWrapper = DtoFrom.vehicleService(vehicleService);

            if (vehicleListWrapper.hasData()) {
                Optional<VehicleDTO> vehicleOptional = getVehicleDto(vehicleListWrapper, vehicleComplete.getId());
                if (vehicleOptional.isPresent()){
                    Vehicle v = Mapper.from(vehicleOptional.get());
                    result.data(v);
                }
            }

            if (vehicleListWrapper.hasError()) {
                result.error(new GenericGraphQLError(vehicleListWrapper.getErrorMessage()));
            }

            return result.build();
        });
    }

    @NotNull
    private Optional<VehicleDTO> getVehicleDto(DtoWrapper<List<VehicleDTO>> vehicleListWrapper, String id) {
        var vehicleDto = vehicleListWrapper.getData();
        var vehicleOptional = vehicleDto.stream()
                .filter(v -> v.id().equals(id))
                .findFirst();
        return vehicleOptional;
    }
}
