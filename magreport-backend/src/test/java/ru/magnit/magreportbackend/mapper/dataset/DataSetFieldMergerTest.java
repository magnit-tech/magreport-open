package ru.magnit.magreportbackend.mapper.dataset;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceField;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.domain.dataset.DataSetField;
import ru.magnit.magreportbackend.domain.dataset.DataType;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceField;
import ru.magnit.magreportbackend.domain.report.ReportField;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterDataSetField;
import ru.magnit.magreportbackend.dto.request.dataset.DataSetFieldAddRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class DataSetFieldMergerTest {
    @Mock
    private DataSetFieldMapper dataSetFieldMapper;

    @InjectMocks
    private DataSetFieldMerger merger;

    private final static Long ID = 1L;
    private final static String NAME = "Name";
    private final static String DESCRIPTION = "Description";
    private final static Boolean SYNC = true;
    private final static LocalDateTime CREATE_TIME = LocalDateTime.now();
    private final static LocalDateTime MODIFIED_TIME = LocalDateTime.now();

    @Test
    void merge() {

        when(dataSetFieldMapper.from(anyList())).thenReturn(Collections.singletonList(getDataSetField()));

        var responseList = merger.merge(new ArrayList<>(Collections.singletonList(getDataSetField())), Collections.singletonList(getDataSetFieldAddRequest()));
        var response = responseList.get(0);

        assertEquals(2, responseList.size());
        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(!SYNC, response.getIsSync());
        assertEquals(1, response.getAuthSourceFields().size());
        assertEquals(1, response.getFieldMappings().size());
        assertEquals(1, response.getInstanceFields().size());
        assertEquals(1, response.getReportFields().size());
        assertEquals(CREATE_TIME, response.getCreatedDateTime());
        assertEquals(MODIFIED_TIME, response.getModifiedDateTime());

        verify(dataSetFieldMapper).from(anyList());
        verifyNoMoreInteractions(dataSetFieldMapper);
    }

    private DataSetField getDataSetField() {
        return new DataSetField()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setDataSet(new DataSet())
                .setType(new DataType())
                .setIsSync(SYNC)
                .setAuthSourceFields(Collections.singletonList(new ExternalAuthSourceField()))
                .setFieldMappings(Collections.singletonList(new SecurityFilterDataSetField()))
                .setInstanceFields(Collections.singletonList(new FilterInstanceField()))
                .setReportFields(Collections.singletonList(new ReportField()))
                .setCreatedDateTime(CREATE_TIME)
                .setModifiedDateTime(MODIFIED_TIME);

    }

    private DataSetFieldAddRequest getDataSetFieldAddRequest() {
        return new DataSetFieldAddRequest()
                .setId(2L)
                .setName("new" + NAME)
                .setDescription(DESCRIPTION)
                .setTypeId(ID)
                .setIsValid(false);
    }

}
