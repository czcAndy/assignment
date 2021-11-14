package org.vgcs.assignment.graphql.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document
public class VehicleComplete {
    @Id
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
