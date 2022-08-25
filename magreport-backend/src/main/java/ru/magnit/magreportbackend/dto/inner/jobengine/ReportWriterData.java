package ru.magnit.magreportbackend.dto.inner.jobengine;

import ru.magnit.magreportbackend.dto.inner.datasource.DataSourceData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportData;

import java.util.concurrent.BlockingQueue;
import java.util.function.Supplier;

public record ReportWriterData (

        long jobId,
        DataSourceData dataSource,
        ReportData report,
        BlockingQueue<CacheRow>cache,
        Supplier<Boolean> isCacheEmpty
){}
