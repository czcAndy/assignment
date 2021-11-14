package org.vgcs.assignment.graphql.resolver;

import graphql.execution.DataFetcherResult;
import graphql.kickstart.execution.error.GenericGraphQLError;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;
import org.vgcs.assignment.graphql.helper.DtoFrom;
import org.vgcs.assignment.graphql.model.VehicleComplete;
import org.vgcs.assignment.graphql.model.VehicleDetails;
import org.vgcs.assignment.restservice.VehicleInfoService;

import java.util.concurrent.CompletableFuture;

@Component
public class VehicleDetailsResolver implements GraphQLResolver<VehicleComplete> {
    private final VehicleInfoService vehicleInfoService;

    public VehicleDetailsResolver(VehicleInfoService vehicleInfoService) {
        this.vehicleInfoService = vehicleInfoService;
    }

    public CompletableFuture<DataFetcherResult<VehicleDetails>> vehicleDetails(VehicleComplete vehicleComplete) {
        return CompletableFuture.supplyAsync(() -> {
            var result = DataFetcherResult.<VehicleDetails>newResult();
            var vehicleInfoWrapper = DtoFrom.vehicleInfoService(vehicleInfoService, vehicleComplete.getId());

            if (vehicleInfoWrapper.hasData()) {
                var vehicleInfoResponseDTO = vehicleInfoWrapper.getData();
                VehicleDetails vehicleInfo = new VehicleDetails();
                vehicleInfo.setMsidn(vehicleInfoResponseDTO.msidn());
                vehicleInfo.setEngineStatus(vehicleInfoResponseDTO.engineStatus());
                vehicleInfo.setFleet(vehicleInfoResponseDTO.fleet());
                vehicleInfo.setBrand(vehicleInfoResponseDTO.brand());
                vehicleInfo.setCountryOfOperation(vehicleInfoResponseDTO.countryOfOperation());
                vehicleInfo.setChassisNumber(vehicleInfoResponseDTO.chassisNumber());
                vehicleInfo.setChassisSeries(vehicleInfoResponseDTO.chassisSeries());

                result.data(vehicleInfo);
            }

            if (vehicleInfoWrapper.hasError()) {
                result.error(new GenericGraphQLError(vehicleInfoWrapper.getErrorMessage()));
            }

            return result.build();
        });
    }
}
