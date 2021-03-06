package org.vgcs.assignment.persistance;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.vgcs.assignment.persistance.model.Vehicle;

import java.util.List;
import java.util.Optional;

public interface VehicleRepo extends MongoRepository<Vehicle, String> {
    Optional<Vehicle> findById(String id);
    List<Vehicle> findAllByNameContains(String value);
}
