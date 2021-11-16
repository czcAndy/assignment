package org.vgcs.assignment.graphql.datafetcher;

import java.util.List;

public interface WrapperGetAllResources<T, S>  {
    DtoWrapper<List<T>> getAll(S service);
}
