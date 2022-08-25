package ru.magnit.magreportbackend.dto.response.filterinstance;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FilterInstanceFieldResponse {

    private Long id;
    private FilterFieldTypeEnum type;
    private Long templateFieldId;
    private Long dataSetFieldId;
    private Long level;
    private String name;
    private String description;
    private LocalDateTime created;
    private LocalDateTime modified;
    private Boolean expand;
}
