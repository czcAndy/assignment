package org.vgcs.assignment.persistance.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.vgcs.assignment.graphql.model.VehicleComplete;

import java.util.UUID;

@Service
public interface VehicleRepository extends MongoRepository<VehicleComplete, UUID> {
}
