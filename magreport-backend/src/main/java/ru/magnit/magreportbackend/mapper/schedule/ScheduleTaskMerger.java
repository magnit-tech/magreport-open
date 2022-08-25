package ru.magnit.magreportbackend.mapper.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.excel.ExcelTemplate;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobStatusEnum;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTask;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTaskStatus;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTaskType;
import ru.magnit.magreportbackend.dto.request.schedule.ScheduleTaskAddRequest;
import ru.magnit.magreportbackend.mapper.Merger;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class ScheduleTaskMerger implements Merger<ScheduleTask, ScheduleTaskAddRequest> {

    private final DestinationEmailMapper mapperEmail;
    private final DestinationUserMapper mapperUser;
    private final DestinationRoleMapper mapperRole;


    @Override
    public ScheduleTask merge(ScheduleTask target, ScheduleTaskAddRequest source) {
        target
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
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setRenewalPeriod(source.getRenewalPeriod() == null ? 180 : source.getRenewalPeriod())
                .setSendEmptyReport(source.getSendEmptyReport())
                .setFailedStart(0L)
                .setMaxFailedStart(source.getMaxFailedStarts() == null ? 3 : source.getMaxFailedStarts())
                .setEmailDestinations(mapperEmail.from(source.getDestinationEmails()))
                .setUserDestinations(mapperUser.from(source.getDestinationUsers()))
                .setRoleDestinations(mapperRole.from(source.getDestinationRoles()))
                .setReportJobFilters(Collections.emptyList())
                .setScheduleList(Collections.emptyList());

        target.getEmailDestinations().forEach(destination -> destination.setScheduleTask(target));
        target.getUserDestinations().forEach(destination -> destination.setScheduleTask(target));
        target.getRoleDestinations().forEach(destination -> destination.setScheduleTask(target));

        return target;
    }
}
