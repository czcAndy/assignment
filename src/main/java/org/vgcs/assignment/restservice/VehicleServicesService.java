package org.vgcs.assignment.restservice;

import org.vgcs.assignment.restservice.dto.VehicleServicesResponseDTO;
import org.vgcs.assignment.restservice.exception.RestCallException;

public interface VehicleServicesService {
    VehicleServicesResponseDTO getVehicleServices(String id) throws RestCallException;
}
