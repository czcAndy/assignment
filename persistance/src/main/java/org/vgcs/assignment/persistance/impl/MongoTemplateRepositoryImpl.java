package org.vgcs.assignment.persistance.impl;


import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.vgcs.assignment.persistance.MongoTemplateRepository;
import org.vgcs.assignment.persistance.model.VehicleServices;

import java.util.List;

@Repository
public class MongoTemplateRepositoryImpl implements MongoTemplateRepository {

    private final MongoTemplate mongoTemplate;

    public MongoTemplateRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<VehicleServices> findServicesByNameAndStatus(String serviceName, String serviceStatus) {
        Query query = new Query();
        query.addCriteria(
                Criteria.where("services")
                .elemMatch(
                        Criteria.where("serviceName").is(serviceName).and("status").is(serviceStatus)));

        return mongoTemplate.find(query, VehicleServices.class);
    }
}
