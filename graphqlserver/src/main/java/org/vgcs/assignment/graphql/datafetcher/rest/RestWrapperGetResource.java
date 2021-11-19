package org.vgcs.assignment.graphql.datafetcher.rest;

import org.springframework.stereotype.Component;
import org.vgcs.assignment.graphql.datafetcher.DtoWrapper;
import org.vgcs.assignment.graphql.datafetcher.Mapper;
import org.vgcs.assignment.graphql.datafetcher.WrapperGetResource;
import org.vgcs.assignment.restservice.GetService;
import org.vgcs.assignment.restservice.exception.RestCallException;

import java.util.List;

@Component
public class RestWrapperGetResource<T, S extends GetService> implements WrapperGetResource<T, S, String>{

    @Override
    public DtoWrapper<T> get(S service, String id) {
        DtoWrapper<T> responseWrapper = new DtoWrapper<>();

        T response = null;
        String errorMessage = "";
        try {
            response = (T) service.get(id);
        } catch (RestCallException ex) {
            errorMessage = ex.getErrorCode() + ":" + ex.getMessage() + ":" + ex.getResourceId();
        }

        responseWrapper.setData(Mapper.fromGeneric(response));
        responseWrapper.setErrorMessage(errorMessage);

        return responseWrapper;
    }

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
