package ru.magnit.magreportbackend.service.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTask;
import ru.magnit.magreportbackend.dto.request.reportjob.ReportJobFilterRequest;
import ru.magnit.magreportbackend.mapper.reportjob.ReportJobFilterMapper;
import ru.magnit.magreportbackend.repository.ReportJobFilterRepository;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportJobFilterDomainService {

    private final ReportJobFilterRepository repository;
    private final ReportJobFilterMapper mapper;

    @Transactional
    public void deleteReportJobFilterByScheduleTaskId(Long scheduleTaskId) {
        repository.deleteByScheduleTaskId(scheduleTaskId);
    }

    @Transactional
   public  void addReportJobFilterByScheduleTaskId (ReportJobFilterRequest request, Long taskId){

        var filter = mapper.from(request);

        filter.setScheduleTask(new ScheduleTask().setId(taskId));
        repository.save(filter);
    }

}
