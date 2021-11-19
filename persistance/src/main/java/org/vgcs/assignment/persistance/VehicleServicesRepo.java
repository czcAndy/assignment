package org.vgcs.assignment.persistance;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.vgcs.assignment.persistance.model.VehicleServices;

import java.util.Optional;

public interface VehicleServicesRepo extends MongoRepository<VehicleServices, String>, MongoTemplateRepository {
    Optional<VehicleServices> findById(String id);
}
