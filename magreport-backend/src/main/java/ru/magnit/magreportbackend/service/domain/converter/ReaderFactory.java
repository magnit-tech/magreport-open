package ru.magnit.magreportbackend.service.domain.converter;

import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobData;

import java.nio.file.Path;

public interface ReaderFactory {

    Reader createReader(ReportJobData data, Path inputPath);
}
