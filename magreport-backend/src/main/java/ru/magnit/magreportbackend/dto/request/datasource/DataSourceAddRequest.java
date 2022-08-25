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
public class DataSourceAddRequest {

    private Long id;
    private Long folderId;
    private Long typeId;
    private String name;
    private String description;
    private String url;
    private String userName;
    private String password;
    private Short poolSize;
}
