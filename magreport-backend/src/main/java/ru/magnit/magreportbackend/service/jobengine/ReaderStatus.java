package ru.magnit.magreportbackend.service.jobengine;

public enum ReaderStatus {
    IDLE(false),
    RUNNING(false),
    CANCELED(true),
    FAILED(true),
    FINISHED(true);

    private final boolean finalStatus;

    ReaderStatus(boolean finalStatus) {
        this.finalStatus = finalStatus;
    }

    public boolean isFinalStatus() {
        return finalStatus;
    }
}
