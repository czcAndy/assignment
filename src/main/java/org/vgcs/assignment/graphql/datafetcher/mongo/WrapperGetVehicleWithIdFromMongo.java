package org.vgcs.assignment.graphql.datafetcher.mongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.vgcs.assignment.graphql.datafetcher.DtoWrapper;
import org.vgcs.assignment.graphql.datafetcher.WrapperGetResourceById;
import org.vgcs.assignment.graphql.model.Vehicle;
import org.vgcs.assignment.persistance.repository.VehicleRepo;

@Component
@Qualifier("mongoWrapper_getVehicleById")
public class WrapperGetVehicleWithIdFromMongo implements WrapperGetResourceById<VehicleRepo, Vehicle, String> {

    @Override
    public DtoWrapper<Vehicle> get(VehicleRepo service, String id) {
        Logger logger = LoggerFactory.getLogger(WrapperGetVehicleWithIdFromMongo.class);
        DtoWrapper<Vehicle> responseWrapper = new DtoWrapper<>();

        Vehicle vehicleResponse = null;
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
