package ru.magnit.magreportbackend.service.jobengine;

public enum WriterStatus {
    IDLE(false),
    RUNNING(false),
    CANCELED(true),
    FAILED(true),
    FINISHED(true);

    private final boolean finalStatus;

    WriterStatus(boolean finalStatus) {
        this.finalStatus = finalStatus;
    }

    public boolean isFinalStatus() {
        return finalStatus;
    }
}
