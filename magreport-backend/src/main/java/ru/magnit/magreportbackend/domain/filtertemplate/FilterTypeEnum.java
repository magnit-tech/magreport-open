package ru.magnit.magreportbackend.domain.filtertemplate;

import ru.magnit.magreportbackend.domain.dataset.DataTypeEnum;

public enum FilterTypeEnum {
    SINGLE_VALUE_UNBOUNDED,
    RANGE,
    VALUE_LIST_UNBOUNDED,
    VALUE_LIST,
    TOKEN_INPUT,
    HIERARCHY,
    HIERARCHY_M2M,
    DATE_RANGE,
    DATE_VALUE;

    public static FilterTypeEnum getByOrdinal(long id) {
        return FilterTypeEnum.values()[(int) id];
    }

    public static DataTypeEnum getDataTypeFilter(long id) {
        if( FilterTypeEnum.getByOrdinal(id) == DATE_RANGE ||
                FilterTypeEnum.getByOrdinal(id) == DATE_VALUE ) return DataTypeEnum.DATE;
        return null;
    }

}
