package ru.magnit.magreportbackend.dto.response.dataset;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@NoArgsConstructor
public class DataSetResponse {

    private Long id;
    private Long typeId;
    private String name;
    private String description;
    private String catalogName;
    private String schemaName;
    private String objectName;
    private Boolean isValid;
    private DataSourceResponse dataSource;
    private String userName;
    private List<DataSetFieldResponse> fields = Collections.emptyList();

    @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
    private LocalDateTime created;

    @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
    private LocalDateTime modified;
}
