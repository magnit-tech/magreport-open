package ru.magnit.magreportbackend.dto.response.olap;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.util.Pair;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@NoArgsConstructor
public class OlapFieldItemsResponse {

    private String[] valueList = new String[0];
    private int countValues = 0;

    public OlapFieldItemsResponse(Pair<String[],Long> result) {
        this.valueList = result.getL();
        countValues = result.getR().intValue();
    }
}
