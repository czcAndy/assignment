package org.vgcs.assignment.persistance.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleComplete {
    private String id;
    private Vehicle vehicle;
    private VehicleInfo vehicleDetails;
    private VehicleServices vehicleServicesWithCommStatus;

}
