package org.vgcs.assignment.restservice.exception;

public class RestCallException extends RuntimeException{
    private String message;
    private int errorCode;
    private String resourceId;

    public RestCallException(int errorCode, String resourceId) {
        super();
        this.errorCode = errorCode;
        this.resourceId = resourceId;
    }

    public RestCallException(String message, int errorCode, String resourceId) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
        this.resourceId = resourceId;
    }

    public RestCallException(String message, int errorCode) {
        super(message);
        this.message = message;
        this.errorCode = errorCode;
    }

    public RestCallException(Throwable cause, int errorCode, String resourceId) {
        super(cause);
        this.errorCode = errorCode;
        this.resourceId = resourceId;
    }

    public RestCallException(Throwable cause, String message, int errorCode, String resourceId) {
        super(message, cause);
        this.errorCode = errorCode;
        this.message = message;
        this.resourceId = resourceId;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getMessage() {
        return this.message;
    }

    public String getResourceId() { return this.resourceId; }
}
