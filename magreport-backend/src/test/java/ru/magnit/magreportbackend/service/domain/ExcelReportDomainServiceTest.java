package ru.magnit.magreportbackend.service.domain;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.magnit.magreportbackend.domain.datasource.DataSourceTypeEnum;
import ru.magnit.magreportbackend.dto.inner.datasource.DataSourceData;
import ru.magnit.magreportbackend.dto.inner.jobengine.CacheEntry;
import ru.magnit.magreportbackend.dto.inner.jobengine.CacheRow;
import ru.magnit.magreportbackend.dto.inner.olap.CubeData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobData;
import ru.magnit.magreportbackend.dto.request.olap.OlapCubeRequest;
import ru.magnit.magreportbackend.exception.ReportExportException;
import ru.magnit.magreportbackend.service.domain.converter.Reader;
import ru.magnit.magreportbackend.service.domain.converter.ReaderFactory;
import ru.magnit.magreportbackend.service.domain.converter.Writer;
import ru.magnit.magreportbackend.service.domain.converter.WriterFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ExcelReportDomainServiceTest {

    @InjectMocks
    private ExcelReportDomainService domainService;

    @Mock
    private ReaderFactory readerFactory;

    @Mock
    private WriterFactory writerFactory;

    private static final Long ID = 1L;
    private static final String TEMPLATE_EXTENSION = ".xlsm";


    @Test
    void getExcelReport() throws IOException {

        ReflectionTestUtils.setField(domainService, "maxRows", 10000);
        ReflectionTestUtils.setField(domainService, "reportFolder", "test/reportFolder");
        ReflectionTestUtils.setField(domainService, "rmsInFolder", "test/rmsInFolder");
        ReflectionTestUtils.setField(domainService, "rmsOutFolder", "test/rmsOutFolder");

        Files.createDirectories(Path.of("test/reportFolder"));
        Files.createDirectory(Path.of("test/rmsInFolder"));
        Files.createDirectory(Path.of("test/rmsOutFolder"));
        Files.createFile(Path.of("test/reportFolder/" + ID + "-" + ID + TEMPLATE_EXTENSION));
        Files.createFile(Path.of("test/rmsInFolder/" + ID + "-" + ID + TEMPLATE_EXTENSION));
        Files.createFile(Path.of("test/rmsOutFolder/" + ID + "-" + ID + TEMPLATE_EXTENSION));

        assertNotNull(domainService.getExcelReport(getReportJobData(), "", ID));

        FileUtils.deleteDirectory(new File("test/"));

        when(readerFactory.createReader(any(ReportJobData.class), any())).thenReturn(getReader());
        when(writerFactory.createWriter(any(Reader.class), any(ReportData.class), any())).thenReturn(getWriter());

        assertNotNull(domainService.getExcelReport(getReportJobData(), "", ID));

    }

    @Test
    void copyReportToRms() {

        var path1 = Path.of("test/reportFolder");
        var path2 = Path.of("test/rmsInFolder");

        assertThrows(ReportExportException.class, () ->
                ReflectionTestUtils.invokeMethod(domainService, "copyReportToRms", path1, path2));

    }


    @Test
    void createExcelReport() {

        ReflectionTestUtils.setField(domainService, "maxRows", 10000);
        ReflectionTestUtils.setField(domainService, "reportFolder", "test/reportFolder");
        ReflectionTestUtils.setField(domainService, "rmsInFolder", "test/rmsInFolder");
        ReflectionTestUtils.setField(domainService, "rmsOutFolder", "test/rmsOutFolder");

        when(readerFactory.createReader(any(ReportJobData.class), any())).thenReturn(getReader());
        when(writerFactory.createWriter(any(Reader.class), any(ReportData.class), any())).thenReturn(getWriter());

        domainService.createExcelReport(getReportJobData(), "", ID);

        verify(readerFactory).createReader(any(ReportJobData.class), any());
        verify(writerFactory).createWriter(any(Reader.class), any(ReportData.class), any());

        verifyNoMoreInteractions(readerFactory, writerFactory);
    }

    @Test
    void moveReportToRms() throws IOException {

        ReflectionTestUtils.setField(domainService, "reportFolder", "test/reportFolder");
        ReflectionTestUtils.setField(domainService, "rmsInFolder", "test/rmsInFolder");
        ReflectionTestUtils.setField(domainService, "rmsOutFolder", "test/rmsOutFolder");

        Files.createDirectories(Path.of("test/reportFolder"));
        Files.createDirectory(Path.of("test/rmsInFolder"));
        Files.createFile(Path.of("test/reportFolder/" + ID + "-" + ID + TEMPLATE_EXTENSION));

        domainService.moveReportToRms(ID, ID);

        assertThrows(ReportExportException.class, () -> domainService.moveReportToRms(ID, ID));

    }

    @Test
    void saveReportToExcel() {

        var jobData = getReportJobData();
        ReflectionTestUtils.setField(domainService, "reportFolder", "test/reportFolder");
        assertThrows(ReportExportException.class, () ->
                domainService.saveReportToExcel(jobData, "", ID));
    }

    @Test
    void getReportSize() throws IOException {

        ReflectionTestUtils.setField(domainService, "rmsOutFolder", "test/rmsOutFolder");
        Files.createDirectories(Path.of("test/rmsOutFolder"));
        Files.createFile(Path.of("test/rmsOutFolder/" + ID + "-" + ID + TEMPLATE_EXTENSION));

        long size = domainService.getReportSize(ID, ID);

        assertEquals(0L, size);
    }

    @AfterEach
    void clear() throws IOException {
        FileUtils.deleteDirectory(new File("test/"));
    }

    private ReportJobData getReportJobData() {
        return new ReportJobData(
                ID, ID, ID, ID, ID, "",
                ID, ID, ID, false,
                new DataSourceData(
                        ID,
                        DataSourceTypeEnum.IMPALA,
                        "",
                        "",
                        "",
                        (short) 1),
                new ReportData(
                        ID,
                        "",
                        "",
                        "",
                        "",
                        Collections.emptyList(),
                        null
                ),
                Collections.emptyList(),
                Collections.emptyList());
    }

    private Reader getReader() {
        return new Reader() {
            @Override
            public CacheRow getRow() {
                return null;
            }

            @Override
            public List<Map<Long, CacheEntry>> getAllRows(Set<Long> allFieldIds) {
                return null;
            }

            @Override
            public CubeData getCube() {
                return null;
            }

            @Override
            public void close(){

            }
        };
    }

    private Writer getWriter() {
        return templatePath -> {

            try {
                Files.createDirectories(Path.of("test/reportFolder"));
                Files.createFile(Path.of("test/reportFolder/" + ID + "-" + ID + TEMPLATE_EXTENSION));
                Files.createDirectory(Path.of("test/rmsInFolder"));
                Files.createDirectory(Path.of("test/rmsOutFolder"));
                Files.createFile(Path.of("test/rmsOutFolder/" + ID + "-" + ID + TEMPLATE_EXTENSION));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        };
    }

}
