package ru.magnit.magreportbackend.mapper.reportjob;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.reportjob.ReportJob;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobStateEnum;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobStatusEnum;
import ru.magnit.magreportbackend.dto.response.reportjob.ReportJobResponse;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.auth.UserShortResponseMapper;
import ru.magnit.magreportbackend.mapper.report.ReportShortResponseMapper;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ReportJobResponseMapper implements Mapper<ReportJobResponse, ReportJob> {

    private final UserShortResponseMapper userResponseMapper;
    private final ReportShortResponseMapper reportResponseMapper;

    @Override
    public ReportJobResponse from(ReportJob source) {

        if (source == null) return null;
        return mapBaseProperties(source);
    }

    private ReportJobResponse mapBaseProperties(ReportJob source) {
        return new ReportJobResponse(
            source.getId(),
            reportResponseMapper.from(source.getReport()),
            userResponseMapper.from(source.getUser()),
            ReportJobStatusEnum.getById(source.getStatus().getId()),
            ReportJobStateEnum.getById(source.getState().getId()),
            source.getMessage(),
            source.getRowCount(),
            source.getCreatedDateTime(),
            source.getModifiedDateTime(),
            new ArrayList<>(),
                false);
    }
}
