package org.vgcs.assignment.graphql.model;

public class VehicleComplete {
    private String id;
    private VehicleSimple vehicleSimple;
    private VehicleDetails vehicleDetails;
    private VehicleServicesWithCommStatus vehicleServicesWithCommStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
