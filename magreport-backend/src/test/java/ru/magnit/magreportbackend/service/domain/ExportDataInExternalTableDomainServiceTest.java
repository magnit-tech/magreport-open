package ru.magnit.magreportbackend.service.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.dataset.DataTypeEnum;
import ru.magnit.magreportbackend.domain.datasource.DataSourceTypeEnum;
import ru.magnit.magreportbackend.domain.filterreport.GroupOperationTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterOperationTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTypeEnum;
import ru.magnit.magreportbackend.dto.inner.datasource.DataSourceData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportFilterData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportFilterGroupData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportFilterLevelData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobFilterData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobTupleData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobTupleFieldData;
import ru.magnit.magreportbackend.service.jobengine.filter.FilterQueryExecutor;


import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class ExportDataInExternalTableDomainServiceTest {

    @Mock
    private FilterQueryExecutor filterQueryService;

    @InjectMocks
    private ExportDataInExternalTableDomainService domainService;

    private final static Long ID = 1L;
    private final static String NAME = "NAME";


    @Test
    void exportData() {

        domainService.exportData(getReportJobData());

        verify(filterQueryService).executeSql(any(),any());
        verifyNoMoreInteractions(filterQueryService);

        Mockito.reset(filterQueryService);

        domainService.exportData(getEmptyReportJobData());

        verifyNoMoreInteractions(filterQueryService);
    }

    private ReportJobData getEmptyReportJobData() {
        return new ReportJobData(
                ID,
                ID,
                ID,
                ID,
                ID,
                NAME,
                ID,
                ID,
                ID,
                false,
                new DataSourceData(ID, DataSourceTypeEnum.IMPALA, "url", NAME, "", (short) 1),
                new ReportData(
                        ID,
                        NAME,
                        NAME,
                        NAME,
                        NAME,
                        Collections.emptyList(),
                        null),
                Collections.emptyList(),
                Collections.emptyList()
        );
    }

    private ReportJobData getReportJobData() {
        return new ReportJobData(
                ID,
                ID,
                ID,
                ID,
                ID,
                NAME,
                ID,
                ID,
                ID,
                false,
                new DataSourceData(ID, DataSourceTypeEnum.IMPALA, "url", NAME, "", (short) 1),
                new ReportData(
                        ID,
                        NAME,
                        NAME,
                        NAME,
                        NAME,
                        Collections.emptyList(),
                        new ReportFilterGroupData(
                                ID,
                                ID,
                                "CODE",
                                "CODE",
                                GroupOperationTypeEnum.OR,
                                Collections.singletonList(new ReportFilterData(
                                        ID,
                                        new DataSourceData(
                                                ID,
                                                DataSourceTypeEnum.IMPALA,
                                                NAME,
                                                NAME,
                                                NAME,
                                                (short) 1
                                        ),
                                        ID,
                                        NAME,
                                        NAME,
                                        NAME,
                                        Collections.singletonList(new ReportFilterLevelData(
                                                ID,
                                                ID,
                                                DataTypeEnum.DATE,
                                                NAME,
                                                NAME,
                                                NAME,
                                                DataTypeEnum.DATE
                                        ))
                                )),
                                Collections.singletonList(new ReportFilterGroupData(
                                        ID,
                                        ID,
                                        "CODE",
                                        "CODE",
                                        GroupOperationTypeEnum.OR,
                                        Collections.emptyList(),
                                        Collections.emptyList()

                                )))
                ),
                Collections.singletonList(new ReportJobFilterData(
                        ID,
                        FilterTypeEnum.DATE_VALUE,
                        FilterOperationTypeEnum.IS_EQUAL,
                        "*",
                        Collections.singletonList(new ReportJobTupleData(
                                Collections.singletonList(
                                        new ReportJobTupleFieldData(ID,ID, NAME,DataTypeEnum.DATE,NAME))
                        ))
                )),
                Collections.emptyList()
        );
    }
}
