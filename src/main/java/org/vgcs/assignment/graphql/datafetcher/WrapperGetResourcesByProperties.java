package org.vgcs.assignment.graphql.datafetcher;

public interface WrapperGetResourcesByProperties<T, R> {
    DtoWrapper<R> get(T service, Object... args);
}
