package org.vgcs.assignment.graphql.datafetcher;

import java.util.List;

public interface WrapperGetResource<T, S, ID > {
   DtoWrapper<T> get(S service, ID id);
}