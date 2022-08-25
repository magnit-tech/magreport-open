package ru.magnit.magreportbackend.service.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.dto.inner.olap.CubeData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobData;
import ru.magnit.magreportbackend.dto.response.reportjob.ReportPageResponse;
import ru.magnit.magreportbackend.exception.ReportExportException;
import ru.magnit.magreportbackend.service.domain.converter.ReaderFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static ru.magnit.magreportbackend.util.FileUtils.replaceHomeShortcut;

@Slf4j
@Service
@RequiredArgsConstructor
public class AvroReportDomainService {

    private final ReaderFactory readerFactory;

    @Value("${magreport.reports.folder}")
    private String reportFolder;

    public ReportPageResponse getPage(ReportJobData jobData, Long pageNumber, Long rowsPerPage) {
        List<Map<String, String>> resultRows = new ArrayList<>();

        try (var reader = readerFactory.createReader(jobData, getPath(jobData))) {
            long rowsToSkip = (pageNumber - 1) * rowsPerPage;
            long rowsToRead = rowsPerPage;
            while (rowsToSkip-- > 0) reader.getRow();
            while (rowsToRead-- > 0) {
                var cacheRow = reader.getRow();
                if (cacheRow == null) break;
                var row = new LinkedHashMap<String, String>(cacheRow.entries().size());
                cacheRow.entries().forEach(entry -> row.put(entry.fieldData().name(), entry.value()));
                resultRows.add(row);
            }
        } catch (Exception ex) {
            throw new ReportExportException("Error while trying to get report page", ex);
        }

        return new ReportPageResponse(
                jobData.reportId(),
                jobData.id(),
                pageNumber,
                jobData.rowCount(),
                resultRows);
    }

    @SuppressWarnings("java:S125")
    public CubeData getCubeData(ReportJobData jobData){
        try (var reader = readerFactory.createReader(jobData, getPath(jobData))) {
            return reader.getCube();
        } catch (Exception ex) {
            throw new ReportExportException("Error while trying to read report in memory", ex);
        }
    }

    private Path getPath(ReportJobData jobData) {
        return Paths.get(replaceHomeShortcut(reportFolder) + "/" + jobData.id() + ".avro");
    }

}
