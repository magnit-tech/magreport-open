package ru.magnit.magreportbackend.domain.olap;

import ru.magnit.magreportbackend.domain.dataset.DataTypeEnum;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.metrics_function.impl.AvgIntegerFunction;
import ru.magnit.magreportbackend.metrics_function.impl.MaxIntegerFunction;
import ru.magnit.magreportbackend.metrics_function.MetricsFunction;
import ru.magnit.magreportbackend.metrics_function.impl.AvgDoubleFunction;
import ru.magnit.magreportbackend.metrics_function.impl.CountDistinctFunction;
import ru.magnit.magreportbackend.metrics_function.impl.CountFunction;
import ru.magnit.magreportbackend.metrics_function.impl.MaxDoubleFunction;
import ru.magnit.magreportbackend.metrics_function.impl.MaxStringFunction;
import ru.magnit.magreportbackend.metrics_function.impl.MinDoubleFunction;
import ru.magnit.magreportbackend.metrics_function.impl.MinIntegerFunction;
import ru.magnit.magreportbackend.metrics_function.impl.MinStringFunction;
import ru.magnit.magreportbackend.metrics_function.impl.SumDoubleFunction;
import ru.magnit.magreportbackend.metrics_function.impl.SumIntegerFunction;

public enum AggregationType {
    COUNT,
    COUNT_DISTINCT,
    SUM,
    MAX,
    MIN,
    AVG;

    private static final String ERROR_MESSAGE = "Aggregation %s does not support data type %s";

    public MetricsFunction getMetricsFunction(DataTypeEnum dataType) {
       return switch (this) {
            case COUNT -> new CountFunction();
            case COUNT_DISTINCT -> new CountDistinctFunction();

            case SUM -> switch (dataType) {
                case INTEGER -> new SumIntegerFunction();
                case DOUBLE -> new SumDoubleFunction();
                case STRING,DATE,TIMESTAMP -> throw new InvalidParametersException(String.format(ERROR_MESSAGE,this,dataType));
            };

            case MAX -> switch (dataType) {
                case INTEGER -> new MaxIntegerFunction();
                case DOUBLE -> new MaxDoubleFunction();
                case STRING, DATE , TIMESTAMP -> new MaxStringFunction();
                };

            case MIN -> switch (dataType){
                case INTEGER -> new MinIntegerFunction();
                case DOUBLE -> new MinDoubleFunction();
                case STRING, DATE, TIMESTAMP -> new MinStringFunction();
            };

            case AVG ->  switch (dataType) {
                case INTEGER -> new AvgIntegerFunction();
                case DOUBLE -> new AvgDoubleFunction();
                case STRING,DATE,TIMESTAMP -> throw new InvalidParametersException(String.format(ERROR_MESSAGE,this, dataType));
            };
        };
    }

    public DataTypeEnum getDataTypeMetricFunction (DataTypeEnum dataTypeField) {

        return switch (this){
            case COUNT,COUNT_DISTINCT -> DataTypeEnum.INTEGER;
            case SUM,MAX,MIN -> dataTypeField;
            case AVG -> DataTypeEnum.DOUBLE;
        };
    }
}
