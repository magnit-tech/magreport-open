package ru.magnit.magreportbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportFieldData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobData;
import ru.magnit.magreportbackend.dto.request.olap.CubeField;
import ru.magnit.magreportbackend.dto.request.olap.OlapCubeOutRequest;
import ru.magnit.magreportbackend.dto.request.olap.OlapCubeRequest;
import ru.magnit.magreportbackend.dto.request.olap.OlapFieldItemsOutRequest;
import ru.magnit.magreportbackend.dto.request.olap.OlapFieldItemsRequest;
import ru.magnit.magreportbackend.dto.response.olap.OlapCubeResponse;
import ru.magnit.magreportbackend.dto.response.olap.OlapFieldItemsResponse;
import ru.magnit.magreportbackend.service.domain.JobDomainService;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OlapOutService {

    @Value("${magreport.olap.out-service.host}")
    String host;

    @Value("${magreport.olap.out-service.get-cube-url}")
    String getCubeUrl;

    @Value("${magreport.olap.out-service.get-fields-value-url}")
    String getFieldsValueUrl;

    private final JobDomainService jobDomainService;
    private final RestTemplate restTemplate;


    public OlapCubeResponse getCube(OlapCubeRequest request) {
        var jobData = jobDomainService.getJobData(request.getJobId());
        var outRequest = new OlapCubeOutRequest();

        outRequest.setJobId(request.getJobId());
        outRequest.setCubeSize(jobData.rowCount());
        outRequest.setColumnFields(request.getColumnFields());
        outRequest.setRowFields(request.getRowFields());
        outRequest.setRowsInterval(request.getRowsInterval());
        outRequest.setColumnsInterval(request.getColumnsInterval());
        outRequest.setFilterGroup(request.getFilterGroup());
        outRequest.setMetrics(request.getMetrics());
        outRequest.setMetricPlacement(request.getMetricPlacement());
        outRequest.setFields(updateRequest(jobData));
        outRequest.setRowSort(request.getRowSort());
        outRequest.setColumnSort(request.getColumnSort());

        return restTemplate.postForObject(host + getCubeUrl, outRequest, OlapCubeResponse.class);
    }

    public OlapFieldItemsResponse getFieldValues(OlapFieldItemsRequest request)  {
        var jobData = jobDomainService.getJobData(request.getJobId());
        var outRequest = new OlapFieldItemsOutRequest();

        outRequest.setJobId(request.getJobId());
        outRequest.setCubeSize(jobData.rowCount());
        outRequest.setFilterGroup(request.getFilterGroup());
        outRequest.setFrom(request.getFrom());
        outRequest.setCount(request.getCount());
        outRequest.setFieldId(request.getFieldId());
        outRequest.setFields(updateRequest(jobData));

        return restTemplate.postForObject(host + getFieldsValueUrl, outRequest, OlapFieldItemsResponse.class);
    }

    private List<CubeField> updateRequest(ReportJobData jobData) {

        return jobData.reportData().fields()
                .stream()
                .filter(ReportFieldData::visible)
                .sorted(Comparator.comparingInt(ReportFieldData::ordinal))
                .map(f -> new CubeField(f.id(), f.dataType().name()))
                .toList();
    }
}
