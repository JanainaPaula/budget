package br.com.janadev.budget.primary.handler;

public class ErrorResponse{
    private final String status;
    private final String exception;
    private final String message;
    private final String cause;
    private final String path;

    public static ErrorResponse of(String status, Exception exception, String path){
        Throwable cause = exception.getCause();
        return new ErrorResponse(
                status,
                exception.getClass().getName(),
                exception.getMessage(),
                cause != null ? cause.getMessage() : null,
                path.replace("uri=", ""));
    }

    private ErrorResponse(String status, String exception, String message, String cause, String path) {
        this.status = status;
        this.exception = exception;
        this.message = message;
        this.cause = cause;
        this.path = path;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getException() {
        return exception;
    }

    public String getPath() {
        return path;
    }

    public String getCause() {
        return cause;
    }
}
