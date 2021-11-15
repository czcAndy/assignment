package org.vgcs.assignment.graphql.helper;

import graphql.com.google.common.collect.Lists;
import org.vgcs.assignment.restservice.VehicleInfoService;
import org.vgcs.assignment.restservice.VehicleService;
import org.vgcs.assignment.restservice.VehicleServicesService;
import org.vgcs.assignment.restservice.dto.VehicleDTO;
import org.vgcs.assignment.restservice.dto.VehicleInfoResponseDTO;
import org.vgcs.assignment.restservice.dto.VehicleServicesResponseDTO;
import org.vgcs.assignment.restservice.exception.RestCallException;

import java.util.List;


public class DtoFrom {

    public static DtoWrapper<List<VehicleDTO>> vehicleService(VehicleService vehicleService) {
        DtoWrapper<List<VehicleDTO>> responseWrapper = new DtoWrapper<>();

        List<VehicleDTO> vehicleResponseDto = null;
        String errorMessage = "";
        try {
            vehicleResponseDto = Lists.newArrayList(vehicleService.getVehicles());
        } catch (RestCallException ex) {
            errorMessage = ex.getErrorCode() + ":" + ex.getMessage() + ":" + ex.getResourceId();
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
            errorMessage = ex.getErrorCode() + ":" + ex.getMessage() + ":" + ex.getResourceId();
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
            errorMessage = ex.getErrorCode() + ":" + ex.getMessage() + ":" + ex.getResourceId();
        }

        responseWrapper.setData(vehicleServicesResponseDTO);
        responseWrapper.setErrorMessage(errorMessage);

        return responseWrapper;
    }

}
