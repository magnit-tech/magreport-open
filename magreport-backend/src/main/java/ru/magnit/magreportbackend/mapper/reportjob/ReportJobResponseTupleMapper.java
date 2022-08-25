package ru.magnit.magreportbackend.mapper.reportjob;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobStateEnum;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobStatusEnum;
import ru.magnit.magreportbackend.dto.response.exceltemplate.ReportExcelTemplateResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportShortResponse;
import ru.magnit.magreportbackend.dto.response.reportjob.ReportJobResponse;
import ru.magnit.magreportbackend.dto.response.user.UserShortResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

import javax.persistence.Tuple;
import java.sql.Clob;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class ReportJobResponseTupleMapper implements Mapper<ReportJobResponse, Tuple> {
    @Override
    public ReportJobResponse from(Tuple source) {
        return new ReportJobResponse(
                source.get("REPORT_JOB_ID", Integer.class).longValue(),
                new ReportShortResponse(
                        source.get("REPORT_ID", Integer.class).longValue(),
                        source.get("REPORTNAME", String.class)
                ),
                new UserShortResponse(
                        source.get("USER_ID", Integer.class).longValue(),
                        source.get("USERNAME", String.class)
                ),
                ReportJobStatusEnum.getById(source.get("REPORT_JOB_STATUS_ID", Integer.class).longValue()),
                ReportJobStateEnum.getById(source.get("REPORT_JOB_STATE_ID", Integer.class).longValue()),
                getClobMessage(source.get("MESSAGE", Clob.class)),
                source.get("ROW_COUNT", Integer.class).longValue(),
                source.get("CREATED", Timestamp.class).toLocalDateTime(),
                source.get("MODIFIED", Timestamp.class).toLocalDateTime(),
                new ArrayList<>(Collections.singletonList(
                        new ReportExcelTemplateResponse()
                                .setExcelTemplateId(source.get("EXCEL_TEMPLATE_ID", Integer.class).longValue())
                                .setName(source.get("NAME", String.class))
                                .setDescription(source.get("DESCRIPTION", String.class))
                                .setIsDefault(source.get("IS_DEFAULT", Boolean.class))

                )),
                false
        );
    }

    @SneakyThrows
    private String getClobMessage(Clob clob) {
        return clob == null ? null : clob.getSubString(1, (int) clob.length());
    }
}
