package org.vgcs.assignment.graphql.helper;

import org.vgcs.assignment.graphql.model.Service;
import org.vgcs.assignment.graphql.model.Vehicle;
import org.vgcs.assignment.graphql.model.VehicleInfo;
import org.vgcs.assignment.graphql.model.VehicleServices;
import org.vgcs.assignment.restservice.dto.VehicleDTO;
import org.vgcs.assignment.restservice.dto.VehicleInfoResponseDTO;
import org.vgcs.assignment.restservice.dto.VehicleServicesResponseDTO;

import java.util.List;

public class Mapper {

    public static Vehicle from(VehicleDTO obj) {
        Vehicle v = new Vehicle();
        v.setId(obj.id());
        v.setName(obj.name());

        return v;
    }
    public static VehicleInfo from(VehicleInfoResponseDTO obj) {
        VehicleInfo vi = new VehicleInfo();
        vi.setMsidn(obj.msidn());
        vi.setEngineStatus(obj.engineStatus());
        vi.setFleet(obj.fleet());
        vi.setBrand(obj.brand());
        vi.setCountryOfOperation(obj.countryOfOperation());
        vi.setChassisNumber(obj.chassisNumber());
        vi.setChassisSeries(obj.chassisSeries());

        return vi;
    }

    public static VehicleServices from(VehicleServicesResponseDTO obj) {
        VehicleServices vs = new VehicleServices();
        vs.setCommunicationStatus(obj.communicationStatus());

        List<Service> services = List.of();

        if( vs.getServices() != null)
            services =  vs.getServices().stream()
                .map(sDto -> new Service(sDto.getServiceName(), sDto.getStatus(), sDto.getLastUpdated()))
                .toList();

        vs.setServices(services);
        return vs;
    }


}
