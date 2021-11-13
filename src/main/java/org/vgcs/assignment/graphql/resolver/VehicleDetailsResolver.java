package org.vgcs.assignment.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;
import org.vgcs.assignment.graphql.model.VehicleComplete;
import org.vgcs.assignment.graphql.model.VehicleDetails;
import org.vgcs.assignment.restservice.VehicleInfoService;
import org.vgcs.assignment.restservice.dto.VehicleInfoResponseDTO;

import java.util.concurrent.CompletableFuture;

@Component
public class VehicleDetailsResolver implements GraphQLResolver<VehicleComplete> {
    private final VehicleInfoService vehicleInfoService;

    public VehicleDetailsResolver(VehicleInfoService vehicleInfoService) {
        this.vehicleInfoService = vehicleInfoService;
    }

    public CompletableFuture<VehicleDetails> vehicleDetails(VehicleComplete vehicleComplete) {
        return CompletableFuture.supplyAsync(() -> {
            VehicleInfoResponseDTO vehicleInfoResponseDTO = vehicleInfoService.getVehiclesById(vehicleComplete.getId());
            VehicleDetails vehicleDetails = new VehicleDetails();
            vehicleDetails.setMsidn(vehicleInfoResponseDTO.msidn());
            vehicleDetails.setEngineStatus(vehicleInfoResponseDTO.engineStatus());
            vehicleDetails.setFleet(vehicleInfoResponseDTO.fleet());
            vehicleDetails.setBrand(vehicleInfoResponseDTO.brand());
            vehicleDetails.setCountryOfOperation(vehicleInfoResponseDTO.countryOfOperation());
            vehicleDetails.setChassisNumber(vehicleInfoResponseDTO.chassisNumber());
            vehicleDetails.setChassisSeries(vehicleInfoResponseDTO.chassisSeries());

            return vehicleDetails;
        });
    }
}
