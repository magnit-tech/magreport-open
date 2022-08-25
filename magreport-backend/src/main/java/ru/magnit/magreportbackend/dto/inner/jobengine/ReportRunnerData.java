package ru.magnit.magreportbackend.dto.inner.jobengine;

import ru.magnit.magreportbackend.service.jobengine.ReportReader;
import ru.magnit.magreportbackend.service.jobengine.ReportWriter;

public record ReportRunnerData(
        ReportReader reader,
        ReportWriter writer
) {
}
