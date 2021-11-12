package org.vgcs.assignment.dto;

import org.vgcs.assignment.model.Service;

import java.util.List;

public record ServiceResponseDTO(String communicationStatus, List<Service> services) {
}
