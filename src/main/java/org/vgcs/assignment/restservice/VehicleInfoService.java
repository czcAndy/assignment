package org.vgcs.assignment.restservice;

import org.vgcs.assignment.restservice.dto.VehicleInfoResponseDTO;

public interface VehicleInfoService  {
    VehicleInfoResponseDTO getVehiclesById(String id);
}
