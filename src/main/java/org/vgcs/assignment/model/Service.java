package org.vgcs.assignment.model;

import java.util.Date;

public record Service(String serviceName, String status, Date lastUpdated) {
}
