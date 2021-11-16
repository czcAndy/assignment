package org.vgcs.assignment.graphql.datafetcher;

public interface WrapperGetResource<T, S, ID > {
   DtoWrapper<T> get(S service, ID id);
}