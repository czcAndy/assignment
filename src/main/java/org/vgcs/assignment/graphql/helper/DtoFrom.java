package org.vgcs.assignment.graphql.helper;

import org.vgcs.assignment.restservice.VehicleInfoService;
import org.vgcs.assignment.restservice.VehicleService;
import org.vgcs.assignment.restservice.VehicleServicesService;
import org.vgcs.assignment.restservice.dto.VehicleInfoResponseDTO;
import org.vgcs.assignment.restservice.dto.VehicleResponseDTO;
import org.vgcs.assignment.restservice.dto.VehicleServicesResponseDTO;
import org.vgcs.assignment.restservice.exception.RestCallException;


public class DtoFrom {

    public static DtoWrapper<VehicleResponseDTO> vehicleService(VehicleService vehicleService) {
        DtoWrapper<VehicleResponseDTO> responseWrapper = new DtoWrapper<>();

        VehicleResponseDTO vehicleResponseDto = null;
        String errorMessage = "";
        try {
            vehicleResponseDto = vehicleService.getVehicles();
        } catch (RestCallException ex) {
            errorMessage = ex.getErrorCode() + ": " + ex.getMessage();
        }

        responseWrapper.setData(vehicleResponseDto);
        responseWrapper.setErrorMessage(errorMessage);

        return responseWrapper;
    }

    public static DtoWrapper<VehicleInfoResponseDTO> vehicleInfoService(VehicleInfoService vehicleInfoService, String id) {
        DtoWrapper<VehicleInfoResponseDTO> responseWrapper = new DtoWrapper<>();

        VehicleInfoResponseDTO vehicleInfoResponseDTO = null;
        String errorMessage = "";
        try {
            vehicleInfoResponseDTO = vehicleInfoService.getVehiclesById(id);
        } catch (RestCallException ex) {
            errorMessage = ex.getErrorCode() + ": " + ex.getMessage();
        }

        responseWrapper.setData(vehicleInfoResponseDTO);
        responseWrapper.setErrorMessage(errorMessage);

        return responseWrapper;
    }

    public static DtoWrapper<VehicleServicesResponseDTO> vehicleServicesService(VehicleServicesService vehicleServicesService,
                                                                                String id) {
        DtoWrapper<VehicleServicesResponseDTO> responseWrapper = new DtoWrapper<>();

        VehicleServicesResponseDTO vehicleServicesResponseDTO = null;
        String errorMessage = "";
        try {
            vehicleServicesResponseDTO = vehicleServicesService.getVehicleServices(id);
        } catch (RestCallException ex) {
            errorMessage = ex.getErrorCode() + ": " + ex.getMessage();
        }

        responseWrapper.setData(vehicleServicesResponseDTO);
        responseWrapper.setErrorMessage(errorMessage);

        return responseWrapper;
    }

}
