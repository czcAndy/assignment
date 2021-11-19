package org.vgcs.assignment.restservice.dto;


public record VehicleInfoResponseDTO(String msidn, String engineStatus, String fleet, String brand,
                                     String countryOfOperation, String chassisNumber, String chassisSeries) {
}
