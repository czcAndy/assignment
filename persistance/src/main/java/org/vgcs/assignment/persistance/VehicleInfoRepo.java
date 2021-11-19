package org.vgcs.assignment.persistance;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.vgcs.assignment.persistance.model.VehicleInfo;

import java.util.Optional;


public interface VehicleInfoRepo extends MongoRepository<VehicleInfo, String>{
    Optional<VehicleInfo> findById(String id);
}
