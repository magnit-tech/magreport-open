package ru.magnit.magreportbackend.mapper.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.dto.response.report.ScheduleReportResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ScheduleReportResponseMapper implements Mapper<ScheduleReportResponse, Report> {
    @Override
    public ScheduleReportResponse from(Report source) {
        return new ScheduleReportResponse()
                .setId(source.getId())
                .setDataSetId(source.getDataSet().getId())
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setRequirementsLink(source.getRequirementsLink());

    }
}
