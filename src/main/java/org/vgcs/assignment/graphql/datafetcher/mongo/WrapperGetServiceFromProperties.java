package org.vgcs.assignment.graphql.datafetcher.mongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.vgcs.assignment.graphql.datafetcher.DtoWrapper;
import org.vgcs.assignment.graphql.datafetcher.WrapperGetResourcesByProperties;
import org.vgcs.assignment.graphql.model.VehicleServices;
import org.vgcs.assignment.persistance.repository.VehicleServicesRepo;

import java.util.List;

@Component
@Qualifier("mongoWrapper_serviceFromProperties")
public class WrapperGetServiceFromProperties implements WrapperGetResourcesByProperties<VehicleServicesRepo, List<VehicleServices>> {
    Logger logger = LoggerFactory.getLogger(WrapperGetServiceFromProperties.class);

    @Override
    public DtoWrapper<List<VehicleServices>> get(VehicleServicesRepo service, Object... args) {
        DtoWrapper<List<VehicleServices>> responseWrapper = new DtoWrapper<>();
        List<VehicleServices> vehicleResponse = null;
        String errorMessage = "";
        try {
            logger.info("WRAPPER: fetching mongo data");
            vehicleResponse = service.findServicesByNameAndStatus((String)args[0], (String)args[1]);
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
