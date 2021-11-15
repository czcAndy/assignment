package org.vgcs.assignment.graphql.datafetcher.mongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.vgcs.assignment.graphql.datafetcher.WrapperGetResourceById;
import org.vgcs.assignment.graphql.datafetcher.DtoWrapper;
import org.vgcs.assignment.graphql.model.VehicleServices;
import org.vgcs.assignment.persistance.repository.VehicleServicesRepo;

@Component
@Qualifier("mongoWrapper_getVehicleServices")
public class WrapperGetVehicleServicesMongo implements WrapperGetResourceById<VehicleServicesRepo, VehicleServices, String> {
    Logger logger = LoggerFactory.getLogger(WrapperGetVehicleServicesMongo.class);
    @Override
    public DtoWrapper<VehicleServices> get(VehicleServicesRepo service, String id) {
        DtoWrapper<VehicleServices> responseWrapper = new DtoWrapper<>();
        VehicleServices vehicleResponse = null;
        String errorMessage = "";
        try {
            logger.info("WRAPPER: fetching mongo data");
            vehicleResponse = service.findById(id).get();
            logger.info("WRAPPER: success");
        } catch (RuntimeException ex) {
            logger.error(ex.getMessage());
            errorMessage = ex.getMessage();
        }

        responseWrapper.setData(vehicleResponse);
        responseWrapper.setErrorMessage(errorMessage);

        return responseWrapper;
    }
}