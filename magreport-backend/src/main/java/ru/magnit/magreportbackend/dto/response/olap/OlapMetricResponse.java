package ru.magnit.magreportbackend.dto.response.olap;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.dataset.DataTypeEnum;
import ru.magnit.magreportbackend.domain.olap.AggregationType;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@NoArgsConstructor
public class OlapMetricResponse {

    private Long fieldId;

    private AggregationType aggregationType;

    private List<List<String>> values;

    private DataTypeEnum dataType;
}
