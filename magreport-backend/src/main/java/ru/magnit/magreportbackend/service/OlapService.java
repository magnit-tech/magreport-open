package ru.magnit.magreportbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataTypeEnum;
import ru.magnit.magreportbackend.domain.olap.AggregationType;
import ru.magnit.magreportbackend.domain.olap.SortDirection;
import ru.magnit.magreportbackend.domain.olap.SortingOrder;
import ru.magnit.magreportbackend.dto.inner.olap.CubeData;
import ru.magnit.magreportbackend.dto.inner.olap.MeasureData;
import ru.magnit.magreportbackend.dto.inner.olap.MetricResult;
import ru.magnit.magreportbackend.dto.inner.olap.Sorting;
import ru.magnit.magreportbackend.dto.request.olap.Interval;
import ru.magnit.magreportbackend.dto.request.olap.MetricDefinition;
import ru.magnit.magreportbackend.dto.request.olap.OlapConfigRequest;
import ru.magnit.magreportbackend.dto.request.olap.OlapCubeRequest;
import ru.magnit.magreportbackend.dto.request.olap.OlapFieldItemsRequest;
import ru.magnit.magreportbackend.dto.request.olap.ReportOlapConfigAddRequest;
import ru.magnit.magreportbackend.dto.request.olap.ReportOlapConfigRequest;
import ru.magnit.magreportbackend.dto.request.olap.ReportOlapConfigSetShareRequest;
import ru.magnit.magreportbackend.dto.request.olap.SortingParams;
import ru.magnit.magreportbackend.dto.request.olap.UsersReceivedMyJobsRequest;
import ru.magnit.magreportbackend.dto.request.reportjob.ReportJobRequest;
import ru.magnit.magreportbackend.dto.response.olap.OlapAvailableConfigurationsResponse;
import ru.magnit.magreportbackend.dto.response.olap.OlapConfigResponse;
import ru.magnit.magreportbackend.dto.response.olap.OlapCubeResponse;
import ru.magnit.magreportbackend.dto.response.olap.OlapFieldItemsResponse;
import ru.magnit.magreportbackend.dto.response.olap.OlapMetricResponse;
import ru.magnit.magreportbackend.dto.response.olap.OlapMetricResponse2;
import ru.magnit.magreportbackend.dto.response.olap.ReportOlapConfigResponse;
import ru.magnit.magreportbackend.dto.response.user.UserShortInfoResponse;
import ru.magnit.magreportbackend.metrics_function.MetricsFunction;
import ru.magnit.magreportbackend.service.domain.JobDomainService;
import ru.magnit.magreportbackend.service.domain.OlapConfigurationDomainService;
import ru.magnit.magreportbackend.service.domain.OlapDomainService;
import ru.magnit.magreportbackend.service.domain.UserDomainService;
import ru.magnit.magreportbackend.util.Comparators;
import ru.magnit.magreportbackend.util.Extensions;
import ru.magnit.magreportbackend.util.Pair;
import ru.magnit.magreportbackend.util.Triple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OlapService {

    private static final int DEFAULT_MEASURE_TUPLES_COUNT = 1000;

    private final OlapDomainService olapDomainService;
    private final JobDomainService jobDomainService;
    private final UserDomainService userDomainService;
    private final OlapConfigurationDomainService olapConfigurationDomainService;


    public OlapCubeResponse getCube(OlapCubeRequest request) {

        jobDomainService.checkAccessForJob(request.getJobId());

        log.debug("Start processing cube");
        var startTime = System.currentTimeMillis();
        final var jobData = jobDomainService.getJobData(request.getJobId());
        var endTime = System.currentTimeMillis() - startTime;
        log.debug("Job data acquired: " + endTime);

        startTime = System.currentTimeMillis();
        final var sourceCube = olapDomainService.getCubeData(jobData);
        endTime = System.currentTimeMillis() - startTime;
        log.debug("Report data acquired: " + endTime);

        startTime = System.currentTimeMillis();
        final var checkedFilterRows = olapDomainService.filterCubeData(sourceCube, request.getFilterGroup());
        endTime = System.currentTimeMillis() - startTime;
        log.debug("Cube filtered: " + endTime);

        startTime = System.currentTimeMillis();
        var measures = getRequestedMeasures(sourceCube, request, checkedFilterRows);
        endTime = System.currentTimeMillis() - startTime;
        log.debug("Measures acquired: " + endTime);

        startTime = System.currentTimeMillis();
        var metricValues = calculateMetricsValues(measures, request, sourceCube, checkedFilterRows);
        endTime = System.currentTimeMillis() - startTime;
        log.debug("Metrics calculation: " + endTime);

        startTime = System.currentTimeMillis();
        var metricResults = collectMetricResult(metricValues, request.getMetrics());
        endTime = System.currentTimeMillis() - startTime;
        log.debug("Metrics collection: " + endTime);

        startTime = System.currentTimeMillis();
        var sortedMetrics = sortedResults(request, sourceCube, metricResults, measures);
        endTime = System.currentTimeMillis() - startTime;
        log.debug("Metrics sorting: " + endTime);

        startTime = System.currentTimeMillis();
        var sortedMeasures = getResultMeasures(sortedMetrics, measures);
        endTime = System.currentTimeMillis() - startTime;
        log.debug("Get result measures: " + endTime);


        if (request.getMetricFilterGroup() != null && (!request.getMetricFilterGroup().getFilters().isEmpty() || !request.getMetricFilterGroup().getChildGroups().isEmpty())) {
            startTime = System.currentTimeMillis();
            var metricResultFilters = olapDomainService.filterMetricResult(metricResults, request.getMetricFilterGroup());
            endTime = System.currentTimeMillis() - startTime;
            log.debug("Metrics filtered: " + endTime);

            return getOlapCubeFilterMetricResponse(request, sortedMetrics, metricResultFilters, sortedMeasures, sourceCube);

        } else {

            return new OlapCubeResponse()
                    .setColumnValues(sortedMeasures.getL())
                    .setRowValues(sortedMeasures.getR())
                    .setMetricValues(getOlapMetricResponse(request, sortedMetrics, sourceCube))
                    .setTotalColumns(measures.getL().totalCount())
                    .setTotalRows(measures.getR().totalCount());
        }
    }

    public OlapFieldItemsResponse getFieldValues(OlapFieldItemsRequest request) {

        log.debug("Start processing cube");
        var startTime = System.currentTimeMillis();
        final var jobData = jobDomainService.getJobData(request.getJobId());
        var endTime = System.currentTimeMillis() - startTime;
        log.debug("Job data acquired: " + endTime);

        startTime = System.currentTimeMillis();
        final var sourceCube = olapDomainService.getCubeData(jobData);
        endTime = System.currentTimeMillis() - startTime;
        log.debug("Report data acquired: " + endTime);

        startTime = System.currentTimeMillis();
        final var checkedFilterRows = olapDomainService.filterCubeData(sourceCube, request.getFilterGroup());
        endTime = System.currentTimeMillis() - startTime;
        log.debug("Cube filtered: " + endTime);

        return new OlapFieldItemsResponse(getFieldValues(sourceCube, request, checkedFilterRows));
    }

    public ReportOlapConfigResponse addOlapReportConfig(ReportOlapConfigAddRequest request) {
        var currentUser = userDomainService.getCurrentUser();
        var id = olapConfigurationDomainService.updateReportOlapConfiguration(request, currentUser.getId());
        return olapConfigurationDomainService.getReportOlapConfiguration(id);
    }

    public OlapConfigResponse getOlapConfig(OlapConfigRequest request) {
        return olapConfigurationDomainService.getOlapConfiguration(request.getOlapConfigId());
    }

    public ReportOlapConfigResponse getOlapReportConfig(ReportOlapConfigRequest request) {
        return olapConfigurationDomainService.getReportOlapConfiguration(request.getReportOlapConfigId());
    }

    public void deleteOlapConfig(OlapConfigRequest request) {
        var currentUser = userDomainService.getCurrentUser();
        olapConfigurationDomainService.deleteOlapConfiguration(request.getOlapConfigId(), currentUser.getId());
    }

    public void deleteOlapReportConfig(ReportOlapConfigRequest request) {
        var currentUser = userDomainService.getCurrentUser();
        olapConfigurationDomainService.deleteReportOlapConfiguration(request.getReportOlapConfigId(), currentUser.getId());
    }

    public List<UserShortInfoResponse> getUsersReceivedMyJobs(UsersReceivedMyJobsRequest request) {
        var currentUser = userDomainService.getCurrentUser();
        return olapConfigurationDomainService.getListUsersReceivedAuthorJob(request, currentUser.getId());
    }

    public void setDefaultReportConfiguration(ReportOlapConfigRequest request) {
        olapConfigurationDomainService.setDefaultReportConfiguration(request.getReportOlapConfigId());
    }

    public void setSharedStatusReportConfiguration(ReportOlapConfigSetShareRequest request) {
        var currentUser = userDomainService.getCurrentUser();
        olapConfigurationDomainService.updateSharedStatusOlapReportConfig(request, currentUser.getId());
    }

    public OlapAvailableConfigurationsResponse getAvailableConfigurationsForJob(ReportJobRequest request) {
        var job = jobDomainService.getJob(request.getJobId());
        var currentUser = userDomainService.getCurrentUser();
        return olapConfigurationDomainService.getAvailableReportOlapConfigurationForJob(job.getId(), job.getReport().id(), currentUser.getId());
    }


    public ReportOlapConfigResponse getCurrentConfiguration(ReportJobRequest request) {
        var job = jobDomainService.getJob(request.getJobId());
        var currentUser = userDomainService.getCurrentUser();

        var currentConfigId = olapConfigurationDomainService.getCurrentConfiguration(job.getId(), job.getReport().id(), currentUser.getId());

        return olapConfigurationDomainService.getReportOlapConfiguration(currentConfigId);
    }

    private Pair<MeasureData, MeasureData> getRequestedMeasures(CubeData cubeData, OlapCubeRequest request, boolean[] checkedFilterRows) {
        final var columnTuples = new HashSet<List<String>>(DEFAULT_MEASURE_TUPLES_COUNT);
        final var rowTuples = new HashSet<List<String>>(DEFAULT_MEASURE_TUPLES_COUNT);

        final var columnFieldsIndexes = request.getColumnFields().stream().map(fieldId -> cubeData.fieldIndexes().get(fieldId)).toList();
        final var rowFieldsIndexes = request.getRowFields().stream().map(fieldId -> cubeData.fieldIndexes().get(fieldId)).toList();

        var startTime = System.currentTimeMillis();
        for (int i = 0; i < cubeData.numRows(); i++) {
            if (checkedFilterRows[i]) {
                final var columnTuple = getTuple(columnFieldsIndexes, cubeData.data(), i);
                final var rowTuple = getTuple(rowFieldsIndexes, cubeData.data(), i);
                if (!columnTuple.isEmpty()) columnTuples.add(columnTuple);
                if (!rowTuple.isEmpty()) rowTuples.add(rowTuple);
            }
        }

        if (columnTuples.isEmpty()) columnTuples.add(Collections.emptyList());
        if (rowTuples.isEmpty()) rowTuples.add(Collections.emptyList());

        var endTime = System.currentTimeMillis() - startTime;
        log.debug("Measures tuples collected: " + endTime);

        startTime = System.currentTimeMillis();
        final Pair<Set<List<String>>, Set<List<String>>> result = new Pair<>(
                getSubSet(sortRowSet(columnTuples), request.getColumnsInterval()),
                getSubSet(sortRowSet(rowTuples), request.getRowsInterval()));
        endTime = System.currentTimeMillis() - startTime;
        log.debug("Measures tuples sorted and trimmed: " + endTime);

        return new Pair<>(new MeasureData(result.getL(), columnTuples.size()), new MeasureData(result.getR(), rowTuples.size()));
    }

    private Set<List<String>> sortRowSet(Set<List<String>> rowSet) {
        return rowSet.stream().sorted(Comparators.listOfStringsComparator())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Set<List<String>> getSubSet(Set<List<String>> columnValues, Interval columnsInterval) {
        return sortRowSet(columnValues).stream()
                .skip(columnsInterval.getFrom())
                .limit(columnsInterval.getCount())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private MetricsFunction[][][] calculateMetricsValues(
            Pair<MeasureData, MeasureData> measures,
            OlapCubeRequest request,
            CubeData reportCube,
            boolean[] checkedFilterRows) {
        final var result = new MetricsFunction[measures.getL().values().size()][measures.getR().values().size()][request.getMetrics().size()];
        final var columnIndices = indexSet(measures.getL().values());
        final var rowIndices = indexSet(measures.getR().values());
        final var columnFieldsIndexes = request.getColumnFields().stream().map(fieldId -> reportCube.fieldIndexes().get(fieldId)).toList();
        final var rowFieldsIndexes = request.getRowFields().stream().map(fieldId -> reportCube.fieldIndexes().get(fieldId)).toList();
        final var metricFieldsIndexes = request.getMetrics().stream().map(MetricDefinition::getFieldId).map(fieldId -> reportCube.fieldIndexes().get(fieldId)).toList();
        final var columnsTupleHolder = Extensions.<String>initList(request.getColumnFields().size());
        final var rowsTupleHolder = Extensions.<String>initList(request.getRowFields().size());

        if (columnIndices.isEmpty() && rowIndices.isEmpty()) return result;

        for (int cubeRow = 0; cubeRow < reportCube.numRows(); cubeRow++) {
            if (!checkedFilterRows[cubeRow]) continue;
            final var columnsTuple = getTuple(columnFieldsIndexes, reportCube.data(), cubeRow, columnsTupleHolder);
            final var rowsTuple = getTuple(rowFieldsIndexes, reportCube.data(), cubeRow, rowsTupleHolder);

            for (int metricNumber = 0; metricNumber < request.getMetrics().size(); metricNumber++) {
                final var columnNumber = columnIndices.getOrDefault(columnsTuple, -1);
                final var rowNumber = rowIndices.getOrDefault(rowsTuple, -1);
                if (columnNumber == -1 || rowNumber == -1) break;
                final var dataTypeMetric = reportCube.reportMetaData().getTypeField(request.getMetrics().get(metricNumber).getFieldId());
                if (result[columnNumber][rowNumber][metricNumber] == null) {
                    result[columnNumber][rowNumber][metricNumber] = request.getMetrics().get(metricNumber).getAggregationType().getMetricsFunction(dataTypeMetric);
                }
                final var metricColumn = metricFieldsIndexes.get(metricNumber);

                result[columnNumber][rowNumber][metricNumber].addValue(reportCube.data()[metricColumn][cubeRow]);
            }
        }

        return result;
    }

    private MetricResult[][][] collectMetricResult(MetricsFunction[][][] metrics, List<MetricDefinition> metricsDef) {

        MetricResult[][][] results = new MetricResult[metrics.length][metrics.length == 0 ? 0 : metrics[0].length][metricsDef.size()];

        for (int col = 0; col < metrics.length; col++)
            for (int row = 0; row < metrics[col].length; row++)
                for (int metric = 0; metric < metrics[col][row].length; metric++) {
                    var value = metrics[col][row][metric] == null ? "" : metrics[col][row][metric].calculate();
                    results[col][row][metric] = new MetricResult(value, col, row);
                }

        return results;
    }

    private List<OlapMetricResponse> getOlapMetricResponse(OlapCubeRequest request, MetricResult[][][] result, CubeData reportCube) {

        AtomicLong index = new AtomicLong(0);
        List<List<List<String>>> valuesMetrics = new ArrayList<>();

        for (int i = 0; i < request.getMetrics().size(); i++) {
            var values = new ArrayList<List<String>>();
            for (MetricResult[][] columns : result) {
                var valueRows = new ArrayList<String>();
                for (MetricResult[] rows : columns) {
                    var value = rows[i];
                    valueRows.add(value == null ? "" : value.getValue());
                }
                values.add(valueRows);
            }
            valuesMetrics.add(values);
        }

        var responses = new ArrayList<OlapMetricResponse>();
        request.getMetrics().forEach(metric -> {
            var response = new OlapMetricResponse()
                    .setFieldId(metric.getFieldId())
                    .setAggregationType(metric.getAggregationType())
                    .setDataType(metric.getAggregationType().getDataTypeMetricFunction(reportCube.reportMetaData().getTypeField(metric.getFieldId())))
                    .setValues(valuesMetrics.get((int) index.getAndIncrement()));
            responses.add(response);
        });

        return responses;
    }

    private OlapCubeResponse getOlapCubeFilterMetricResponse(
            OlapCubeRequest request, MetricResult[][][] metrics, Triple<boolean[][], boolean[], boolean[]> filterResult, Pair<List<List<String>>, List<List<String>>> measures, CubeData reportCube) {

        var result = new OlapMetricResponse2[request.getMetrics().size()];

        var columnRange = collectRange(filterResult.getB(), request.getColumnsInterval());
        var rowRange = collectRange(filterResult.getC(), request.getRowsInterval());

        for (int metric = 0; metric < request.getMetrics().size(); metric++) {
            result[metric] = new OlapMetricResponse2();
            result[metric].setValues(new String[columnRange.size()][rowRange.size()]);
            result[metric].setAggregationType(request.getMetrics().get(metric).getAggregationType());
            result[metric].setFieldId(request.getMetrics().get(metric).getFieldId());
        }

        var columns = new LinkedHashMap<Integer, List<String>>();
        var rows = new LinkedHashMap<Integer, List<String>>();

        final int totalColumns = countingItems(filterResult.getB());
        final int totalRows = countingItems(filterResult.getC());

        for (int colIndex = 0; colIndex < columnRange.size(); colIndex++)
            for (int rowIndex = 0; rowIndex < rowRange.size(); rowIndex++)
                for (int metric = 0; metric < request.getMetrics().size(); metric++) {

                    result[metric].getValues()[colIndex][rowIndex] = metrics[columnRange.get(colIndex)][rowRange.get(rowIndex)][metric].getValue();

                    if (!columns.containsKey(colIndex))
                        columns.put(columnRange.get(colIndex) - request.getColumnsInterval().getFrom(), measures.getL().get(metrics[columnRange.get(colIndex)][rowRange.get(rowIndex)][metric].getColumn()));

                    if (!rows.containsKey(rowIndex))
                        rows.put(rowRange.get(rowIndex) - request.getRowsInterval().getFrom(), measures.getR().get(metrics[columnRange.get(colIndex)][rowRange.get(rowIndex)][metric].getRow()));

                }


        return new OlapCubeResponse()
                .setTotalColumns(totalColumns)
                .setTotalRows(totalRows)
                .setColumnValues(columns.values().stream().toList())
                .setRowValues(rows.values().stream().toList())
                .setMetricValues(
                        Arrays.stream(result)
                                .map(r -> {
                                    var response = new OlapMetricResponse();
                                    response.setFieldId(r.getFieldId())
                                            .setAggregationType(r.getAggregationType())
                                            .setDataType(r.getAggregationType().getDataTypeMetricFunction(reportCube.reportMetaData().getTypeField(r.getFieldId())))
                                            .setValues(Arrays.stream(r.getValues()).map(f -> Arrays.stream(f).toList()).toList());
                                    return response;
                                }).toList());
    }


    private List<String> getTuple(List<Integer> columnIndexes, String[][] cubeData, int rowNumber, List<String> tupleHolder) {
        for (int i = 0; i < tupleHolder.size(); i++) {
            tupleHolder.set(i, cubeData[columnIndexes.get(i)][rowNumber]);
        }
        return tupleHolder;
    }

    private List<String> getTuple(List<Integer> columnIndexes, String[][] cubeData, int rowNumber) {
        final var tupleHolder = new ArrayList<String>(columnIndexes.size());
        for (Integer columnIndex : columnIndexes) {
            tupleHolder.add(cubeData[columnIndex][rowNumber]);
        }
        return tupleHolder;
    }

    private Map<List<String>, Integer> indexSet(Set<List<String>> source) {
        final var result = new HashMap<List<String>, Integer>();
        final var counter = new AtomicInteger(0);

        source.forEach(element -> result.put(element, counter.getAndIncrement()));

        return result;
    }

    private Pair<String[], Long> getFieldValues(CubeData reportCube, OlapFieldItemsRequest request, boolean[] checkedFilterRows) {

        int indexField = reportCube.fieldIndexes().get(request.getFieldId());
        HashSet<String> values = new HashSet<>();

        for (int i = 0; i < reportCube.numRows(); i++) {
            if (checkedFilterRows[i])
                values.add(reportCube.data()[indexField][i]);
        }

        String[] sortedValues = values.toArray(new String[0]);
        Arrays.sort(sortedValues, Comparator.nullsFirst(Comparator.naturalOrder()));

        if (request.getFrom() >= sortedValues.length) {
            return new Pair<>(new String[0], (long) values.size());
        } else {
            if (request.getEndPoint() >= sortedValues.length)
                return new Pair<>(Arrays.copyOfRange(sortedValues, request.getFrom().intValue(), sortedValues.length), (long) values.size());
            else
                return new Pair<>(Arrays.copyOfRange(sortedValues, request.getFrom().intValue(), request.getEndPoint()), (long) values.size());
        }
    }

    private MetricResult[][][] sortedResults(OlapCubeRequest request, CubeData reportCube, MetricResult[][][] result, Pair<MeasureData, MeasureData> measures) {

        var metricTypes =
                request
                        .getMetrics()
                        .stream()
                        .map(metric -> {
                            if (metric.getAggregationType().equals(AggregationType.COUNT) || metric.getAggregationType().equals(AggregationType.COUNT_DISTINCT))
                                return DataTypeEnum.INTEGER;
                            else
                                return reportCube.reportMetaData().getTypeField(metric.getFieldId());
                        })
                        .toList();

        if (!request.getColumnSort().isEmpty()) {
            sortMetrics(result, metricTypes, SortDirection.Column, getSorting(measures.getR(), request.getColumnSort()));
        }

        if (!request.getRowSort().isEmpty()) {

            result = transposeResults(result, request.getMetrics().size());
            sortMetrics(result, metricTypes, SortDirection.Row, getSorting(measures.getL(), request.getRowSort()));
            result = transposeResults(result, request.getMetrics().size());
        }


        return result;
    }

    private void sortMetrics(MetricResult[][][] metrics, List<DataTypeEnum> dataTypes, SortDirection sortDirection, List<Sorting> sortings) {

        if (metrics.length > 0) {
            var rowIndex = new int[sortings.size()][metrics[0].length];

            for (int sortingIndex = 0; sortingIndex < sortings.size(); sortingIndex++)
                for (int i = 0; i < metrics[0].length; i++) {
                    var row = switch (sortDirection) {
                        case Column -> metrics[0][i][0].getColumn();
                        case Row -> metrics[0][i][0].getRow();
                    };
                    rowIndex[sortingIndex][row] = i;
                }


            Arrays.sort(metrics, (row1, row2) -> {
                var result = 0;
                for (Sorting sorting : sortings) {
                    var value1 = row1[rowIndex[sortings.indexOf(sorting)][sorting.getTupleIndex()]][sorting.getMetricId()].getValue();
                    var value2 = row2[rowIndex[sortings.indexOf(sorting)][sorting.getTupleIndex()]][sorting.getMetricId()].getValue();
                    var type = dataTypes.get(sorting.getMetricId());
                    var compare = switch (type) {
                        case INTEGER -> Integer.valueOf(value1).compareTo(Integer.valueOf(value2));
                        case STRING, DATE, TIMESTAMP -> value1.compareTo(value2);
                        case DOUBLE -> Double.valueOf(value1).compareTo(Double.valueOf(value2));
                    };
                    if (compare != 0) return sorting.getOrder().equals(SortingOrder.Ascending) ? compare : compare * -1;
                }
                return result;
            });
        }
    }

    private MetricResult[][][] transposeResults(MetricResult[][][] metrics, int metricCount) {

        MetricResult[][][] result = new MetricResult[metrics[0].length][metrics.length][metricCount];

        for (int col = 0; col < metrics.length; col++)
            for (int row = 0; row < metrics[col].length; row++)
                result[row][col] = metrics[col][row].clone();
        return result;

    }

    private List<Sorting> getSorting(MeasureData measure, List<SortingParams> params) {

        return params.stream()
                .map(param -> new Sorting(
                        param.getOrder(),
                        measure.values().stream().toList().indexOf(param.getTuple()),
                        param.getMetricId()
                )).toList();
    }

    private Pair<List<List<String>>, List<List<String>>> getResultMeasures(MetricResult[][][] metrics, Pair<MeasureData, MeasureData> measures) {

        var defaultColumnValues = measures.getL().values().stream().toList();
        var defaultRowValues = measures.getR().values().stream().toList();

        var resultColumnValues = new ArrayList<List<String>>(defaultColumnValues.size());
        var resultRowValues = new ArrayList<List<String>>(defaultRowValues.size());

        if (metrics.length == 0) return new Pair<>(defaultColumnValues, defaultRowValues);
        if (metrics[0].length == 0) return new Pair<>(defaultColumnValues, defaultRowValues);
        if (metrics[0][0].length == 0) return new Pair<>(defaultColumnValues, defaultRowValues);


        for (int col = 0; col < metrics.length; col++) {
            var oldIndex = metrics[col][0][0].getColumn();
            resultColumnValues.add(col, defaultColumnValues.get(oldIndex));
        }


        for (int row = 0; row < metrics[0].length; row++) {
            var oldIndex = metrics[0][row][0].getRow();
            resultRowValues.add(row, defaultRowValues.get(oldIndex));
        }


        return new Pair<>(resultColumnValues, resultRowValues);

    }

    private List<Integer> collectRange(boolean[] filter, Interval interval) {

        var result = new ArrayList<Integer>();
        var skip = interval.getFrom();
        var take = interval.getCount();

        for (int i = 0; i < filter.length; i++) {
            if (!filter[i]) {
                if (skip > 0) {
                    skip -= 1;
                } else if (take > 0) {
                    result.add(i);
                    take -= 1;
                } else break;
            }
        }
        return result;
    }

    private int countingItems(boolean[] items) {
        int result = 0;
        for (boolean item : items) {
            if (!item) result++;
        }
        return result;
    }
}