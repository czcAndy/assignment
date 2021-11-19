package org.vgcs.assignment.restservice;

import java.util.List;

public interface GetService<T, ID> {
    T get(ID id);
    List<T> getAsync(List<ID> ids);

}
