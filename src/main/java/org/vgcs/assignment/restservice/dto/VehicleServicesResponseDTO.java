package org.vgcs.assignment.restservice.dto;

import java.util.List;

public record VehicleServicesResponseDTO(String communicationStatus, List<ServiceDTO> services) {
}
