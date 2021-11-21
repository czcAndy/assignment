package org.vgcs.assignment.persistance;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.vgcs.assignment.persistance.model.Vehicle;

import java.util.List;

@DataMongoTest
@EnableMongoRepositories
@AutoConfigureDataMongo
@ContextConfiguration(classes = VehicleRepo.class)
class VehicleRepoTest implements PersistanceTestSpecifications{

    @Autowired
    private VehicleRepo vehicleRepo;

    @AfterEach
    void afterEach() {
        vehicleRepo.deleteAll();
    }

    @Override
    @Test
    public void findById_OK_test() {
        Vehicle v = new Vehicle();
        v.setId("1");
        v.setName("Truck");

        vehicleRepo.save(v);

        var response = vehicleRepo.findById("1");
        assert (response.isPresent());
        assert (response.get().equals(v));
    }

    @Override
    @Test
    public void findById_not_OK_test() {
        Vehicle v = new Vehicle();
        v.setId("1");
        v.setName("Truck");

        vehicleRepo.save(v);

        var response = vehicleRepo.findById("2");
        assert (response.isEmpty());
    }

    @Test
    public void findAllByNameContains_whenAtLeastOneExists_test() {
        Vehicle v1 = new Vehicle();
        v1.setId("1");
        v1.setName("Truck 1");

        Vehicle v2 = new Vehicle();
        v2.setId("2");
        v2.setName("Truck 2");

        Vehicle v3 = new Vehicle();
        v3.setId("3");
        v3.setName("Vehicle 3");

        vehicleRepo.saveAll(List.of(v1, v2, v3));

        var response = vehicleRepo.findAllByNameContains("ruc");
        assert (response.size() == 2);
        assert (response.get(0).equals(v1));
        assert (response.get(1).equals(v2));
    }

    @Test
    public void findAllByNameContains_whenNoneExists_test() {
        Vehicle v1 = new Vehicle();
        v1.setId("1");
        v1.setName("Truck 1");

        Vehicle v2 = new Vehicle();
        v2.setId("2");
        v2.setName("Truck 2");

        Vehicle v3 = new Vehicle();
        v3.setId("3");
        v3.setName("Truck 3");

        vehicleRepo.saveAll(List.of(v1, v2, v3));

        var response = vehicleRepo.findAllByNameContains("Vehicle");
        assert (response.isEmpty());
    }

    @Test
    public void findAllByNameContains_Empty_test() {
        Vehicle v1 = new Vehicle();
        v1.setId("1");
        v1.setName("Truck 1");

        Vehicle v2 = new Vehicle();
        v2.setId("2");
        v2.setName("Truck 2");

        Vehicle v3 = new Vehicle();
        v3.setId("3");
        v3.setName("Truck 3");

        vehicleRepo.saveAll(List.of(v1, v2, v3));

        var response = vehicleRepo.findAllByNameContains("");
        assert (response.size() == 3);
        assert (response.get(0).equals(v1));
        assert (response.get(1).equals(v2));
        assert (response.get(2).equals(v3));
    }
}
