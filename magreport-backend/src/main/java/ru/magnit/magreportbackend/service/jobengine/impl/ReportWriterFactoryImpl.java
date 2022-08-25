package ru.magnit.magreportbackend.service.jobengine.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.dto.inner.jobengine.ReportWriterData;
import ru.magnit.magreportbackend.service.jobengine.ReportWriter;
import ru.magnit.magreportbackend.service.jobengine.ReportWriterFactory;

import static ru.magnit.magreportbackend.util.FileUtils.replaceHomeShortcut;

@Service
@RequiredArgsConstructor
public class ReportWriterFactoryImpl implements ReportWriterFactory {

    private final AvroSchemaBuilder schemaBuilder;

    @Value("${magreport.reports.folder}")
    private String reportsPath;

    @Value("${magreport.jobengine.max-rows}")
    private Long maxRows;

    @Override
    public ReportWriter getWriter(ReportWriterData writerData) {

        return new ReportWriterImpl(schemaBuilder, writerData, replaceHomeShortcut(reportsPath), maxRows);
    }
}
