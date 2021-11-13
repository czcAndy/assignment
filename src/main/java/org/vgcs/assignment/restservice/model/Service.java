package org.vgcs.assignment.restservice.model;

import java.util.Date;

public record Service(String serviceName, String status, Date lastUpdated) {
}
