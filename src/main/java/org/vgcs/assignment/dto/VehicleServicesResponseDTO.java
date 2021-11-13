package org.vgcs.assignment.dto;

import org.vgcs.assignment.model.Service;

import java.util.List;

public record VehicleServicesResponseDTO(String communicationStatus, List<Service> services) {
}
