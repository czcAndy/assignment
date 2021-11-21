package org.vgcs.assignment.persistance;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.vgcs.assignment.persistance.model.Service;
import org.vgcs.assignment.persistance.model.VehicleServices;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DataMongoTest
@EnableMongoRepositories
@AutoConfigureDataMongo
@ContextConfiguration(classes = VehicleServicesRepo.class)
class VehicleServicesRepoTest implements PersistanceTestSpecifications {

    @Autowired
    private VehicleServicesRepo vehicleServicesRepo;
    private static LocalDateTime dateTime;

    @BeforeAll
    static void setup() {
        dateTime = LocalDateTime.now(Clock.tick(Clock.systemDefaultZone(), Duration.ofNanos(1000000)));
    }

    @AfterEach
    void afterEach() {
        vehicleServicesRepo.deleteAll();
    }

    @Override
    @Test
    public void findById_OK_test() {
        VehicleServices vs = new VehicleServices();
        vs.setId("1");
        vs.setCommunicationStatus("ACTIVE");
        vs.setServices(List.of(
                new Service("GPS", "ACTIVE", dateTime),
                new Service("rswdl", "ERROR", dateTime),
                new Service("FuelMeasuremnet", "ACTIVE", dateTime)
        ));

        vehicleServicesRepo.save(vs);

        Optional<VehicleServices> result = vehicleServicesRepo.findById("1");
        assert (result.isPresent());
        assert (result.get().equals(vs));
    }

    @Override
    @Test
    public void findById_not_OK_test() {
        VehicleServices vs = new VehicleServices();
        vs.setId("1");
        vs.setCommunicationStatus("ACTIVE");
        vs.setServices(List.of(
                new Service("GPS", "ACTIVE", dateTime),
                new Service("rswdl", "ERROR", dateTime),
                new Service("FuelMeasuremnet", "ACTIVE", dateTime)
        ));

        vehicleServicesRepo.save(vs);

        Optional<VehicleServices> result = vehicleServicesRepo.findById("2");
        assert (result.isEmpty());
    }

    @Test
    void findServicesByNameAndStatus_whenAtLeastOneExists_test() {
        VehicleServices vs1 = new VehicleServices();
        vs1.setId("1");
        vs1.setCommunicationStatus("ACTIVE");
        vs1.setServices(List.of(
                new Service("GPS", "ACTIVE", dateTime),
                new Service("rswdl", "ERROR", dateTime),
                new Service("FuelMeasuremnet", "ACTIVE", dateTime)
        ));

        VehicleServices vs2 = new VehicleServices();
        vs2.setId("2");
        vs2.setCommunicationStatus("ACTIVE");
        vs2.setServices(List.of(
                new Service("GPS", "ACTIVE", dateTime),
                new Service("rswdl", "ERROR", dateTime),
                new Service("FuelMeasuremnet", "ERROR", dateTime)
        ));

        VehicleServices vs3 = new VehicleServices();
        vs3.setId("3");
        vs3.setCommunicationStatus("ACTIVE");
        vs3.setServices(List.of(
                new Service("GPS", "INACTIVE", dateTime),
                new Service("rswdl", "ERROR", dateTime),
                new Service("FuelMeasuremnet", "ACTIVE", dateTime)
        ));

        VehicleServices vs4 = new VehicleServices();
        vs4.setId("4");
        vs4.setCommunicationStatus("INACTIVE");



        vehicleServicesRepo.saveAll(List.of(vs1, vs2, vs3, vs4));

        List<VehicleServices> result = vehicleServicesRepo.findServicesByNameAndStatus("GPS", "ACTIVE");
        assert (result.size() == 2);
        assert (result.get(0).equals(vs1));
        assert (result.get(1).equals(vs2));
    }

    @Test
    void findServicesByNameAndStatus_whenNoneExists_test() {
        VehicleServices vs = new VehicleServices();
        vs.setId("1");
        vs.setCommunicationStatus("ACTIVE");
        vs.setServices(List.of(
                new Service("GPS", "ACTIVE", dateTime),
                new Service("rswdl", "ERROR", dateTime),
                new Service("FuelMeasuremnet", "ACTIVE", dateTime)
        ));

        vehicleServicesRepo.save(vs);

        List<VehicleServices> result = vehicleServicesRepo.findServicesByNameAndStatus("GPS", "ERROR");
        assert (result.isEmpty());
    }
}
