package ru.magnit.magreportbackend.service.domain.asm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.domain.dataset.DataTypeEnum;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstance;
import ru.magnit.magreportbackend.domain.filterreport.FilterReport;
import ru.magnit.magreportbackend.domain.filterreport.FilterReportGroup;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterOperationTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTypeEnum;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.inner.datasource.DataSourceData;
import ru.magnit.magreportbackend.dto.inner.filter.FilterChildNodesRequestData;
import ru.magnit.magreportbackend.dto.inner.filter.FilterData;
import ru.magnit.magreportbackend.dto.inner.filter.FilterFieldData;
import ru.magnit.magreportbackend.dto.inner.filter.FilterValueListRequestData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportFilterGroupData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobFilterData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobTupleData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobTupleFieldData;
import ru.magnit.magreportbackend.dto.request.filterinstance.ChildNodesRequest;
import ru.magnit.magreportbackend.dto.request.filterinstance.ListValuesCheckRequest;
import ru.magnit.magreportbackend.dto.request.filterinstance.ListValuesRequest;
import ru.magnit.magreportbackend.dto.request.filterreport.FilterGroupAddRequest;
import ru.magnit.magreportbackend.dto.request.reportjob.ReportJobFilterRequest;
import ru.magnit.magreportbackend.dto.response.filterreport.FilterReportResponse;
import ru.magnit.magreportbackend.dto.tuple.Tuple;
import ru.magnit.magreportbackend.mapper.filter.FilterChildNodesRequestDataMerger;
import ru.magnit.magreportbackend.mapper.filterreport.FilterDataFRMapper;
import ru.magnit.magreportbackend.mapper.filterreport.FilterGroupResponseMapper;
import ru.magnit.magreportbackend.mapper.filterreport.FilterReportGroupMapper;
import ru.magnit.magreportbackend.mapper.filterreport.FilterReportQueryDataMerger;
import ru.magnit.magreportbackend.mapper.filterreport.FilterReportResponseMapper;
import ru.magnit.magreportbackend.repository.FilterReportGroupRepository;
import ru.magnit.magreportbackend.repository.FilterReportRepository;
import ru.magnit.magreportbackend.service.domain.FilterReportDomainService;
import ru.magnit.magreportbackend.service.domain.UserDomainService;
import ru.magnit.magreportbackend.service.jobengine.filter.FilterQueryExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilterReportDomainServiceTest {

    @InjectMocks
    private FilterReportDomainService service;

    @Mock
    private FilterReportGroupRepository filterGroupRepository;

    @Mock
    private FilterReportRepository filterReportRepository;

    @Mock
    private FilterQueryExecutor queryExecutor;

    @Mock
    private FilterReportGroupMapper filterReportGroupMapper;

    @Mock
    private FilterGroupResponseMapper responseMapper;

    @Mock
    private UserDomainService userDomainService;

    @Mock
    private FilterReportResponseMapper filterReportResponseMapper;

    @Mock
    private FilterReportQueryDataMerger filterReportQueryDataMerger;

    @Mock
    private FilterDataFRMapper filterDataFRMapper;

    @Mock
    private FilterChildNodesRequestDataMerger filterChildNodesRequestDataMerger;

    private static final Long ID = 1L;

    @Test
    void addFilters() {
        when(filterReportGroupMapper.from(any(FilterGroupAddRequest.class))).thenReturn(new FilterReportGroup());

        service.addFilters(new UserView().setId(1L), getFGARequest());

        verify(filterGroupRepository).deleteByReportId(anyLong());
        verify(filterGroupRepository).save(any());
        verifyNoMoreInteractions(filterGroupRepository);
        verify(filterReportGroupMapper).from(ArgumentMatchers.<FilterGroupAddRequest>any());
        verifyNoMoreInteractions(filterReportGroupMapper);
    }

    @Test
    void getFilters() {
        service.getFilters(1L);

        verify(filterGroupRepository).getByReportIdAndParentGroupIsNull(1L);
        verifyNoMoreInteractions(filterGroupRepository);
        verify(responseMapper).from(ArgumentMatchers.<FilterReportGroup>any());
        verifyNoMoreInteractions(responseMapper);
    }

    @Test
    void removeFilters() {

        service.removeFilters(ID);

        verify(filterGroupRepository).deleteByReportId(anyLong());
        verifyNoMoreInteractions(filterGroupRepository);
    }

    @Test
    void getFilterReportValues() {

        when(filterReportRepository.getReferenceById(anyLong())).thenReturn(getFilterReport());
        when(filterReportQueryDataMerger.merge(any(), any())).thenReturn(getFilterValueListRequestData());
        when(queryExecutor.getFilterInstanceValuesQuery(any())).thenReturn(Collections.emptyList());
        when(filterReportResponseMapper.from(any(FilterReport.class))).thenReturn(new FilterReportResponse());

        assertNotNull(service.getFilterReportValues(getListValuesRequest()));

        verify(filterReportRepository).getReferenceById(anyLong());
        verify(filterReportQueryDataMerger).merge(any(), any());
        verify(queryExecutor).getFilterInstanceValuesQuery(any());
        verifyNoMoreInteractions(filterReportRepository, filterReportQueryDataMerger, queryExecutor);

    }

    @Test
    void getChildNodes() {

        when(filterReportRepository.getReferenceById(anyLong())).thenReturn(getFilterReport());
        when(filterChildNodesRequestDataMerger.merge(any(FilterData.class), any(), anyList())).thenReturn(getFilterChildNodesRequestData());
        when(filterDataFRMapper.from(any(FilterReport.class))).thenReturn(getFilterData());

        assertNotNull(service.getChildNodes(new ChildNodesRequest().setFilterId(ID), Collections.emptyList()));

        verify(filterReportRepository).getReferenceById(anyLong());
        verifyNoMoreInteractions(filterReportRepository);
    }

    @Test
    @SuppressWarnings("unchecked")
    void decodeFieldsValues() {

        when(filterReportRepository.getReferenceById(anyLong())).thenReturn(getFilterReport());
        when(filterDataFRMapper.from(any(FilterReport.class))).thenReturn(getFilterData());
        when(queryExecutor.getFieldsValues(any())).thenReturn(Collections.emptyList(), Collections.singletonList(new Tuple()));

        service.decodeFieldsValues(getReportJobData());

        verify(filterReportRepository, times(2)).getReferenceById(anyLong());
        verify(filterDataFRMapper, times(2)).from(any(FilterReport.class));
        verify(queryExecutor, times(2)).getFieldsValues(any());
        verifyNoMoreInteractions(filterReportRepository, filterDataFRMapper, queryExecutor);

    }

    @Test
    void getFilterReportDataSetId() {

        when(filterReportRepository.getReferenceById(anyLong())).thenReturn(getFilterReport());

        assertNotNull(service.getFilterReportDataSetId(ID));

        verify(filterReportRepository).getReferenceById(anyLong());
        verifyNoMoreInteractions(filterReportRepository);
    }

    @Test
    void getCleanedValueListValues() {

        when(filterReportRepository.getReferenceById(anyLong())).thenReturn(getFilterReport());
        when(filterDataFRMapper.from(any(FilterReport.class))).thenReturn(getFilterData());
        when(queryExecutor.getFieldsValues(any())).thenReturn(Collections.emptyList());

        assertNotNull(service.getCleanedValueListValues(new ReportJobFilterRequest().setFilterId(ID).setParameters(Collections.emptyList())));

        verify(filterReportRepository).getReferenceById(anyLong());
        verify(filterDataFRMapper).from(any(FilterReport.class));
        verify(queryExecutor).getFieldsValues(any());
        verifyNoMoreInteractions(filterReportRepository, filterDataFRMapper, queryExecutor);
    }

    @Test
    void checkFilterReportForInstance() {

        when(filterReportRepository.findFilterReportByFilterInstanceId(anyLong())).thenReturn(Collections.emptyList());

        assertNotNull(service.checkFilterReportForInstance(ID));


        verify(filterReportRepository).findFilterReportByFilterInstanceId(anyLong());
        verifyNoMoreInteractions(filterReportRepository, filterReportResponseMapper);
    }

    @Test
    void checkFilterReportValues() {

        when(filterReportRepository.getReferenceById(anyLong())).thenReturn(getFilterReport());

        assertNotNull(service.checkFilterReportValues(getListValuesCheckRequest()));

        verify(filterReportRepository).getReferenceById(anyLong());
        verifyNoMoreInteractions(filterReportRepository);
    }

    @Test
    void unWindGroups() {
        final var filterReportGroup = new FilterReportGroup()
                .setChildGroups(Collections.singletonList(
                        new FilterReportGroup()
                            .setChildGroups(Collections.singletonList(new FilterReportGroup()))
                ));

        assertNotNull(ReflectionTestUtils.invokeMethod(service, "unWindGroups", filterReportGroup, new ArrayList<>()));
    }

    private FilterGroupAddRequest getFGARequest() {

        return new FilterGroupAddRequest().setReportId(1L);
    }

    private FilterReport getFilterReport() {
        return new FilterReport()
                .setFilterInstance(new FilterInstance().setDataSet(new DataSet().setId(ID)));
    }

    private FilterValueListRequestData getFilterValueListRequestData() {
        return new FilterValueListRequestData(
                null, null, null, null, null, null,
                null, null, null, null,
                null, false, null, 0L, null);
    }

    private ListValuesRequest getListValuesRequest() {
        var list = new ListValuesRequest();
        list.setFilterId(ID);
        return list;
    }

    private ListValuesCheckRequest getListValuesCheckRequest() {

        var list = new ListValuesCheckRequest();
        list.setFilterId(ID);
        return list;
    }

    private FilterChildNodesRequestData getFilterChildNodesRequestData() {
        return new FilterChildNodesRequestData(
                null, null, null, null, 0L, null,
                null, 1L, null, null);
    }

    private FilterData getFilterData() {
        return new FilterData(
                null, 0L, null, null, null, null,
                null, null,
                Arrays.asList(
                        new FilterFieldData(
                                ID, ID, "", "", "", DataTypeEnum.DATE, FilterFieldTypeEnum.CODE_FIELD),

                        new FilterFieldData(
                                ID, ID, "", "", "", DataTypeEnum.INTEGER, FilterFieldTypeEnum.ID_FIELD)
                )
        );
    }

    private ReportJobData getReportJobData() {
        return new ReportJobData(
                ID,
                ID,
                ID,
                ID,
                ID,
                "",
                ID,
                ID,
                ID,
                false,
                new DataSourceData(null, null, null, null, null, null),
                new ReportData(ID, "", "", "", "", Collections.emptyList(), new ReportFilterGroupData(
                        ID, ID, "", "", null, Collections.emptyList(), Collections.emptyList())),
                new ArrayList<>(Arrays.asList(
                        new ReportJobFilterData(ID, FilterTypeEnum.VALUE_LIST, FilterOperationTypeEnum.IS_BETWEEN, "*", Collections.singletonList(
                                new ReportJobTupleData(
                                        Collections.singletonList(
                                                new ReportJobTupleFieldData(ID, ID, "", DataTypeEnum.DATE, ""))))),
                        new ReportJobFilterData(ID, FilterTypeEnum.VALUE_LIST, FilterOperationTypeEnum.IS_LOWER, "*", new ArrayList<>(Collections.singletonList(
                                new ReportJobTupleData(
                                        Collections.singletonList(
                                                new ReportJobTupleFieldData(ID, ID, "", DataTypeEnum.INTEGER, "")))))),
                        new ReportJobFilterData(ID, FilterTypeEnum.DATE_RANGE, FilterOperationTypeEnum.IS_EQUAL, "*", new ArrayList<>(Collections.singletonList(
                                new ReportJobTupleData(
                                        Collections.singletonList(
                                                new ReportJobTupleFieldData(ID, ID, "", DataTypeEnum.DATE, "")))))))),
                Collections.emptyList());
    }

}
