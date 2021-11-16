package org.vgcs.assignment.restservice;

public interface GetService<T, ID> {
    T get(ID id);
}
