package org.vgcs.assignment.restservice;

import org.vgcs.assignment.restservice.dto.VehicleDTO;
import org.vgcs.assignment.restservice.exception.RestCallException;

import java.util.List;

public interface VehicleService {
    List<VehicleDTO> getVehicles() throws RestCallException;
}
