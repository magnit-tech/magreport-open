package ru.magnit.magreportbackend.service.domain;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobData;
import ru.magnit.magreportbackend.exception.FileSystemException;
import ru.magnit.magreportbackend.exception.ReportExportException;
import ru.magnit.magreportbackend.service.domain.converter.ReaderFactory;
import ru.magnit.magreportbackend.service.domain.converter.WriterFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static ru.magnit.magreportbackend.util.FileUtils.replaceHomeShortcut;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelReportDomainService {

    private static final String ERROR_TRYING_TO_COPY_FILE_TO_RMS_FOLDER = "Error trying to copy file to rms folder";
    private final ReaderFactory readerFactory;
    private final WriterFactory writerFactory;

    @Value("${magreport.reports.folder}")
    private String reportFolder;

    @Value("${magreport.reports.rms-in-folder}")
    private String rmsInFolder;

    @Value("${magreport.reports.rms-out-folder}")
    private String rmsOutFolder;

    @Value("${magreport.excel-file-buffer}")
    private int bufferStream;

    @Value("${magreport.jobengine.wait-rms}")
    private long waitTimeRms;

    @Value("${magreport.excel-file.max-rows}")
    private long maxRows;

    public StreamingResponseBody getExcelReport(ReportJobData jobData, String templatePath, Long templateId) {
        var reportPath = getReportPath(reportFolder, jobData.id(), templateId);
        var rmsInPath = getReportPath(rmsInFolder, jobData.id(), templateId);
        var rmsOutPath = getReportPath(rmsOutFolder, jobData.id(), templateId);

        if (!Files.isReadable(reportPath) && !Files.isReadable(rmsOutPath) && !Files.isReadable(rmsInPath)) {
            final var startMillis = System.currentTimeMillis();
            saveReportToExcel(jobData, templatePath, templateId);
            final var exportMillis = System.currentTimeMillis() - startMillis;
            log.debug("\nExport excel report(" + jobData.id() + ") to file: " + exportMillis * .001);
            copyReportToRms(reportPath, rmsInPath);
            final var copyMillis = System.currentTimeMillis() - startMillis - exportMillis;
            log.debug("\nCopy excel report(" + jobData.id() + ") to encode folder: " + copyMillis * .001);
        }
        if (Files.isReadable(rmsOutPath)) return getStreamExcelFile(rmsOutPath);

        final var startMillis = System.currentTimeMillis();
        waitForRmsEncode(rmsOutPath);
        final var encodeMillis = System.currentTimeMillis() - startMillis;
        log.debug("\nEncode excel report(" + jobData.id() + "): " + encodeMillis * .001);

        return getStreamExcelFile(rmsOutPath);
    }

    public void createExcelReport(ReportJobData jobData, String templatePath, Long templateId) {
        var reportPath = getReportPath(reportFolder, jobData.id(), templateId);
        var rmsInPath = getReportPath(rmsInFolder, jobData.id(), templateId);
        var rmsOutPath = getReportPath(rmsOutFolder, jobData.id(), templateId);

        if (!Files.isReadable(reportPath) && !Files.isReadable(rmsOutPath) && !Files.isReadable(rmsInPath)) {
            final var startMillis = System.currentTimeMillis();
            saveReportToExcel(jobData, templatePath, templateId);
            final var exportMillis = System.currentTimeMillis() - startMillis;
            log.debug("\nExport excel report(" + jobData.id() + ") to file: " + exportMillis * .001);
            copyReportToRms(reportPath, rmsInPath);
            final var copyMillis = System.currentTimeMillis() - startMillis - exportMillis;
            log.debug("\nCopy excel report(" + jobData.id() + ") to encode folder: " + copyMillis * .001);
        }
        if (Files.isReadable(rmsOutPath)) return;

        final var startMillis = System.currentTimeMillis();
        waitForRmsEncode(rmsOutPath);
        final var encodeMillis = System.currentTimeMillis() - startMillis;
        log.debug("\nEncode excel report(" + jobData.id() + "): " + encodeMillis * .001);
    }

    @SuppressWarnings("ALL")
    private void waitForRmsEncode(Path rmsOutPath) {
        final var stopTime = System.currentTimeMillis() + waitTimeRms;
        while (!Files.isReadable(rmsOutPath)) {
            try {
                Thread.sleep(500L);
            } catch (InterruptedException ex) {
                log.info("RMS wait interrupted");
                Thread.currentThread().interrupt();
            }
            if (stopTime < System.currentTimeMillis()){
                throw new FileSystemException("RMS not available");
            }
        }
    }

    private void copyReportToRms(Path reportPath, Path rmsInPath) {
        try {
            Files.copy(reportPath, rmsInPath);
            Files.delete(reportPath);
        } catch (IOException ex) {
            log.error(ERROR_TRYING_TO_COPY_FILE_TO_RMS_FOLDER, ex);
            throw new ReportExportException(ERROR_TRYING_TO_COPY_FILE_TO_RMS_FOLDER, ex);
        }
    }

    public void moveReportToRms(Long jobId, Long templateId) {
        try {
            var reportPath = getReportPath(reportFolder, jobId, templateId);
            var rmsInPath = getReportPath(rmsInFolder, jobId, templateId);

            Files.copy(reportPath, rmsInPath);
            Files.delete(reportPath);
        } catch (IOException ex) {
            log.error(ERROR_TRYING_TO_COPY_FILE_TO_RMS_FOLDER, ex);
            throw new ReportExportException(ERROR_TRYING_TO_COPY_FILE_TO_RMS_FOLDER, ex);
        }
    }

    public void saveReportToExcel(ReportJobData jobData, String templatePath, Long templateId) {

        if(jobData.rowCount() > maxRows) {
            throw new ReportExportException(String.format("Export to file is not available. The report contains more %s rows",maxRows));
        }

            try (var reader = readerFactory.createReader(jobData, getPath(jobData, "avro"))) {
                var writer = writerFactory.createWriter(reader, jobData.reportData(), getPath(jobData, templateId, "xlsm"));
                writer.convert(replaceHomeShortcut(templatePath));
            } catch (Exception ex) {
                try {
                    Files.deleteIfExists(getPath(jobData, templateId,"xlsm"));
                } catch (IOException e) {
                    throw new ReportExportException("Error deleting corrupted excel file", e);
                }
                throw new ReportExportException("Error closing AVRO reader.", ex);
            }
    }

    private Path getPath(ReportJobData jobData, String extension) {
        return Paths.get(replaceHomeShortcut(reportFolder) + "/" + jobData.id() + "." + extension);
    }

    private Path getPath(ReportJobData jobData, Long templateId, String extension) {
        return Paths.get(replaceHomeShortcut(reportFolder) + "/" + jobData.id() + "-" + templateId + "." + extension);
    }

    @SneakyThrows
    private StreamingResponseBody getStreamExcelFile(Path reportPath) {

        var numTries = 5;
        var result = false;

        while (numTries-- > 0) {
            try (var ignored = Files.newInputStream(reportPath)) {
                result = true;
            } catch (IOException ex) {
                result = false;
            }
            if (result) break;
            Thread.sleep(500L);
        }

        return (outputStream -> outputStream(Files.newInputStream(reportPath), outputStream));
    }

    private void outputStream(final InputStream is, OutputStream os)
            throws IOException {
        byte[] data = new byte[bufferStream];
        int read;
        while ((read = is.read(data)) >= 0) {
            os.write(data, 0, read);
        }
        os.flush();
    }

    private Path getReportPath(String folderPath, long jobId, long templateId) {

        var fileName = jobId + "-" + templateId + ".xlsm";
        return Paths.get(replaceHomeShortcut(folderPath), fileName);
    }

    @SneakyThrows
    public long getReportSize(long jobId, long templateId) {
        var rmsOutPath = getReportPath(rmsOutFolder, jobId, templateId);
        return Files.size(rmsOutPath);
    }

    public Path getExcelReportPath(long jobId, long templateId){
        return getReportPath(rmsOutFolder,jobId,templateId);
    }
}
