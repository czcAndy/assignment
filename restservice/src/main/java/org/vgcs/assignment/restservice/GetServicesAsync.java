package org.vgcs.assignment.restservice;

import java.util.List;

public interface GetServicesAsync<T, ID> {
    List<T> getAsync(List<ID> ids);
}
