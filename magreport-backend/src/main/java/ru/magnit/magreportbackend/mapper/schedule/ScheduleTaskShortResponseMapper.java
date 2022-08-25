package ru.magnit.magreportbackend.mapper.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTask;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTaskStatusEnum;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTaskTypeEnum;
import ru.magnit.magreportbackend.dto.response.schedule.ScheduleTaskShortResponse;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.auth.UserShortResponseMapper;
import ru.magnit.magreportbackend.mapper.report.ScheduleReportResponseMapper;

@Service
@RequiredArgsConstructor
public class ScheduleTaskShortResponseMapper implements Mapper<ScheduleTaskShortResponse, ScheduleTask> {

    private final UserShortResponseMapper userResponseMapper;
    private final ScheduleShortResponseMapper scheduleMapper;
    private final ScheduleReportResponseMapper reportMapper;
    private final DestinationEmailResponseMapper destinationEmailMapper;
    private final DestinationUserResponseMapper destinationUserMapper;
    private final DestinationRoleResponseMapper destinationRoleMapper;

    @Override
    public ScheduleTaskShortResponse from(ScheduleTask source) {
        return new ScheduleTaskShortResponse()
                .setId(source.getId())
                .setName(source.getName())
                .setDescription(source.getDescription())
                .setStatus(ScheduleTaskStatusEnum.getById(source.getStatus().getId()))
                .setExpirationDate(source.getExpirationDate())
                .setDestinationEmails(destinationEmailMapper.from(source.getEmailDestinations()))
                .setDestinationUsers(destinationUserMapper.from(source.getUserDestinations()))
                .setDestinationRoles(destinationRoleMapper.from(source.getRoleDestinations()))
                .setCode(source.getCode())
                .setRenewalPeriod(source.getRenewalPeriod())
                .setSendEmptyReport(source.getSendEmptyReport())
                .setSetMaxFailedStarts(source.getMaxFailedStart())
                .setReportBodyMail(source.getReportBodyMail())
                .setReportTitleMail(source.getReportTitleMail())
                .setErrorBodyMail(source.getErrorBodyMail())
                .setErrorTitleMail(source.getErrorTitleMail())
                .setTypeTask(ScheduleTaskTypeEnum.getById(source.getTaskType().getId()))
                .setUser(userResponseMapper.from(source.getUser()))
                .setSchedules(scheduleMapper.from(source.getScheduleList()))
                .setReport(reportMapper.from(source.getReport()))
                .setCreated(source.getCreatedDateTime())
                .setModified(source.getModifiedDateTime());
    }
}
