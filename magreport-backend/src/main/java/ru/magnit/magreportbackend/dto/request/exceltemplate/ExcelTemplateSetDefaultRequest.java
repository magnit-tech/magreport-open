package ru.magnit.magreportbackend.dto.request.exceltemplate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@NoArgsConstructor
public class ExcelTemplateSetDefaultRequest {

    Long excelTemplateId;

    Long reportId;
}
