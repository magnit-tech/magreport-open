package ru.magnit.magreportbackend.dto.request.datasource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class DataSourceCheckRequest {
    private String url;
    private String userName;
    private String password;
}
