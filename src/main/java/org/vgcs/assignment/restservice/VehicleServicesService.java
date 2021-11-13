package org.vgcs.assignment.restservice;

import org.vgcs.assignment.restservice.dto.VehicleServicesResponseDTO;

public interface VehicleServicesService {
    VehicleServicesResponseDTO getVehicleServices(String id);
}
