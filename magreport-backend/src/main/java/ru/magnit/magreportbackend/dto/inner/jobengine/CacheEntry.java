package ru.magnit.magreportbackend.dto.inner.jobengine;

import ru.magnit.magreportbackend.dto.inner.reportjob.ReportFieldData;

public record CacheEntry(
        ReportFieldData fieldData,
        String value
) {
}
