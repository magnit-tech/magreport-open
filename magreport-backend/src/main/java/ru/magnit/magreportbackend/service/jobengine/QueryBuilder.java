package ru.magnit.magreportbackend.service.jobengine;

import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobData;

public interface QueryBuilder {

    String getQuery(ReportJobData reportData);
}
