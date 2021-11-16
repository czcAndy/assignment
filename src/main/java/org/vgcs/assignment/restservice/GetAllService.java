package org.vgcs.assignment.restservice;

import java.util.List;

public interface GetAllService<T> {
    List<T> getAll();
}
