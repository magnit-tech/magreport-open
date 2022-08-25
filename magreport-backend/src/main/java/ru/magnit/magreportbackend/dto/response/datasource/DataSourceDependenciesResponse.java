package ru.magnit.magreportbackend.dto.response.datasource;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.datasource.DataSourceTypeEnum;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetDependenciesResponse;
import ru.magnit.magreportbackend.dto.response.user.UserResponse;

import java.time.LocalDateTime;
import java.util.List;

import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DataSourceDependenciesResponse {
    private long id;
    private DataSourceTypeEnum type;
    private String name;
    private String description;
    private String url;
    private String userName;
    private UserResponse creator;
    private List<DataSetDependenciesResponse> dataSets;

    @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
    LocalDateTime created;

    @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
    LocalDateTime modified;
}
