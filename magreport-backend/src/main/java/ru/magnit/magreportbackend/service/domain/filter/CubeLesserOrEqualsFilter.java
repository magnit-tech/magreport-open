package ru.magnit.magreportbackend.service.domain.filter;

import ru.magnit.magreportbackend.domain.dataset.DataTypeEnum;
import ru.magnit.magreportbackend.dto.inner.olap.CubeData;
import ru.magnit.magreportbackend.dto.request.olap.FilterDefinition;
import ru.magnit.magreportbackend.exception.InvalidParametersException;

import java.time.LocalDate;

public class CubeLesserOrEqualsFilter implements CubeFilterNode {

    private boolean invert;
    private String value;
    private String[] data;
    private DataTypeEnum dataType;

    @Override
    public CubeFilterNode init(CubeData sourceCube, FilterDefinition filterDefinition) {
        dataType = sourceCube.reportMetaData().getTypeField(filterDefinition.getFieldId());
        invert = filterDefinition.isInvertResult();
        value = filterDefinition.getValues().isEmpty() ? "" : filterDefinition.getValues().get(0);
        data = sourceCube.data()[sourceCube.fieldIndexes().get(filterDefinition.getFieldId())];

        return this;
    }

    @Override
    public boolean filter(int row) {
        var result = switch (dataType){
            case INTEGER, DOUBLE -> Double.parseDouble(data[row]) <= Double.parseDouble(value);
            case DATE -> LocalDate.parse(data[row]).isBefore(LocalDate.parse(value)) || data[row].equals(value);
            case STRING, TIMESTAMP-> throw new InvalidParametersException("Not supported datatype field");
        };

        return invert != result;
    }
}
