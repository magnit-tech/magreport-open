package ru.magnit.magreportbackend.dto.request.dataset;

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
public class DataSetCreateFromMetaDataRequest {

    private Long dataSourceId;
    private Long folderId;
    private Long typeId;
    private String name;
    private String description;
    private String catalogName;
    private String schemaName;
    private String objectName;
}
