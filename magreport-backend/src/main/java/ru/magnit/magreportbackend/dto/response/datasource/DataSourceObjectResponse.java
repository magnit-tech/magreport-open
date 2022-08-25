package ru.magnit.magreportbackend.dto.response.datasource;

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
public class DataSourceObjectResponse {

    private String catalog;
    private String schema;
    private String name;
    private String type;
    private String comment;
}
