package org.vgcs.assignment.graphql.datafetcher;

import java.util.List;

public interface WrapperGetResourcesByReflection<T, R> {
    DtoWrapper<List<T>> getAll(R repo, String method, Object... args);
    DtoWrapper<T> get(R repo, String method, Object... args);
}
