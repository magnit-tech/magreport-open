package ru.magnit.magreportbackend.mapper.reportjob;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.dto.inner.jobengine.CacheRow;
import ru.magnit.magreportbackend.dto.inner.jobengine.ReportReaderData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobData;
import ru.magnit.magreportbackend.mapper.Merger2;

import java.util.concurrent.BlockingQueue;

@Service
@RequiredArgsConstructor
public class ReportReaderDataMerger implements Merger2<ReportReaderData, ReportJobData, BlockingQueue<CacheRow>> {

    @Override
    public ReportReaderData merge(ReportJobData source1, BlockingQueue<CacheRow> source2) {
        return new ReportReaderData(
                source1.id(),
                source1.dataSource(),
                source1,
                source2
        );
    }
}
