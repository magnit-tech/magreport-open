package ru.magnit.magreportbackend.service.jobengine;

public interface ReportWriter extends Runnable{

    void run();
    WriterStatus getWriterStatus();
    boolean isFinished();
    boolean isFailed();
    boolean isCanceled();
    long getRowCount();
    void cancel();
    String getErrorDescription();
}
