package ru.magnit.magreportbackend.service.domain.converter;

import ru.magnit.magreportbackend.dto.inner.reportjob.ReportData;

import java.nio.file.Path;


public interface WriterFactory {

    Writer createWriter(Reader reader, ReportData data, Path exportPath);
}
