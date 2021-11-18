package org.vgcs.assignment.graphql.datafetcher;

import org.vgcs.assignment.graphql.model.Service;
import org.vgcs.assignment.graphql.model.Vehicle;
import org.vgcs.assignment.graphql.model.VehicleInfo;
import org.vgcs.assignment.graphql.model.VehicleServices;
import org.vgcs.assignment.restservice.dto.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Mapper {

    public static Vehicle fromVehicleDto(VehicleDTO obj) {
        if (obj == null)
            return null;

        Vehicle v = new Vehicle();
        v.setId(obj.id());
        v.setName(obj.name());

        return v;
    }

    public static VehicleInfo fromVehicleInfoResponseWithIdDto(VehicleInfoResponseWithIdDTO obj) {
        if (obj == null)
            return null;

        VehicleInfo vi = new VehicleInfo();
        vi.setId(obj.id());
        vi.setMsidn(obj.vehicleInfoResponseDTO().msidn());
        vi.setEngineStatus(obj.vehicleInfoResponseDTO().engineStatus());
        vi.setFleet(obj.vehicleInfoResponseDTO().fleet());
        vi.setBrand(obj.vehicleInfoResponseDTO().brand());
        vi.setCountryOfOperation(obj.vehicleInfoResponseDTO().countryOfOperation());
        vi.setChassisNumber(obj.vehicleInfoResponseDTO().chassisNumber());
        vi.setChassisSeries(obj.vehicleInfoResponseDTO().chassisSeries());

        return vi;
    }

    public static VehicleServices fromVehiclesServicesResponseWithIdDTO(VehicleServicesResponseWithIdDTO obj) {
        if (obj == null)
            return null;

        VehicleServices vs = new VehicleServices();
        vs.setId(obj.id());
        vs.setCommunicationStatus(obj.vehicleServicesResponseDTO().communicationStatus());

        List<Service> services = List.of();

        if (obj.vehicleServicesResponseDTO().services() != null)
            services = obj.vehicleServicesResponseDTO().services().stream()
                    .map(sDto -> new Service(sDto.serviceName(), sDto.status(),
                            LocalDateTime.parse(sDto.lastUpdate(),
                                    DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.of("Europe/Stockholm")))))
                    .toList();

        vs.setServices(services);
        return vs;
    }


    public static <T, D> T fromGeneric(D obj) {
        if (obj instanceof VehicleDTO)
            return (T) fromVehicleDto((VehicleDTO) obj);
        if (obj instanceof VehicleInfoResponseWithIdDTO)
            return (T) fromVehicleInfoResponseWithIdDto((VehicleInfoResponseWithIdDTO) obj);
        if (obj instanceof VehicleServicesResponseWithIdDTO)
            return (T) fromVehiclesServicesResponseWithIdDTO((VehicleServicesResponseWithIdDTO) obj);
        return null;
    }
}
