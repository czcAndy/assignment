package org.vgcs.assignment.dto;

import org.vgcs.assignment.model.Vehicle;

import java.util.List;

public record VehicleResponseDTO(List<Vehicle> vehicles) {
}
