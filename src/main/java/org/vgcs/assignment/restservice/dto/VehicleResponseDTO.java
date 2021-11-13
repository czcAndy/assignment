package org.vgcs.assignment.restservice.dto;

import org.vgcs.assignment.restservice.model.Vehicle;

import java.util.List;

public record VehicleResponseDTO(List<Vehicle> vehicles) {
}
