package ru.magnit.magreportbackend.dto.response.dataset;

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
public class DataSetDataTypeResponse {

    private Long id;
    private String name;
    private String description;
}
