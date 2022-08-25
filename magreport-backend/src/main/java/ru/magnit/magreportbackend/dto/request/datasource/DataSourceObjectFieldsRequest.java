package ru.magnit.magreportbackend.dto.request.datasource;

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
public class DataSourceObjectFieldsRequest {

    private Long id;
    private String catalogName;
    private String schemaName;
    private String objectName;
}
