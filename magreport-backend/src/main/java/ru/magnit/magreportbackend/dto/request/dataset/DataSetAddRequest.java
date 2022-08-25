package ru.magnit.magreportbackend.dto.request.dataset;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class DataSetAddRequest {

    private Long id;
    private Long folderId;
    private Long typeId;
    private Long dataSourceId;
    private String name;
    private String description;
    private String catalogName;
    private String schemaName;
    private String objectName;
    private List<DataSetFieldAddRequest> fields = Collections.emptyList();
}
