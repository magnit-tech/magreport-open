package ru.magnit.magreportbackend.service.jobengine.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.file.CodecFactory;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;
import ru.magnit.magreportbackend.dto.inner.jobengine.ReportWriterData;
import ru.magnit.magreportbackend.exception.ReportExportException;
import ru.magnit.magreportbackend.service.jobengine.ReportWriter;
import ru.magnit.magreportbackend.service.jobengine.WriterStatus;

import java.io.File;

@Slf4j
@RequiredArgsConstructor
public class ReportWriterImpl implements ReportWriter {

    private static final String ERROR_MESSAGE = "Error while trying to write report into AVRO file.";
    private final AvroSchemaBuilder schemaBuilder;
    private final ReportWriterData writerData;
    private final String reportRootDir;
    private final Long maxRows;
    private WriterStatus status = WriterStatus.IDLE;
    private long rowCount;
    private boolean isCanceled;
    private String errorDescription = null;

    @Override
    public void run() {

        try {
            status = WriterStatus.RUNNING;
            var schema = schemaBuilder.createSchema(writerData.report());
            processData(schema);
        } catch (Exception ex) {
            status = WriterStatus.FAILED;
            errorDescription = ex.getMessage() + "\n" + ex;
            throw new ReportExportException(ERROR_MESSAGE, ex);
        }
    }

    private void processData(Schema schema) {

        final DatumWriter<GenericRecord> recordWriter = new GenericDatumWriter<>(schema);

        try (DataFileWriter<GenericRecord> fileWriter = new DataFileWriter<>(recordWriter)) {
            final var file = new File(reportRootDir + File.separator + writerData.jobId() + ".avro");
            //noinspection All
            file.getParentFile().mkdirs(); // ensure that the target folder exists
            fileWriter.setCodec(CodecFactory.snappyCodec());
            fileWriter.create(schema, file);

            while (true) {
                var cacheRow = writerData.cache().poll();
                if (cacheRow == null) {
                    if (Boolean.TRUE.equals(writerData.isCacheEmpty().get()) || isCanceled) break;
                    //noinspection BusyWait
                    Thread.sleep(10);
                } else {
                    GenericRecord genericRecord = new GenericData.Record(schema);
                    cacheRow.entries().forEach(field ->
                            genericRecord.put(field.fieldData().columnName(), field.value())
                    );
                    fileWriter.append(genericRecord);
                    rowCount++;
                    if (rowCount > maxRows) throw new ReportExportException("Превышено максимально допустимое количество строк отчета:" + maxRows);
                }
            }
            status = isCanceled ? WriterStatus.CANCELED : WriterStatus.FINISHED;
        } catch (Exception ex) {
            status = WriterStatus.FAILED;
            errorDescription = ex.getMessage();
            throw new ReportExportException(ERROR_MESSAGE, ex);
        }
    }

    @Override
    public WriterStatus getWriterStatus() {
        return status;
    }

    @Override
    public boolean isFinished() {
        return status.isFinalStatus();
    }

    @Override
    public boolean isFailed() {
        return status == WriterStatus.FAILED;
    }

    @Override
    public long getRowCount() {
        return rowCount;
    }

    @Override
    public void cancel() {
        isCanceled = true;
    }

    @Override
    public boolean isCanceled() {
        return status == WriterStatus.CANCELED;
    }

    @Override
    public String getErrorDescription() {
        return errorDescription;
    }
}
