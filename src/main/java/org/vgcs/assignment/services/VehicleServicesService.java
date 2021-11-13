package org.vgcs.assignment.services;

import org.vgcs.assignment.dto.VehicleServicesResponseDTO;

public interface VehicleServicesService {
    VehicleServicesResponseDTO getVehicleServices(String id);
}
