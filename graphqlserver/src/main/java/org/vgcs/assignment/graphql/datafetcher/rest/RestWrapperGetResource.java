package org.vgcs.assignment.graphql.datafetcher.rest;

import org.springframework.stereotype.Component;
import org.vgcs.assignment.graphql.datafetcher.DtoWrapper;
import org.vgcs.assignment.graphql.datafetcher.Mapper;
import org.vgcs.assignment.graphql.datafetcher.WrapperGetResource;
import org.vgcs.assignment.restservice.GetService;
import org.vgcs.assignment.restservice.exception.RestCallException;

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
}
