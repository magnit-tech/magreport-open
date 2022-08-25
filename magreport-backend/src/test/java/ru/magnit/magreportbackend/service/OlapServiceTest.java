package ru.magnit.magreportbackend.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.dataset.DataTypeEnum;
import ru.magnit.magreportbackend.domain.olap.AggregationType;
import ru.magnit.magreportbackend.dto.inner.olap.CubeData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportFieldData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobData;
import ru.magnit.magreportbackend.dto.request.olap.Interval;
import ru.magnit.magreportbackend.dto.request.olap.MetricDefinition;
import ru.magnit.magreportbackend.dto.request.olap.MetricFilterGroup;
import ru.magnit.magreportbackend.dto.request.olap.OlapCubeRequest;
import ru.magnit.magreportbackend.dto.request.olap.OlapFieldItemsRequest;
import ru.magnit.magreportbackend.service.domain.JobDomainService;
import ru.magnit.magreportbackend.service.domain.OlapDomainService;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OlapServiceTest {

    @InjectMocks
    private OlapService service;

    @Mock
    private OlapDomainService domainService;

    @Mock
    private JobDomainService jobDomainService;

    @Test
    void getCube() throws Exception {
        when(jobDomainService.getJobData(anyLong())).thenReturn(getTestJobData());
        when(domainService.getCubeData(any())).thenReturn(getTestCubeData());
        when(domainService.filterCubeData(any(), any())).thenReturn(getTrueStatusRows());

        final var result1 = service.getCube(getOlapRequest());
        assertNotNull(result1);
        assertEquals("2021-11-03", result1.getColumnValues().get(1).get(0));
        assertEquals(5, result1.getRowValues().size());
        assertEquals(2, result1.getColumnValues().size());
        assertEquals(6, result1.getMetricValues().size());
        assertEquals("30", result1.getMetricValues().get(0).getValues().get(1).get(1));
        assertEquals("10", result1.getMetricValues().get(1).getValues().get(1).get(1));
        assertEquals("10", result1.getMetricValues().get(2).getValues().get(1).get(1));
        assertEquals("10", result1.getMetricValues().get(3).getValues().get(1).get(1));
        assertEquals("3", result1.getMetricValues().get(4).getValues().get(1).get(1));
        assertEquals("1", result1.getMetricValues().get(5).getValues().get(1).get(1));
    }

    @Test
    void getFieldValues() {

        when(jobDomainService.getJobData(anyLong())).thenReturn(getTestJobData());
        when(domainService.getCubeData(any())).thenReturn(getTestCubeData());
        when(domainService.filterCubeData(any(), any())).thenReturn(getTrueStatusRows());

        var result = service.getFieldValues(getOlapFieldItemsRequest());

        assertEquals(5, result.getCountValues());
        assertEquals(5, result.getValueList().length);

        verify(jobDomainService).getJobData(anyLong());
        verify(domainService).getCubeData(any());
        verify(domainService).filterCubeData(any(), any());
        verifyNoMoreInteractions(jobDomainService, domainService);

        Mockito.reset(jobDomainService, domainService);

        when(jobDomainService.getJobData(anyLong())).thenReturn(getTestJobData());
        when(domainService.getCubeData(any())).thenReturn(getTestCubeData());
        when(domainService.filterCubeData(any(), any())).thenReturn(getFalseStatusRows());

        result = service.getFieldValues(getOlapFieldItemsRequest());

        assertEquals(0, result.getCountValues());
        assertEquals(0, result.getValueList().length);

        verify(jobDomainService).getJobData(anyLong());
        verify(domainService).getCubeData(any());
        verify(domainService).filterCubeData(any(), any());
        verifyNoMoreInteractions(jobDomainService, domainService);

        Mockito.reset(jobDomainService, domainService);

        when(jobDomainService.getJobData(anyLong())).thenReturn(getTestJobData());
        when(domainService.getCubeData(any())).thenReturn(getTestCubeData());
        when(domainService.filterCubeData(any(), any())).thenReturn(getMixedStatusRows());

        result = service.getFieldValues(getOlapFieldItemsRequest());

        assertEquals(3, result.getCountValues());
        assertEquals(3, result.getValueList().length);

        verify(jobDomainService).getJobData(anyLong());
        verify(domainService).getCubeData(any());
        verify(domainService).filterCubeData(any(), any());
        verifyNoMoreInteractions(jobDomainService, domainService);

    }

    private OlapCubeRequest getOlapRequest() {
        return new OlapCubeRequest()
                .setJobId(1L)
                .setColumnFields(new LinkedHashSet<>(List.of(0L)))
                .setRowFields(new LinkedHashSet<>(List.of(2L)))
                .setMetrics(List.of(
                        new MetricDefinition(6L, AggregationType.SUM),
                        new MetricDefinition(6L, AggregationType.MIN),
                        new MetricDefinition(6L, AggregationType.MAX),
                        new MetricDefinition(6L, AggregationType.AVG),
                        new MetricDefinition(6L, AggregationType.COUNT),
                        new MetricDefinition(6L, AggregationType.COUNT_DISTINCT)))
                .setMetricFilterGroup(new MetricFilterGroup())
                .setColumnsInterval(new Interval(0, 10))
                .setRowsInterval(new Interval(0, 10));
    }

    private CubeData getTestCubeData() {
        final var dataArray = transposeArray(getDataArray());

        return new CubeData(
                getTestReportData(),
                dataArray[0].length,
                Map.ofEntries(
                        Map.entry(0L, 0),
                        Map.entry(2L, 1),
                        Map.entry(3L, 2),
                        Map.entry(4L, 3),
                        Map.entry(5L, 4),
                        Map.entry(6L, 5),
                        Map.entry(7L, 6)),
                dataArray
        );
    }

    private String[][] transposeArray(String[][] sourceArray) {
        final var result = new String[sourceArray[0].length][sourceArray.length];

        for (int i = 0; i < sourceArray.length; i++) {
            for (int j = 0; j < sourceArray[0].length; j++) {
                result[j][i] = sourceArray[i][j];
            }
        }

        return result;
    }

    private String[][] getDataArray() {
        return new String[][]{
                new String[]{null, null, null, null, null, null, null},
                new String[]{"2021-11-03", "Russia", "Store 1", "Product type 1", "Product 1", "10", "5.5"},
                new String[]{"2021-11-03", "Russia", "Store 1", "Product type 2", "Product 2", "15", "4"},
                new String[]{"2021-11-03", "Russia", "Store 2", "Product type 1", "Product 1", "10", "5.5"},
                new String[]{"2021-11-03", "Germany", "Store 3", "Product type 2", "Product 2", "10", "5.5"},
                new String[]{"2021-11-03", "Germany", "Store 3", "Product type 3", "Product 3", "10", "5.5"},
                new String[]{"2021-11-03", "Germany", "Store 3", "Product type 4", "Product 4", "10", "5.5"},
                new String[]{"2021-11-03", "Turkey", "Store 4", "Product type 3", "Product 5", "10", "5.5"},
                new String[]{"2021-11-03", "Turkey", "Store 5", "Product type 3", "Product 6", "10", "5.5"},
                new String[]{"2021-11-03", "Turkey", "Store 4", "Product type 5", "Product 7", "10", "5.5"},
                new String[]{"2021-11-03", "Ukraine", "Store 6", "Product type 1", "Product 1", "10", "5.5"},
                new String[]{"2021-11-03", "Ukraine", "Store 7", "Product type 2", "Product 2", "10", "5.5"},
                new String[]{"2021-11-03", "Ukraine", "Store 8", "Product type 2", "Product 2", "10", "5.5"},
        };
    }

    private List<ReportFieldData> getReportFields() {
        return List.of(
                new ReportFieldData(0, 1, true, DataTypeEnum.DATE, "OPER_DATE", "Date of operation", ""),
                new ReportFieldData(1, 2, false, DataTypeEnum.STRING, "HIDDEN_FIELD", "Hidden field", ""),
                new ReportFieldData(2, 3, true, DataTypeEnum.STRING, "REGION_NAME", "Region name", ""),
                new ReportFieldData(3, 4, true, DataTypeEnum.STRING, "STORE_NAME", "Store name", ""),
                new ReportFieldData(4, 5, true, DataTypeEnum.STRING, "PRODUCT_TYPE", "Product type", ""),
                new ReportFieldData(5, 6, true, DataTypeEnum.DATE, "PRODUCT_NAME", "Product name", ""),
                new ReportFieldData(6, 7, true, DataTypeEnum.INTEGER, "SALE_QTY", "Quantity of sold products", ""),
                new ReportFieldData(7, 8, true, DataTypeEnum.DOUBLE, "SALE_SUM", "Sum of sold products", "")
        );
    }

    private ReportData getTestReportData() {
        return new ReportData(
                1,
                "TestReport",
                "TestReport",
                "TEST",
                "CUBE_TABLE",
                getReportFields(),
                null
        );
    }

    private ReportJobData getTestJobData() {
        return new ReportJobData(
                1,
                1,
                1,
                1,
                1,
                "TestUser",
                1,
                1,
                10,
                true,
                null,
                getTestReportData(),
                Collections.emptyList(),
                Collections.emptyList()
        );
    }

    private OlapFieldItemsRequest getOlapFieldItemsRequest() {
        return new OlapFieldItemsRequest()
                .setJobId(1L)
                .setFieldId(2L)
                .setFrom(0L)
                .setCount(10L);
    }

    private boolean[] getTrueStatusRows() {
        var result = new boolean[getDataArray().length];
        Arrays.fill(result, true);
        return result;
    }

    private boolean[] getFalseStatusRows() {
        return new boolean[getDataArray().length];
    }

    private boolean[] getMixedStatusRows() {
        var result = new boolean[getDataArray().length];

        for (int i = 0; i < result.length; i++)
            if (i < result.length / 2)
                result[i] = true;

        return result;
    }

}