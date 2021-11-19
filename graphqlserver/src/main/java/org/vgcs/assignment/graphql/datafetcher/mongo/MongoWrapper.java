package org.vgcs.assignment.graphql.datafetcher.mongo;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.vgcs.assignment.graphql.datafetcher.DtoWrapper;
import org.vgcs.assignment.persistance.generic.Invoker;
import org.vgcs.assignment.graphql.datafetcher.WrapperGetResourcesByReflection;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class MongoWrapper<T, R> implements WrapperGetResourcesByReflection<T, R> {

    private final Invoker<List<T>> listInvoker;
    private final Invoker<Optional<T>> simpleInvoker;

    @Override
    public DtoWrapper<List<T>> getAll(R repo, String method, Object... args) {
        DtoWrapper<List<T>> responseWrapper = new DtoWrapper<>();
        List<T> response = null;
        String errorMessage = "";
        try {
            response = listInvoker.call(method, repo, args);
        } catch (RuntimeException ex) {
            errorMessage = ex.getMessage();
        }

        if (response == null)
            responseWrapper.setData(null);
        else if (response.isEmpty())
            responseWrapper.setData(null);
        else
            responseWrapper.setData(response);

        responseWrapper.setErrorMessage(errorMessage);

        return responseWrapper;
    }

    @Override
    public DtoWrapper<T> get(R repo, String method, Object... args) {
        DtoWrapper<T> responseWrapper = new DtoWrapper<>();
        Optional<T> vehicleResponse = null;
        String errorMessage = "";
        try {
            vehicleResponse = simpleInvoker.call(method, repo, args);
        } catch (RuntimeException ex) {
            errorMessage = ex.getMessage();
        }

        if(vehicleResponse == null)
            responseWrapper.setData(null);
        else
            responseWrapper.setData(vehicleResponse.isPresent() ? vehicleResponse.get() : null);

        responseWrapper.setErrorMessage(errorMessage);

        return responseWrapper;
    }
}
