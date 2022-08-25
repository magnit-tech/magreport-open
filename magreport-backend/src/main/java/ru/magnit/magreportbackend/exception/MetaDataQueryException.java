package ru.magnit.magreportbackend.exception;

public class MetaDataQueryException extends RuntimeException {

    public MetaDataQueryException(String message) {
        super(message);
    }

    public MetaDataQueryException(String message, Throwable cause) {
        super(message, cause);
    }
}
