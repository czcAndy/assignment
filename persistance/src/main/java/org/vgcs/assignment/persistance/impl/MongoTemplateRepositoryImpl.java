package org.vgcs.assignment.persistance.impl;


import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.vgcs.assignment.persistance.MongoTemplateRepository;
import org.vgcs.assignment.persistance.model.VehicleServices;

import java.util.List;

public class MongoTemplateRepositoryImpl implements MongoTemplateRepository {

    private final MongoTemplate mongoTemplate;

    public MongoTemplateRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public List<VehicleServices> findServicesByNameAndStatus(String serviceName, String serviceStatus) {
        Query query = new Query();
        query.addCriteria(Criteria.where("services.serviceName").is(serviceName).and("services.status").is(serviceStatus));
        query.fields().include("_id");
        return mongoTemplate.find(query, VehicleServices.class);
    }
}
