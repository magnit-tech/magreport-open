package ru.magnit.magreportbackend.mapper.filter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.dataset.DataTypeEnum;
import ru.magnit.magreportbackend.domain.datasource.DataSourceTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterOperationTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTypeEnum;
import ru.magnit.magreportbackend.dto.inner.datasource.DataSourceData;
import ru.magnit.magreportbackend.dto.inner.filter.FilterData;
import ru.magnit.magreportbackend.dto.inner.filter.FilterFieldData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobFilterData;
import ru.magnit.magreportbackend.dto.request.filterinstance.ChildNodesRequest;
import ru.magnit.magreportbackend.dto.tuple.TupleValue;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class FilterChildNodesRequestDataMergerTest {


    private static final String SCHEMA_NAME = "schema";
    private static final String TABLE_NAME = "table";
    private static final long ROOT_FIELD_ID = 5L;
    private static final String ID_FIELD_NAME = "id field name";
    private static final String NAME_FIELD_NAME = "name field name";
    private static final String NULL_VALUE = "'null'";
    public static final short POOL_SIZE = (short) 5;

    @InjectMocks
    private FilterChildNodesRequestDataMerger mapper;

    @Test
    void merge() {
        final var result = mapper.merge(getFilterData(), getChildNodesRequest(), getEffectiveSettings());

        assertEquals(SCHEMA_NAME, result.schemaName());
        assertEquals(TABLE_NAME, result.tableName());
        assertEquals(ROOT_FIELD_ID, result.rootFieldId());
        assertEquals(ROOT_FIELD_ID, result.responseFieldId());
        assertEquals(ID_FIELD_NAME, result.idFieldName());
        assertEquals(NAME_FIELD_NAME, result.nameFieldName());
        assertEquals(0L, result.level());
        assertEquals(NULL_VALUE, result.pathValues().get(ID_FIELD_NAME));
        assertNotNull(result.dataSource());
    }

    private List<ReportJobFilterData> getEffectiveSettings() {
        return Collections.singletonList(new ReportJobFilterData(
                1L,
                FilterTypeEnum.HIERARCHY,
                FilterOperationTypeEnum.IS_IN_LIST,
                "",
                Collections.emptyList()
        ));
    }

    private ChildNodesRequest getChildNodesRequest() {
        return new ChildNodesRequest()
                .setFilterId(1L)
                .setPathNodes(Collections.singletonList(new TupleValue().setFieldId(5L).setValue(null)));
    }

    private FilterData getFilterData() {
        return new FilterData(
                new DataSourceData(3L, DataSourceTypeEnum.TERADATA, "teradata url", "user", "password", POOL_SIZE),
                4L,
                FilterTypeEnum.HIERARCHY,
                SCHEMA_NAME,
                TABLE_NAME,
                "filter name",
                "code filter",
                "filter description",
                List.of(new FilterFieldData(
                                ROOT_FIELD_ID,
                        1L,
                        "filter field name",
                        "filter field description",
                                ID_FIELD_NAME,
                        DataTypeEnum.STRING,
                        FilterFieldTypeEnum.ID_FIELD
                ),
                        new FilterFieldData(
                                6L,
                                1L,
                                "filter field name",
                                "filter field description",
                                NAME_FIELD_NAME,
                                DataTypeEnum.STRING,
                                FilterFieldTypeEnum.NAME_FIELD
                        ))
        );
    }
}