package ru.magnit.magreportbackend.mapper.reportjob;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.reportjob.ReportJob;
import ru.magnit.magreportbackend.dto.response.reportjob.ReportJobMetadataResponse;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.report.ReportFieldMetadataResponseMapper;

@Service
@RequiredArgsConstructor
public class ReportJobMetadataResponseMapper implements Mapper<ReportJobMetadataResponse, ReportJob> {
    private final ReportFieldMetadataResponseMapper fieldMapper;

    @Override
    public ReportJobMetadataResponse from(ReportJob source) {
        return new ReportJobMetadataResponse(
                source.getId(),
                source.getRowCount(),
                fieldMapper.from(source.getReport().getFields())
        );
    }
}
