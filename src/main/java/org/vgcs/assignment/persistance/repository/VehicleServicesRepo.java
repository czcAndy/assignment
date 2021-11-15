package org.vgcs.assignment.persistance.repository;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.data.mongodb.repository.Query;
import org.vgcs.assignment.graphql.model.VehicleServices;

import java.util.List;

public interface VehicleServicesRepo extends MongoRepository<VehicleServices, String>, MongoTemplateRepository {


}
