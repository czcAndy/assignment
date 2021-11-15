package org.vgcs.assignment.graphql.datafetcher.rest;

import graphql.com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.vgcs.assignment.graphql.datafetcher.DtoWrapper;
import org.vgcs.assignment.graphql.datafetcher.Mapper;
import org.vgcs.assignment.graphql.datafetcher.WrapperGetAllResources;
import org.vgcs.assignment.graphql.datafetcher.mongo.WrapperGetVehicleServicesMongo;
import org.vgcs.assignment.graphql.model.Vehicle;
import org.vgcs.assignment.restservice.VehicleService;
import org.vgcs.assignment.restservice.dto.VehicleDTO;
import org.vgcs.assignment.restservice.exception.RestCallException;

import java.util.List;

@Component
@Qualifier("restWrapper_getAllVehicles")
public class WrapperGetAllVehiclesFromRest implements WrapperGetAllResources<VehicleService, List<Vehicle>> {
    @Override
    public DtoWrapper<List<Vehicle>> getAll(VehicleService service) {
        Logger logger = LoggerFactory.getLogger(WrapperGetAllVehiclesFromRest.class);
        DtoWrapper<List<Vehicle>> responseWrapper = new DtoWrapper<>();

        List<VehicleDTO> vehicleResponseDto = null;
        String errorMessage = "";
        try {
            logger.info("WRAPPER: fetching REST data");
            vehicleResponseDto = Lists.newArrayList(service.getAll());
            logger.info("WRAPPER: success");
        } catch (RestCallException ex) {
            logger.error(ex.getMessage());
            errorMessage = ex.getErrorCode() + ":" + ex.getMessage() + ":" + ex.getResourceId();
        }

        responseWrapper.setData(vehicleResponseDto.stream().map(Mapper::from).toList());
        responseWrapper.setErrorMessage(errorMessage);

        return responseWrapper;
    }

}
