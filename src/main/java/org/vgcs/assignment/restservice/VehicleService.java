package org.vgcs.assignment.restservice;

import org.vgcs.assignment.restservice.dto.VehicleResponseDTO;
import org.vgcs.assignment.restservice.exception.RestCallException;

public interface VehicleService {
    VehicleResponseDTO getVehicles() throws RestCallException;
}
