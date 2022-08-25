package ru.magnit.magreportbackend.mapper.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplate;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobStatusEnum;
import ru.magnit.magreportbackend.domain.schedule.Schedule;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTask;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTaskStatus;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTaskType;
import ru.magnit.magreportbackend.dto.request.schedule.ScheduleTaskAddRequest;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.reportjob.ReportJobFilterMapper;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleTaskMapper implements Mapper<ScheduleTask, ScheduleTaskAddRequest> {

    private final DestinationEmailMapper mapperEmail;
    private final DestinationUserMapper mapperUser;
    private final DestinationRoleMapper mapperRole;
    private final ReportJobFilterMapper filterMapper;


    @Override
    public ScheduleTask from(ScheduleTaskAddRequest source) {
        var response = new ScheduleTask()
                .setStatus(new ScheduleTaskStatus(ReportJobStatusEnum.SCHEDULED.getId()))
                .setExpirationDate(source.getExpirationDate())
                .setCode(source.getCode())
                .setReportBodyMail(source.getReportBodyMail())
                .setReportTitleMail(source.getReportTitleMail())
                .setErrorBodyMail(source.getErrorBodyMail())
                .setErrorTitleMail(source.getErrorTitleMail())
                .setExcelTemplate(new ExcelTemplate().setId(source.getExcelTemplateId()))
                .setReport(new Report(source.getReportId()))
                .setTaskType(new ScheduleTaskType(source.getTaskTypeId()))
                .setEmailDestinations(mapperEmail.from(source.getDestinationEmails()))
                .setUserDestinations(mapperUser.from(source.getDestinationUsers()))
                .setRoleDestinations(mapperRole.from(source.getDestinationRoles()))
                .setScheduleList(source.getSchedules().stream().map(s -> new Schedule(s.getId())).collect(Collectors.toList()))
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setRenewalPeriod(source.getRenewalPeriod() == null ? 180 : source.getRenewalPeriod())
                .setReportJobFilters(filterMapper.from(source.getReportJobFilter()))
                .setSendEmptyReport(source.getSendEmptyReport())
                .setFailedStart(0L)
                .setMaxFailedStart(source.getMaxFailedStarts() == null ? 3 : source.getMaxFailedStarts());

        response.getReportJobFilters().forEach(reportJobFilter -> reportJobFilter.setScheduleTask(response));

        response.getEmailDestinations().forEach(destination -> destination.setScheduleTask(response));
        response.getUserDestinations().forEach(destination -> destination.setScheduleTask(response));
        response.getRoleDestinations().forEach(destination -> destination.setScheduleTask(response));

        return response;
    }
}
