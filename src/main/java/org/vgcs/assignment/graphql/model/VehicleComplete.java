package org.vgcs.assignment.graphql.model;

import java.util.UUID;

public class VehicleComplete {
    private UUID id;
    private VehicleSimple vehicleSimple;
    private VehicleDetails vehicleDetails;
    private VehicleServicesWithCommStatus vehicleServicesWithCommStatus;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public VehicleSimple getVehicleSimple() {
        return vehicleSimple;
    }

    public void setVehicleSimple(VehicleSimple vehicleSimple) {
        this.vehicleSimple = vehicleSimple;
    }

    public VehicleDetails getVehicleDetails() {
        return vehicleDetails;
    }

    public void setVehicleDetails(VehicleDetails vehicleDetails) {
        this.vehicleDetails = vehicleDetails;
    }

    public VehicleServicesWithCommStatus getVehicleServicesWithCommStatus() {
        return vehicleServicesWithCommStatus;
    }

    public void setVehicleServicesWithCommStatus(VehicleServicesWithCommStatus vehicleServicesWithCommStatus) {
        this.vehicleServicesWithCommStatus = vehicleServicesWithCommStatus;
    }
}
