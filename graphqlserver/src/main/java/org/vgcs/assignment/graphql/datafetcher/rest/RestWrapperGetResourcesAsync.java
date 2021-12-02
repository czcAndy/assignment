package org.vgcs.assignment.graphql.datafetcher.rest;

import org.springframework.stereotype.Component;
import org.vgcs.assignment.graphql.datafetcher.DtoWrapper;
import org.vgcs.assignment.graphql.datafetcher.Mapper;
import org.vgcs.assignment.graphql.datafetcher.WrapperGetResourcesAsync;
import org.vgcs.assignment.restservice.GetServicesAsync;
import org.vgcs.assignment.restservice.exception.RestCallException;

import java.util.List;

@Component
public class RestWrapperGetResourcesAsync<T, S extends GetServicesAsync> implements WrapperGetResourcesAsync<T, S, String> {
    @Override
    public DtoWrapper<List<T>> getAsync(S service, List<String> ids) {
        DtoWrapper<List<T>> responseWrapper = new DtoWrapper<>();

        List<T> response = null;
        String errorMessage = "";
        try {
            response = service.getAsync(ids);
        } catch (RestCallException ex) {
            errorMessage = ex.getErrorCode() + ":" + ex.getMessage() + ":" + ex.getResourceId();
        }

        if (response == null)
            responseWrapper.setData(null);
        else if (response.isEmpty())
            responseWrapper.setData(null);
        else
            responseWrapper.setData((List<T>)response.stream().map(Mapper::fromGeneric).toList());

        responseWrapper.setErrorMessage(errorMessage);

        return responseWrapper;
    }
}
