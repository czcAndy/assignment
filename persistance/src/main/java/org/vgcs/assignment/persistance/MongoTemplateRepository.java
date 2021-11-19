package org.vgcs.assignment.persistance;

import org.vgcs.assignment.persistance.model.VehicleServices;

import java.util.List;

public interface MongoTemplateRepository {
    List<VehicleServices> findServicesByNameAndStatus(String serviceName, String serviceStatus);
}
