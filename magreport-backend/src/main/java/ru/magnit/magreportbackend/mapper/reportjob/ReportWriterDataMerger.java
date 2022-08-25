package ru.magnit.magreportbackend.mapper.reportjob;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.dto.inner.jobengine.CacheRow;
import ru.magnit.magreportbackend.dto.inner.jobengine.ReportWriterData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobData;
import ru.magnit.magreportbackend.mapper.Merger3;

import java.util.concurrent.BlockingQueue;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class ReportWriterDataMerger implements Merger3<ReportWriterData, ReportJobData, BlockingQueue<CacheRow>, Supplier<Boolean>> {

    @Override
    public ReportWriterData merge(ReportJobData source1, BlockingQueue<CacheRow> source2, Supplier<Boolean> source3) {

        return new ReportWriterData(
                source1.id(),
                source1.dataSource(),
                source1.reportData(),
                source2,
                source3
        );
    }
}
