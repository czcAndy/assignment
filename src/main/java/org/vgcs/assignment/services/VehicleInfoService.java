package org.vgcs.assignment.services;

import org.vgcs.assignment.dto.VehicleInfoResponseDTO;
import org.vgcs.assignment.dto.VehicleResponseDTO;

public interface VehicleInfoService  {
    VehicleInfoResponseDTO getVehiclesById(String id);
}
