package ru.magnit.magreportbackend.service.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.dataset.DataTypeEnum;
import ru.magnit.magreportbackend.domain.filterreport.GroupOperationTypeEnum;
import ru.magnit.magreportbackend.domain.olap.FilterType;
import ru.magnit.magreportbackend.dto.inner.olap.CubeData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportFieldData;
import ru.magnit.magreportbackend.dto.request.olap.FilterDefinition;
import ru.magnit.magreportbackend.dto.request.olap.FilterGroup;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@ExtendWith(MockitoExtension.class)
class OlapDomainServiceTest {

    @InjectMocks
    private OlapDomainService service;

    @Test
    void testSingleInListFilter() {

        final var result = service.filterCubeData(getTestCubeData(), getFilter());
        assertTrue(result[2]);

        long trueCount = getTrueCount(result);
        assertEquals(1, trueCount);

        long falseCount = getFalseCount(result);
        assertEquals(12, falseCount);
    }

    @Test
    void testFilterGroups() {

        final var result = service.filterCubeData(getTestCubeData(), getFilterGroup());
        assertTrue(result[2]);

        long trueCount = getTrueCount(result);
        assertEquals(1, trueCount);

        long falseCount = getFalseCount(result);
        assertEquals(12, falseCount);
    }


    private long getFalseCount(boolean[] result) {
        return IntStream
                .range(0, result.length)
                .mapToObj(i -> result[i])
                .filter(v -> !v)
                .count();
    }

    private long getTrueCount(boolean[] result) {
        return IntStream
                .range(0, result.length)
                .mapToObj(i -> result[i])
                .filter(v -> v)
                .count();
    }

    private FilterGroup getFilter() {
        return new FilterGroup(
                GroupOperationTypeEnum.AND,
                false,
                null,
                List.of(new FilterDefinition(
                        6L,
                        FilterType.IN_LIST,
                        false,
                        List.of("15")
                ))
        );
    }

    private FilterGroup getFilterGroup() {
        return new FilterGroup(
                GroupOperationTypeEnum.AND,
                false,
                List.of(
                        new FilterGroup(
                                GroupOperationTypeEnum.AND,
                                true,
                                null,
                                List.of(new FilterDefinition(
                                        6L,
                                        FilterType.IN_LIST,
                                        true,
                                        List.of("15")
                                ))),
                        new FilterGroup(
                                GroupOperationTypeEnum.AND,
                                false,
                                null,
                                List.of(new FilterDefinition(
                                        6L,
                                        FilterType.IN_LIST,
                                        true,
                                        List.of("10")
                                )))
                ),
                null
        );
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
                new ReportFieldData(6, 7, true, DataTypeEnum.DATE, "SALE_QTY", "Quantity of sold products", ""),
                new ReportFieldData(7, 8, true, DataTypeEnum.DATE, "SALE_SUM", "Sum of sold products", "")
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
}
