package org.vgcs.assignment.graphql.datafetcher;

import java.util.List;

public interface WrapperGetResource<T, S, ID > {
   DtoWrapper<T> get(S service, ID id);
   DtoWrapper<List<T>> getAsync(S service, List<ID> ids);
}