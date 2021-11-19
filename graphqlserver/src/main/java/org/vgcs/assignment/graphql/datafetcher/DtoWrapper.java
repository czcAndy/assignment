package org.vgcs.assignment.graphql.datafetcher;

public class DtoWrapper<T> {
    private String errorMessage;
    private T data;

    public boolean hasError() {
        return !errorMessage.isEmpty();
    }

    public boolean hasData() {
        return data != null;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
