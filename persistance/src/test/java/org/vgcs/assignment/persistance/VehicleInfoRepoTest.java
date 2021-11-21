package org.vgcs.assignment.persistance;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.vgcs.assignment.persistance.model.VehicleInfo;

@DataMongoTest
@EnableMongoRepositories
@AutoConfigureDataMongo
@ContextConfiguration(classes = VehicleInfoRepo.class)
class VehicleInfoRepoTest implements PersistanceTestSpecifications{
    @Autowired
    private VehicleInfoRepo vehicleInfoRepo;

    @AfterEach
    void afterEach() {
        vehicleInfoRepo.deleteAll();
    }
    @Override
    public void findById_OK_test() {
        VehicleInfo vehicleInfo = new VehicleInfo();
        vehicleInfo.setId("1");
        vehicleInfo.setMsidn("+4678625847");
        vehicleInfo.setEngineStatus("OK");
        vehicleInfo.setFleet("Thor's fleet");
        vehicleInfo.setBrand("Volvo Construction Equipment");
        vehicleInfo.setCountryOfOperation("Japan");
        vehicleInfo.setChassisNumber("000543");
        vehicleInfo.setChassisSeries("VCE");

        vehicleInfoRepo.save(vehicleInfo);

        var result = vehicleInfoRepo.findById("1");

        assert (result.isPresent());
        assert (result.get().equals(vehicleInfo));
    }

    @Override
    public void findById_not_OK_test() {
        VehicleInfo vehicleInfo = new VehicleInfo();
        vehicleInfo.setId("1");
        vehicleInfo.setMsidn("+4678625847");
        vehicleInfo.setEngineStatus("OK");
        vehicleInfo.setFleet("Thor's fleet");
        vehicleInfo.setBrand("Volvo Construction Equipment");
        vehicleInfo.setCountryOfOperation("Japan");
        vehicleInfo.setChassisNumber("000543");
        vehicleInfo.setChassisSeries("VCE");

        vehicleInfoRepo.save(vehicleInfo);

        var result = vehicleInfoRepo.findById("2");

        assert (result.isEmpty());
    }
}
