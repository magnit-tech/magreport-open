package ru.magnit.magreportbackend.mapper.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataSetField;
import ru.magnit.magreportbackend.domain.report.PivotFieldType;
import ru.magnit.magreportbackend.domain.report.ReportField;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetFieldResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class ReportFieldMapperDataSet implements Mapper<ReportField, DataSetFieldResponse> {

    @Override
    public ReportField from(DataSetFieldResponse source) {
        var fieldCounter = new AtomicInteger(1);
        return new ReportField()
                .setPivotFieldType(new PivotFieldType(0L))
                .setVisible(true)
                .setOrdinal(fieldCounter.getAndIncrement())
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setDataSetField(new DataSetField(source.getId()));
    }
}
