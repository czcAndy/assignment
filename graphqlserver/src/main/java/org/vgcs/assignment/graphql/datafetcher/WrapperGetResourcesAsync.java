package org.vgcs.assignment.graphql.datafetcher;

import java.util.List;

public interface WrapperGetResourcesAsync<T, S, ID> {
    DtoWrapper<List<T>> getAsync(S service, List<ID> ids);
}
