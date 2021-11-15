package org.vgcs.assignment.graphql.datafetcher.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.vgcs.assignment.graphql.datafetcher.DtoWrapper;
import org.vgcs.assignment.graphql.datafetcher.Mapper;
import org.vgcs.assignment.graphql.datafetcher.WrapperGetResourceById;
import org.vgcs.assignment.graphql.model.VehicleInfo;
import org.vgcs.assignment.restservice.VehicleInfoService;
import org.vgcs.assignment.restservice.dto.VehicleInfoResponseDTO;
import org.vgcs.assignment.restservice.exception.RestCallException;

@Component
@Qualifier("restWrapper_getVehicleInfo")
public class WrapperGetVehicleInfoFromRest implements WrapperGetResourceById<VehicleInfoService, VehicleInfo, String> {

    @Override
    public DtoWrapper<VehicleInfo> get(VehicleInfoService service, String id) {
        Logger logger = LoggerFactory.getLogger(WrapperGetVehicleInfoFromRest.class);
        DtoWrapper<VehicleInfo> responseWrapper = new DtoWrapper<>();

        VehicleInfoResponseDTO vehicleInfoResponseDTO = null;
        String errorMessage = "";
        try {
            logger.info("WRAPPER: fetching REST data");
            vehicleInfoResponseDTO = service.get(id);
        } catch (RestCallException ex) {
            logger.error(ex.getMessage());
            errorMessage = ex.getErrorCode() + ":" + ex.getMessage() + ":" + ex.getResourceId();
        }

        responseWrapper.setData(Mapper.from(vehicleInfoResponseDTO));
        responseWrapper.setErrorMessage(errorMessage);

        return responseWrapper;
    }
}
