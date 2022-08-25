package ru.magnit.magreportbackend.service.jobengine.schedule;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.magnit.magreportbackend.domain.reportjob.ReportJobUserTypeEnum;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTaskStatusEnum;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTaskTypeEnum;
import ru.magnit.magreportbackend.dto.request.reportjob.ExcelReportRequest;
import ru.magnit.magreportbackend.dto.request.schedule.ScheduleCalendarAddRequest;
import ru.magnit.magreportbackend.dto.request.schedule.ScheduleTaskRequest;
import ru.magnit.magreportbackend.dto.request.user.RoleRequest;
import ru.magnit.magreportbackend.dto.request.user.UserRequest;
import ru.magnit.magreportbackend.dto.response.reportjob.ReportJobResponse;
import ru.magnit.magreportbackend.dto.response.schedule.DestinationUserResponse;
import ru.magnit.magreportbackend.dto.response.schedule.ScheduleTaskResponse;
import ru.magnit.magreportbackend.dto.response.user.UserResponse;
import ru.magnit.magreportbackend.service.ReportJobService;
import ru.magnit.magreportbackend.service.RoleService;
import ru.magnit.magreportbackend.service.ScheduleService;
import ru.magnit.magreportbackend.service.UserService;
import ru.magnit.magreportbackend.service.domain.JobDomainService;
import ru.magnit.magreportbackend.service.domain.JobTokenDomainService;
import ru.magnit.magreportbackend.service.domain.MailTextDomainService;
import ru.magnit.magreportbackend.service.domain.ReportJobUserDomainService;
import ru.magnit.magreportbackend.service.jobengine.JobEngine;
import ru.magnit.magreportbackend.util.Pair;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static ru.magnit.magreportbackend.controller.ScheduleController.SCHEDULE_TASK_GET_EXCEL_REPORT;
import static ru.magnit.magreportbackend.controller.ScheduleController.SCHEDULE_TASK_PROLONGATION;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleEngine implements JobEngine, InitializingBean {

    private final ScheduleService scheduleService;
    private final UserService userService;
    private final RoleService roleService;
    private final ReportJobService reportJobService;
    private final JobDomainService jobDomainService;
    private final MailTextDomainService mailTextDomainService;
    private final JobTokenDomainService jobTokenDomainService;
    private final ReportJobUserDomainService reportJobUserDomainService;


    private final Map<Long, Long> jobRunners = new HashMap<>();
    private final Map<Long, Long> taskRunners = new HashMap<>();
    private int numJobRunners = Integer.MAX_VALUE;

    @Value("${magreport.schedule-mail-task-complete-excel}")
    private String scheduleMailCompleteExcel;

    @Value("${magreport.schedule-mail-task-complete-web}")
    private String scheduleMailCompleteWeb;

    @Value("${magreport.schedule-mail-task-expired}")
    private String scheduleMailExpired;

    @Value("${magreport.schedule-mail-task-inform-expired}")
    private String scheduleMailInformExpired;

    @Value("${magreport.schedule-mail-task-failed}")
    private String scheduleMailFailed;

    @Value("${magreport.schedule-mail-task-big-size-excel}")
    private String scheduleMailBigSizeExcel;

    @Value("${magreport.schedule-mail-task-deadLine-expires}")
    private String scheduleMailDeadlineExpires;

    @Value("${magreport.host}")
    private String host;

    @Value("${magreport.schedule-user}")
    private String scheduleUser;

    @Value("${magreport.schedule-send-warning-days}")
    private Long daysBeforeExpired;

    @Override
    @Scheduled(initialDelay = 5000, fixedDelay = 5000)
    public void processJobQueue() {

        checkStatusJob();
        scheduleService.getTaskForDate(LocalDateTime.now())
                .stream()
                .filter(task -> task.getStatus().getId().equals(ScheduleTaskStatusEnum.SCHEDULED.getId()))
                .filter(this::checkExpiredDate)
                .forEach(task ->
                        scheduleService.updateStatusScheduleTask(task.getId(), ScheduleTaskStatusEnum.RUNNING)
                );

        scheduleService.getTaskForStatus(ScheduleTaskStatusEnum.RUNNING).forEach(task -> {
            if (!jobRunners.containsValue(task.getId())) {
                var user = userService.getUserResponse(new UserRequest().setUserName(scheduleUser));
                var idJob = jobDomainService.addJob(task.getReport(), task.getReportJobFilters(), user.getId());
                jobRunners.put(idJob, task.getId());
                log.debug("Schedule task with id " + task.getId() + " has JobId: " + idJob);
            }
        });

        if (numJobRunners != jobRunners.size()) {
            numJobRunners = jobRunners.size();
            log.debug("Running tasks awaiting job completion :" + numJobRunners);
        }

        executeCompleteTask();

    }

    @Override
    public void afterPropertiesSet() {
        if (scheduleService.checkDatesInScheduleCalendar()) {
            scheduleService.addNewDateScheduleCalendar(new ScheduleCalendarAddRequest(180L));
        }
        scheduleService.refreshStatusesAfterRestart();
    }

    @Scheduled(cron = "${magreport.scheduleengine.check-last-date-calendar}")
    public void updateScheduleCalendar() {
        scheduleService.addNewDateScheduleCalendar(new ScheduleCalendarAddRequest(31L));
    }


    private boolean checkExpiredDate(ScheduleTaskResponse task) {

        if (task.getExpirationDate().isAfter(LocalDate.now()) || task.getExpirationDate().equals(LocalDate.now()))
            return true;

        UUID code = scheduleService.setExpiredCodeTask(task.getId());
        scheduleService.updateStatusScheduleTask(task.getId(), ScheduleTaskStatusEnum.EXPIRED);
        mailTextDomainService.sendScheduleMailExpired(scheduleMailExpired, task, getProlongationLink(code));

        log.info("Schedule task with id: " + task.getId() + " has expired date");
        return false;
    }

    private void checkStatusJob() {
        var forRemove = new ArrayList<Long>();

        jobRunners.keySet().forEach(idJob -> {

            ReportJobResponse job = jobDomainService.getJob(idJob);

            if (job != null) {

                switch (job.getStatus()) {
                    case SCHEDULED, RUNNING, EXPORT -> {}
                    case COMPLETE -> {
                        scheduleService.updateStatusScheduleTask(jobRunners.get(idJob), ScheduleTaskStatusEnum.COMPLETE);
                        taskRunners.put(jobRunners.get(idJob), idJob);
                        forRemove.add(idJob);

                    }
                    case FAILED -> {
                        try {
                            log.error("Error execute job schedule: " + job.getId() + ". " + job.getMessage());
                            var task = scheduleService.getScheduleTask(new ScheduleTaskRequest().setId(jobRunners.get(idJob)));
                            scheduleService.updateStatusScheduleTask(jobRunners.get(idJob), ScheduleTaskStatusEnum.FAILED);

                            mailTextDomainService.sendScheduleMailFailed(
                                    scheduleMailFailed,
                                    task,
                                    idJob,
                                    new Pair<String, StackTraceElement[]>().setL(job.getMessage()).setR(null));

                        } catch (Exception ex) {
                            log.error(ex.getMessage());
                        }
                        forRemove.add(idJob);
                    }
                    case CANCELING, CANCELED -> {
                        scheduleService.updateStatusScheduleTask(jobRunners.get(idJob), ScheduleTaskStatusEnum.SCHEDULED);
                        forRemove.add(idJob);
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + job.getStatus());
                }
            } else {
                scheduleService.updateStatusScheduleTask(jobRunners.get(idJob), ScheduleTaskStatusEnum.SCHEDULED);
                forRemove.add(idJob);
            }
        });
        forRemove.forEach(jobRunners::remove);
    }

    private void executeCompleteTask() {

        var tasks = scheduleService.getTaskForStatus(ScheduleTaskStatusEnum.COMPLETE);
        tasks.forEach(task -> {
            try {
                var job = reportJobService.getJob(taskRunners.get(task.getId()));
                if (job.getRowCount() == 0 && !task.getSendEmptyReport()) {
                    scheduleService.updateStatusScheduleTask(task.getId(), ScheduleTaskStatusEnum.SCHEDULED);
                    log.debug("ScheduleTask with id: " + task.getId() + " completed successfully! Mail not send!");
                    taskRunners.remove(task.getId());
                    return;
                }

                if (task.getTypeTask() == ScheduleTaskTypeEnum.EMAIL) {
                    var excelTemplateRequest = new ExcelReportRequest()
                            .setId(taskRunners.get(task.getId()))
                            .setExcelTemplateId(task.getExcelTemplate().getId());

                    reportJobService.createExcelReport(excelTemplateRequest);
                    var excelReport = reportJobService.getPathToExcelReport(excelTemplateRequest).toFile();


                    if (excelReport.length() < 11000000) {

                        var nameFile = task.getReport().getName() + " " + LocalDate.now() + ".xlsm";
                        mailTextDomainService.sendScheduleMailExcel(scheduleMailCompleteExcel,
                                task,
                                Collections.singletonList(new Pair<>(nameFile, excelReport)),
                                getWarningAboutExpiredScheduleTask(task));
                    } else {
                        mailTextDomainService.sendScheduleMailBigExcel(
                                scheduleMailBigSizeExcel,
                                task,
                                taskRunners.get(task.getId()),
                                host + SCHEDULE_TASK_GET_EXCEL_REPORT,
                                getWarningAboutExpiredScheduleTask(task)
                        );
                    }
                } else if (task.getTypeTask() == ScheduleTaskTypeEnum.USER_TASK) {

                    var users = task.getDestinationUsers().stream().map(DestinationUserResponse::getUserName).collect(Collectors.toList());
                    users.addAll(task.getDestinationRoles().stream()
                            .map(role -> roleService.getRoleUsers(new RoleRequest().setId(role.getRoleId())))
                            .flatMap(user -> user.getUsers().stream())
                            .map(UserResponse::getName).toList());

                    reportJobUserDomainService.addUsersJob(ReportJobUserTypeEnum.SCHEDULE, taskRunners.get(task.getId()), users
                            .stream()
                            .map(user -> userService.getUserResponse(new UserRequest().setUserName(user)))
                            .collect(Collectors.toList()));

                    mailTextDomainService.sendScheduleMailWeb(
                            scheduleMailCompleteWeb,
                            task,
                            taskRunners.get(task.getId()),
                            getWarningAboutExpiredScheduleTask(task)
                    );

                }
                scheduleService.updateStatusScheduleTask(task.getId(), ScheduleTaskStatusEnum.SCHEDULED);
                log.debug("ScheduleTask with id: " + task.getId() + " completed successfully!");
            } catch (Exception ex) {
                log.error(" ScheduleTask with id: " + task.getId() + " complete error! ", ex);
                scheduleService.updateStatusScheduleTask(task.getId(), ScheduleTaskStatusEnum.FAILED);
                mailTextDomainService.sendScheduleMailFailed(
                        scheduleMailFailed,
                        task,
                        taskRunners.get(task.getId()),
                        new Pair<String, StackTraceElement[]>().setL(ex.getMessage()).setR(ex.getStackTrace()));
            }

            taskRunners.remove(task.getId());

        });
    }

    private Pair<String, String> getWarningAboutExpiredScheduleTask(ScheduleTaskResponse task) {

        if (task.getSchedules()
                .stream()
                .map(scheduleService::getNextDateSchedule)
                .filter(Objects::nonNull)
                .noneMatch(date -> date.isAfter(task.getExpirationDate()) || date.isEqual(task.getExpirationDate()))
                )

            return new Pair<String, String>().setL("").setR("");

        else {
            UUID code = scheduleService.setExpiredCodeTask(task.getId());
            return new Pair<String, String>().setL(scheduleMailInformExpired).setR(getProlongationLink(code));
        }
    }

    @Scheduled(cron = "${magreport.jobengine.history-clear-schedule}")
    private void clearTokenHistory() {
        jobTokenDomainService.clearOldToken();
    }

    @Scheduled(cron = "${magreport.schedule-mail-time-send-warning}")
    private void sendWarningExpiredMails() {

        scheduleService.getTaskWithDeadlineExpires(daysBeforeExpired).forEach(task -> {
            var code = task.getExpirationCode() == null ? scheduleService.setExpiredCodeTask(task.getId()) : task.getExpirationCode();
            mailTextDomainService.sendScheduleMailDeadline(scheduleMailDeadlineExpires, task, getProlongationLink(code));
        });
    }

    private String getProlongationLink(UUID code) {
        return host + SCHEDULE_TASK_PROLONGATION + "?code=" + code;
    }
}
