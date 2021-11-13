package org.vgcs.assignment.restservice.dto;

import org.vgcs.assignment.restservice.model.Service;

import java.util.List;

public record VehicleServicesResponseDTO(String communicationStatus, List<Service> services) {
}
