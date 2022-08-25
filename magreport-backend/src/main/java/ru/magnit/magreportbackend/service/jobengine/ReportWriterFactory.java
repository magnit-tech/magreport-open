package ru.magnit.magreportbackend.service.jobengine;

import ru.magnit.magreportbackend.dto.inner.jobengine.ReportWriterData;

public interface ReportWriterFactory {

    ReportWriter getWriter(ReportWriterData writerData);
}
