package org.vgcs.assignment.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;
import org.vgcs.assignment.graphql.model.VehicleComplete;
import org.vgcs.assignment.graphql.model.VehicleSimple;
import org.vgcs.assignment.restservice.VehicleService;
import org.vgcs.assignment.restservice.dto.VehicleResponseDTO;


import java.util.concurrent.CompletableFuture;

@Component
public class VehicleSimpleResolver implements GraphQLResolver<VehicleComplete> {
    private final VehicleService vehicleService;

    public VehicleSimpleResolver(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    public CompletableFuture<VehicleSimple> vehicleSimple(VehicleComplete vehicleComplete) {
        return CompletableFuture.supplyAsync(() -> {
            var startTime = System.currentTimeMillis();
            VehicleResponseDTO vehicleResponseDTO = vehicleService.getVehicles();
            var endTime = System.currentTimeMillis();
            System.out.println("In vehicleSimple" + (endTime - startTime));
            String vehicleName = vehicleResponseDTO.vehicles().stream()
                    .filter(v -> v.id().equals(vehicleComplete.getId())).findFirst().get().name();

            VehicleSimple vehicleSimple = new VehicleSimple();
            vehicleSimple.setId(vehicleComplete.getId());
            vehicleSimple.setName(vehicleName);

            return vehicleSimple;
        });
    }
}
