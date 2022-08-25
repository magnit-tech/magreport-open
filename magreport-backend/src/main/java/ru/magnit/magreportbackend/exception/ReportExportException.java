package ru.magnit.magreportbackend.exception;

public class ReportExportException extends RuntimeException {

    public ReportExportException(String message) {
        super(message);
    }

    public ReportExportException(String message, Throwable cause) {
        super(message, cause);
    }
}
