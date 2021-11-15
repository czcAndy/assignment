package org.vgcs.assignment.persistance.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.vgcs.assignment.graphql.model.Vehicle;
import java.util.List;

@Service
public interface VehicleRepo extends MongoRepository<Vehicle, String> {
    List<Vehicle> findAllByNameContains(String value);
}
