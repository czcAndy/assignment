package model;

import java.util.Date;

public record Service(String serviceName, String status, Date lastUpdated) {
}
