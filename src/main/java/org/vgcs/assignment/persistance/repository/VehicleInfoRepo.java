package org.vgcs.assignment.persistance.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.vgcs.assignment.graphql.model.Vehicle;
import org.vgcs.assignment.graphql.model.VehicleInfo;

import java.util.List;

public interface VehicleInfoRepo extends MongoRepository<VehicleInfo, String>{

}
