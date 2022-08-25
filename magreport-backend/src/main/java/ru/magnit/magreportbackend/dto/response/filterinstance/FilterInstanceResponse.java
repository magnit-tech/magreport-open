package ru.magnit.magreportbackend.dto.response.filterinstance;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterTypeResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@NoArgsConstructor
public class FilterInstanceResponse {

    private Long id;
    private Long folderId;
    private Long templateId;
    private FilterTypeResponse type;
    private Long dataSetId;
    private String name;
    private String description;
    private String code;
    private List<FilterInstanceFieldResponse> fields = Collections.emptyList();

    private String userName;

    @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
    private LocalDateTime created;

    @JsonFormat(pattern = ISO_DATE_TIME_PATTERN)
    private LocalDateTime modified;
}
