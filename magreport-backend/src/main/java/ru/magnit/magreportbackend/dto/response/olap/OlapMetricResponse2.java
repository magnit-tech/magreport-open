package ru.magnit.magreportbackend.dto.response.olap;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.olap.AggregationType;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@NoArgsConstructor
public class OlapMetricResponse2 {

    private Long fieldId;

    private AggregationType aggregationType;

    private String[][] values;
}


