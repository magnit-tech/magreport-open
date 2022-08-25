package ru.magnit.magreportbackend.dto.response.report;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@NoArgsConstructor
public class ReportFieldResponse {

    private Long id;
    private Long dataSetFieldId;
    private String name;
    private String description;
    private Integer ordinal;
    private Boolean visible;
    private Boolean valid;
    private Long pivotTypeId;
    private LocalDateTime created;
    private LocalDateTime modified;
}
