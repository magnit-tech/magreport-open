package ru.magnit.magreportbackend.service.jobengine;

public interface ReportReader extends Runnable{

    void run();
    ReaderStatus getReaderStatus();
    boolean isFinished();
    boolean isFailed();
    boolean isCanceled();
    long getRowCount();
    void cancel();
    String getErrorDescription();
}
