package ru.magnit.magreportbackend.service.domain.filter;

import ru.magnit.magreportbackend.domain.dataset.DataTypeEnum;
import ru.magnit.magreportbackend.dto.inner.olap.CubeData;
import ru.magnit.magreportbackend.dto.request.olap.FilterDefinition;
import ru.magnit.magreportbackend.exception.InvalidParametersException;

import java.time.LocalDate;
import java.util.List;

public class CubeBetweenFilter implements CubeFilterNode {

    private boolean invert;
    private List<String> values;
    private String[] data;

    private DataTypeEnum dataType;

    @Override
    public CubeFilterNode init(CubeData sourceCube, FilterDefinition filterDefinition) {
        dataType = sourceCube.reportMetaData().getTypeField(filterDefinition.getFieldId());
        invert = filterDefinition.isInvertResult();
        values = filterDefinition.getValues().stream().sorted().toList();
        data = sourceCube.data()[sourceCube.fieldIndexes().get(filterDefinition.getFieldId())];
        return this;
    }

    @Override
    public boolean filter(int row) {
        var result = switch (dataType) {
            case INTEGER, DOUBLE -> values.size() == 2 ?
                    Double.parseDouble(values.get(0)) < Double.parseDouble(data[row]) && Double.parseDouble(data[row]) < Double.parseDouble(values.get(1)) :
                    Double.MIN_VALUE < Double.parseDouble(data[row]) && Double.parseDouble(data[row]) < Double.MAX_VALUE;
            case DATE -> values.size() == 2 ?
                    LocalDate.parse(data[row]).isAfter(LocalDate.parse(values.get(0))) && LocalDate.parse(data[row]).isBefore(LocalDate.parse(values.get(1))) :
                    LocalDate.parse(data[row]).isAfter(LocalDate.MIN) && LocalDate.parse(data[row]).isBefore(LocalDate.MAX);

            case STRING, TIMESTAMP -> throw new InvalidParametersException("Not supported datatype field");
        };

        return invert != result;
    }
}
