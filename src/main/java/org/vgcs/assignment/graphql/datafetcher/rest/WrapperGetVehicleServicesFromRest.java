package org.vgcs.assignment.graphql.datafetcher.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.vgcs.assignment.graphql.datafetcher.DtoWrapper;
import org.vgcs.assignment.graphql.datafetcher.Mapper;
import org.vgcs.assignment.graphql.datafetcher.WrapperGetResourceById;
import org.vgcs.assignment.graphql.model.VehicleServices;
import org.vgcs.assignment.restservice.VehicleServicesService;
import org.vgcs.assignment.restservice.dto.VehicleServicesResponseDTO;
import org.vgcs.assignment.restservice.exception.RestCallException;

@Component
@Qualifier("restWrapper_getVehicleServices")
public class WrapperGetVehicleServicesFromRest implements WrapperGetResourceById<VehicleServicesService,VehicleServices, String> {

    public DtoWrapper<VehicleServices> get(VehicleServicesService service, String id) {
        Logger logger = LoggerFactory.getLogger(WrapperGetVehicleServicesFromRest.class);
        DtoWrapper<VehicleServices> responseWrapper = new DtoWrapper<>();

        VehicleServicesResponseDTO vehicleServicesResponseDTO = null;
        String errorMessage = "";
        try {
            logger.info("WRAPPER: fetching REST data");
            vehicleServicesResponseDTO = service.get(id);
        } catch (RestCallException ex) {
            logger.error(ex.getMessage());
            errorMessage = ex.getErrorCode() + ":" + ex.getMessage() + ":" + ex.getResourceId();
        }

        responseWrapper.setData(Mapper.from(vehicleServicesResponseDTO));
        responseWrapper.setErrorMessage(errorMessage);

        return responseWrapper;
    }
}

