package org.vgcs.assignment.graphql.datafetcher;

public interface WrapperGetAllResources<T, R>  {
    DtoWrapper<R> getAll(T service);
}
