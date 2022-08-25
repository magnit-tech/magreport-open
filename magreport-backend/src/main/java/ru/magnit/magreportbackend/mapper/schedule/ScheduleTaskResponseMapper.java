package ru.magnit.magreportbackend.mapper.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTask;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTaskStatusEnum;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTaskTypeEnum;
import ru.magnit.magreportbackend.dto.response.schedule.ScheduleTaskResponse;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.auth.UserShortResponseMapper;
import ru.magnit.magreportbackend.mapper.exceltemplate.ExcelTemplateResponseMapper;
import ru.magnit.magreportbackend.mapper.report.ReportResponseMapper;
import ru.magnit.magreportbackend.mapper.reportjob.ReportJobFilterResponseMapper;

@Service
@RequiredArgsConstructor
public class ScheduleTaskResponseMapper implements Mapper<ScheduleTaskResponse, ScheduleTask> {

    private final ExcelTemplateResponseMapper excelTemplateMapper;
    private final UserShortResponseMapper userResponseMapper;
    private final ScheduleShortResponseMapper scheduleMapper;
    private final ReportResponseMapper reportMapper;
    private final ReportJobFilterResponseMapper reportJobFilterMapper;
    private final DestinationEmailResponseMapper destinationEmailMapper;
    private final DestinationUserResponseMapper destinationUserMapper;
    private final DestinationRoleResponseMapper destinationRoleMapper;

    @Override
    public ScheduleTaskResponse from(ScheduleTask source) {
        return new ScheduleTaskResponse()
                .setId(source.getId())
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setStatus(ScheduleTaskStatusEnum.getById(source.getStatus().getId()))
                .setExpirationDate(source.getExpirationDate())
                .setExpirationCode(source.getExpirationCode())
                .setDestinationEmails(destinationEmailMapper.from(source.getEmailDestinations()))
                .setDestinationUsers(destinationUserMapper.from(source.getUserDestinations()))
                .setDestinationRoles(destinationRoleMapper.from(source.getRoleDestinations()))
                .setCode(source.getCode())
                .setRenewalPeriod(source.getRenewalPeriod())
                .setSendEmptyReport(source.getSendEmptyReport())
                .setMaxFailedStarts(source.getMaxFailedStart())
                .setFailedStart(source.getFailedStart())
                .setReportBodyMail(source.getReportBodyMail())
                .setReportTitleMail(source.getReportTitleMail())
                .setErrorBodyMail(source.getErrorBodyMail())
                .setErrorTitleMail(source.getErrorTitleMail())
                .setExcelTemplate(excelTemplateMapper.from(source.getExcelTemplate()))
                .setReport(reportMapper.from(source.getReport()))
                .setTypeTask(ScheduleTaskTypeEnum.getById(source.getTaskType().getId()))
                .setUser(userResponseMapper.from(source.getUser()))
                .setSchedules(scheduleMapper.from(source.getScheduleList()))
                .setReportJobFilters(reportJobFilterMapper.from(source.getReportJobFilters()));
    }
}
