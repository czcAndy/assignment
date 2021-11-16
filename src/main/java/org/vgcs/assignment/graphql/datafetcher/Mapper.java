package org.vgcs.assignment.graphql.datafetcher;

import org.vgcs.assignment.graphql.model.Service;
import org.vgcs.assignment.graphql.model.Vehicle;
import org.vgcs.assignment.graphql.model.VehicleInfo;
import org.vgcs.assignment.graphql.model.VehicleServices;
import org.vgcs.assignment.restservice.dto.VehicleDTO;
import org.vgcs.assignment.restservice.dto.VehicleInfoResponseDTO;
import org.vgcs.assignment.restservice.dto.VehicleServicesResponseDTO;

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

    public static VehicleInfo fromVehicleInfoResponseDto(VehicleInfoResponseDTO obj) {
        if (obj == null)
            return null;

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

    public static VehicleServices fromVehiclesServicesResponseDTO(VehicleServicesResponseDTO obj) {
        if (obj == null)
            return null;

        VehicleServices vs = new VehicleServices();
        vs.setCommunicationStatus(obj.communicationStatus());

        List<Service> services = List.of();

        if (obj.services() != null)
            services = obj.services().stream()
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
        if (obj instanceof VehicleInfoResponseDTO)
            return (T) fromVehicleInfoResponseDto((VehicleInfoResponseDTO) obj);
        if (obj instanceof VehicleServicesResponseDTO)
            return (T) fromVehiclesServicesResponseDTO((VehicleServicesResponseDTO) obj);
        return null;
    }
}
