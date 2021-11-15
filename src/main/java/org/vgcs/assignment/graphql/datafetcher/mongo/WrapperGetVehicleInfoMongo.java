package org.vgcs.assignment.graphql.datafetcher.mongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.vgcs.assignment.graphql.datafetcher.WrapperGetResourceById;
import org.vgcs.assignment.graphql.datafetcher.DtoWrapper;
import org.vgcs.assignment.graphql.model.VehicleInfo;
import org.vgcs.assignment.persistance.repository.VehicleInfoRepo;

@Component
@Qualifier("mongoWrapper_getVehicleInfo")
public class WrapperGetVehicleInfoMongo implements WrapperGetResourceById<VehicleInfoRepo, VehicleInfo, String> {
    Logger logger = LoggerFactory.getLogger(WrapperGetVehicleInfoMongo.class);
    @Override
    public DtoWrapper<VehicleInfo> get(VehicleInfoRepo service, String id) {
        DtoWrapper<VehicleInfo> responseWrapper = new DtoWrapper<>();

        VehicleInfo vehicleResponse = null;
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
