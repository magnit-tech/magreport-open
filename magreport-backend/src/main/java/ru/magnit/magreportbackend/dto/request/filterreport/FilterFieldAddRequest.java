package ru.magnit.magreportbackend.dto.request.filterreport;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class FilterFieldAddRequest {

    private Long id;
    private String name;
    private String description;
    private Long ordinal;
    private Long filterInstanceFieldId;
    private Long reportFieldId;
    private Boolean expand;
}
