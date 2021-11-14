package org.vgcs.assignment.restservice;

import org.vgcs.assignment.restservice.dto.VehicleInfoResponseDTO;
import org.vgcs.assignment.restservice.exception.RestCallException;

public interface VehicleInfoService {
    VehicleInfoResponseDTO getVehiclesById(String id) throws RestCallException;
}
