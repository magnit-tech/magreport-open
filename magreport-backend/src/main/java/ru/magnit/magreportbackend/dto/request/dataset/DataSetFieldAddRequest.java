package ru.magnit.magreportbackend.dto.request.dataset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DataSetFieldAddRequest {

    private Long id;
    private String name;
    private String description;
    private Long typeId;
    private Boolean isValid;
}
