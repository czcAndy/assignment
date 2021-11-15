package org.vgcs.assignment.graphql.datafetcher;

public interface WrapperGetResourceById<T, R, ID > {
   DtoWrapper<R> get(T service, ID id);
}