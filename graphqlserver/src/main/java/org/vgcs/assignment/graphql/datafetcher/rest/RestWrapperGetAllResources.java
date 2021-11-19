package org.vgcs.assignment.graphql.datafetcher.rest;

import org.springframework.stereotype.Component;
import org.vgcs.assignment.graphql.datafetcher.DtoWrapper;
import org.vgcs.assignment.graphql.datafetcher.Mapper;
import org.vgcs.assignment.graphql.datafetcher.WrapperGetAllResources;
import org.vgcs.assignment.restservice.GetAllService;
import org.vgcs.assignment.restservice.exception.RestCallException;

import java.util.List;

@Component
public class RestWrapperGetAllResources <T, S extends GetAllService> implements WrapperGetAllResources<T, S> {

    @Override
    public DtoWrapper<List<T>> getAll(S service) {
        DtoWrapper<List<T>> responseWrapper = new DtoWrapper<>();

        List<T> response = null;
        String errorMessage = "";
        try {
            response = (List<T>) service.getAll();
        } catch (RestCallException ex) {
            errorMessage = ex.getErrorCode() + ":" + ex.getMessage() + ":" + ex.getResourceId();
        }

        if (response == null)
            responseWrapper.setData(null);
        else if (response.isEmpty())
            responseWrapper.setData(null);
        else
            responseWrapper.setData(response);

        responseWrapper.setData((List<T>)response.stream().map(Mapper::fromGeneric).toList());
        responseWrapper.setErrorMessage(errorMessage);

        return responseWrapper;
    }
}