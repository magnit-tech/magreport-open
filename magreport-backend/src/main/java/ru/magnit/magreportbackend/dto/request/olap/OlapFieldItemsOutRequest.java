package ru.magnit.magreportbackend.dto.request.olap;

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

public class OlapFieldItemsOutRequest {

    private Long jobId;
    private Long fieldId;
    private FilterGroup filterGroup;
    private Long from;
    private Long count;
    private Long cubeSize;
    private List<CubeField> fields = Collections.emptyList();
}
