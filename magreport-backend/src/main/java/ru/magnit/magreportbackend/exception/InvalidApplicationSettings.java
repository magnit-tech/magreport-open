package ru.magnit.magreportbackend.exception;

public class InvalidApplicationSettings extends RuntimeException {

    public InvalidApplicationSettings(String message) {
        super(message);
    }

    public InvalidApplicationSettings(String message, Throwable cause) {
        super(message, cause);
    }
}
