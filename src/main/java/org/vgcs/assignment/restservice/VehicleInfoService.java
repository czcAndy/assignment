package org.vgcs.assignment.restservice;


import org.vgcs.assignment.restservice.dto.VehicleInfoResponseDTO;
import org.vgcs.assignment.restservice.exception.RestCallException;

public interface VehicleInfoService extends GetService<VehicleInfoResponseDTO, String> {
    VehicleInfoResponseDTO get(String id) throws RestCallException;
}
