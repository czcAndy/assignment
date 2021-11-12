package dto;

import model.Vehicle;

import java.util.List;

public record VehicleResponseDTO(List<Vehicle> vehicles) {
}
