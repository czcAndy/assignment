package org.vgcs.assignment.persistance.repository;

import org.vgcs.assignment.graphql.model.VehicleServices;

import java.util.List;

public interface MongoTemplateRepository {
    List<VehicleServices> findServicesByNameAndStatus(String serviceName, String serviceStatus);
}
