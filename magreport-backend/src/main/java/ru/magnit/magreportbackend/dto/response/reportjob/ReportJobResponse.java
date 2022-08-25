package ru.magnit.magreportbackend.dto.response.reportjob;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobStateEnum;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobStatusEnum;
import ru.magnit.magreportbackend.dto.response.exceltemplate.ReportExcelTemplateResponse;
import ru.magnit.magreportbackend.dto.response.report.ReportShortResponse;
import ru.magnit.magreportbackend.dto.response.user.UserShortResponse;

import java.time.LocalDateTime;
import java.util.List;

import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ReportJobResponse {

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        Long id;

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        ReportShortResponse report;

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        UserShortResponse user;

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        ReportJobStatusEnum status;

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        ReportJobStateEnum state;

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        String message;

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        Long rowCount;

        @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
        LocalDateTime created;

        @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
        LocalDateTime modified;

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        List<ReportExcelTemplateResponse> excelTemplates;

        @JsonFormat(shape = JsonFormat.Shape.BOOLEAN)
        Boolean canExecute;


}
