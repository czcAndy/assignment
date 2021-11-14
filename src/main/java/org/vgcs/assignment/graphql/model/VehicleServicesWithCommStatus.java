package org.vgcs.assignment.graphql.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
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
