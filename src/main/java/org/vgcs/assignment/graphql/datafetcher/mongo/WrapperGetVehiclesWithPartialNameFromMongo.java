package org.vgcs.assignment.graphql.datafetcher.mongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.vgcs.assignment.graphql.datafetcher.DtoWrapper;
import org.vgcs.assignment.graphql.datafetcher.WrapperGetResourceById;
import org.vgcs.assignment.graphql.model.Vehicle;
import org.vgcs.assignment.persistance.repository.VehicleRepo;

import java.util.List;

@Component
@Qualifier("mongoWrapper_getVehiclePartialName")
public class WrapperGetVehiclesWithPartialNameFromMongo implements WrapperGetResourceById<VehicleRepo, List<Vehicle>, String> {

    @Override
    public DtoWrapper<List<Vehicle>> get(VehicleRepo service, String name) {
        Logger logger = LoggerFactory.getLogger(WrapperGetVehiclesWithPartialNameFromMongo.class);
        DtoWrapper<List<Vehicle>> responseWrapper = new DtoWrapper<>();

        List<Vehicle> vehicleResponse = null;
        String errorMessage = "";
        try {
            logger.info("WRAPPER: fetching mongo data");
            vehicleResponse = service.findAllByNameContains(name);
            logger.info("WRAPPER: success");
        } catch (RuntimeException ex) {
            logger.error(ex.getMessage());
            errorMessage = ex.getMessage();
        }

        responseWrapper.setData(vehicleResponse.isEmpty() ? null : vehicleResponse);
        responseWrapper.setErrorMessage(errorMessage);

        return responseWrapper;
    }
}
