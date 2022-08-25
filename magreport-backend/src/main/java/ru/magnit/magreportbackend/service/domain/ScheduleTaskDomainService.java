package ru.magnit.magreportbackend.service.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTaskStatus;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTaskStatusEnum;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.request.schedule.ScheduleTaskAddRequest;
import ru.magnit.magreportbackend.dto.request.schedule.ScheduleTaskRequest;
import ru.magnit.magreportbackend.dto.response.schedule.ScheduleTaskResponse;
import ru.magnit.magreportbackend.dto.response.schedule.ScheduleTaskShortResponse;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.mapper.schedule.ScheduleTaskMapper;
import ru.magnit.magreportbackend.mapper.schedule.ScheduleTaskMerger;
import ru.magnit.magreportbackend.mapper.schedule.ScheduleTaskResponseMapper;
import ru.magnit.magreportbackend.mapper.schedule.ScheduleTaskShortResponseMapper;
import ru.magnit.magreportbackend.repository.ScheduleRepository;
import ru.magnit.magreportbackend.repository.ScheduleTaskRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor

public class ScheduleTaskDomainService {

    private final ScheduleTaskRepository repository;
    private final ScheduleRepository scheduleRepository;

    private final ScheduleTaskMapper scheduleTaskMapper;
    private final ScheduleTaskMerger scheduleTaskMerger;
    private final ScheduleTaskResponseMapper scheduleTaskResponseMapper;
    private final ScheduleTaskShortResponseMapper scheduleTaskShortResponseMapper;

    @Transactional
    public Long addScheduleTask(ScheduleTaskAddRequest request, UserView userView) {
        var task = scheduleTaskMapper.from(request);
        task.setUser(new User().setId(userView.getId()));
        var response = repository.save(task);
        return response.getId();
    }

    @Transactional
    public ScheduleTaskResponse getScheduleTask(Long id) {
        return scheduleTaskResponseMapper.from(repository.getReferenceById(id));
    }

    @Transactional
    public void deleteScheduleTask(ScheduleTaskRequest request) {
        repository.deleteById(request.getId());
    }

    @Transactional
    public List<ScheduleTaskShortResponse> getAllScheduleTask() {
        return scheduleTaskShortResponseMapper.from(repository.findAll());
    }


    @Transactional
    public UUID setExpiredCodeTask(Long id) {

        var code = UUID.randomUUID();
        var task = repository.getReferenceById(id);
        task.setExpirationCode(code);
        repository.save(task);
        return code;
    }

    @Transactional
    public String activationExpiredTask(UUID code) {

        var task = repository.findByExpirationCode(code);

        if (task == null) return "Код активации не действителен или уже использован!";

        task.setStatus(new ScheduleTaskStatus(ScheduleTaskStatusEnum.SCHEDULED.getId()));
        task.setExpirationCode(null);
        task.setExpirationDate(LocalDate.now().plusDays(task.getRenewalPeriod()));
        repository.save(task);

        return "Продление срока выполнения задания по расписанию выполнено успешно!";
    }

    @Transactional
    public void setStatusScheduleTask(Long idTask, ScheduleTaskStatusEnum status) {

        var task = repository.getReferenceById(idTask);

        if (status == ScheduleTaskStatusEnum.RUNNING &&
                ScheduleTaskStatusEnum.getById(task.getStatus().getId()) != ScheduleTaskStatusEnum.SCHEDULED &&
                ScheduleTaskStatusEnum.getById(task.getStatus().getId()) != ScheduleTaskStatusEnum.FAILED)
            throw new InvalidParametersException(
                    "Запуск задания с id: " + idTask + " из текущего статуса (" + ScheduleTaskStatusEnum.getById(task.getStatus().getId()) + ") невозможен");

        switch (status) {
            case RUNNING, COMPLETE, EXPIRED, CHANGED, INACTIVE ->
                    task.setStatus(new ScheduleTaskStatus(status.getId()));
            case SCHEDULED -> {
                task.setFailedStart(0L);
                task.setStatus(new ScheduleTaskStatus(status.getId()));
            }

            case FAILED -> {
                task.setFailedStart(task.getFailedStart() + 1);
                if (task.getFailedStart() < task.getMaxFailedStart() || task.getMaxFailedStart() == 0) {
                    task.setStatus(new ScheduleTaskStatus(ScheduleTaskStatusEnum.SCHEDULED.getId()));
                } else {
                    task.setStatus(new ScheduleTaskStatus(status.getId()));
                }
            }
            default -> throw new IllegalStateException("Unknown status: " + status);
        }
        repository.save(task);

    }

    @Transactional
    public List<ScheduleTaskResponse> getTaskForStatus(ScheduleTaskStatusEnum status) {
        return scheduleTaskResponseMapper.from(repository.findByStatusId(status.getId()));
    }

    @Transactional
    public void refreshStatusesAfterRestart() {

        var tasks = repository.findByStatusId(ScheduleTaskStatusEnum.RUNNING.getId());
        tasks.addAll(repository.findByStatusId(ScheduleTaskStatusEnum.COMPLETE.getId()));

        tasks.forEach(t -> {
            t.setStatus(new ScheduleTaskStatus().setId(ScheduleTaskStatusEnum.SCHEDULED.getId()));
            repository.save(t);
        });

    }

    @Transactional
    public Long findScheduleTaskByCode(String code) {

        var response = repository.findByCode(code);
        if (response != null)
            return response.getId();
        else
            throw new InvalidParametersException("Задание с кодом " + code + " не найдено");

    }

    @Transactional
    public List<ScheduleTaskResponse> findScheduleTaskByReport(Long reportId) {
        return scheduleTaskResponseMapper.from(repository.findByReportId(reportId));
    }


    @Transactional
    public void editScheduleTask(ScheduleTaskAddRequest request, UserView userView) {
        var task = repository.getReferenceById(request.getId());

        task = scheduleTaskMerger.merge(task, request);
        task.setUser(new User().setId(userView.getId()));

        task.setScheduleList(request.getSchedules().stream().map(s -> scheduleRepository.getReferenceById(s.getId())).collect(Collectors.toList()));// не исправлять на toList()

        repository.save(task);
    }

    @Transactional
    public List<ScheduleTaskResponse> getTaskWithDeadlineExpires(Long countDays) {
        return scheduleTaskResponseMapper.from(repository.findByExpirationDateBefore(LocalDate.now().plusDays(countDays)));
    }
}
