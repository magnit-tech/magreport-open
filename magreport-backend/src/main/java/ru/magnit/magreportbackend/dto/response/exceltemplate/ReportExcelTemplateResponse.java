package ru.magnit.magreportbackend.dto.response.exceltemplate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ReportExcelTemplateResponse {
    Long excelTemplateId;
    String name;
    String description;
    @JsonProperty ( value = "default")
    Boolean isDefault;
}
