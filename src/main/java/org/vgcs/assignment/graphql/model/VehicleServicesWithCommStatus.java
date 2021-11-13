package org.vgcs.assignment.graphql.model;

import java.util.List;

public class VehicleServicesWithCommStatus {
    private String communicationStatus;
    private List<Service> services;

    public String getCommunicationStatus() {
        return communicationStatus;
    }

    public void setCommunicationStatus(String communicationStatus) {
        this.communicationStatus = communicationStatus;
    }

    public List<Service> getVehicleServices() {
        return services;
    }

    public void setVehicleServiceList(List<Service> services) {
        this.services = services;
    }
}
