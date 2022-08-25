package ru.magnit.magreportbackend.dto.request.reportjob;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
@AllArgsConstructor
public class ExcelReportRequest {

    private Long id;
    private Long excelTemplateId;
}
