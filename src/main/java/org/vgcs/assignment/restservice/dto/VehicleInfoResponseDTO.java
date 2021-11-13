package org.vgcs.assignment.restservice.dto;


public record VehicleInfoResponseDTO(String msisdn, String engineStatus, String fleet, String brand,
                                     String countryOfOperation, String chassisNumber, String chassisSeries) {
}
