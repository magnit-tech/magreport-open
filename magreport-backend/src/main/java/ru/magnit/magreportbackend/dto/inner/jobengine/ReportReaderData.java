package ru.magnit.magreportbackend.dto.inner.jobengine;

import ru.magnit.magreportbackend.dto.inner.datasource.DataSourceData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobData;

import java.util.concurrent.BlockingQueue;

public record ReportReaderData (

        long jobId,
        DataSourceData dataSource,
        ReportJobData report,
        BlockingQueue<CacheRow> cache
){}
