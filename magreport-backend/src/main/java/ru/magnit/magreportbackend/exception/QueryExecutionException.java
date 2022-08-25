package ru.magnit.magreportbackend.exception;

public class QueryExecutionException extends RuntimeException {

    public QueryExecutionException(String message) {
        super(message);
    }

    public QueryExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
