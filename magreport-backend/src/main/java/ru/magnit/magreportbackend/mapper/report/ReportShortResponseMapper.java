package ru.magnit.magreportbackend.mapper.report;

import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.dto.response.report.ReportShortResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
public class ReportShortResponseMapper implements Mapper<ReportShortResponse, Report> {

    @Override
    public ReportShortResponse from(Report source) {
        return new ReportShortResponse(source.getId(), source.getName());
    }
}
