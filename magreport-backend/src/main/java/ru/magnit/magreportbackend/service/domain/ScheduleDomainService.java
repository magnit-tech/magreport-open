package ru.magnit.magreportbackend.service.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.schedule.Schedule;
import ru.magnit.magreportbackend.domain.schedule.ScheduleCalendarInfo;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTypeEnum;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.request.schedule.ScheduleAddRequest;
import ru.magnit.magreportbackend.dto.request.schedule.ScheduleRequest;
import ru.magnit.magreportbackend.dto.response.schedule.ScheduleResponse;
import ru.magnit.magreportbackend.dto.response.schedule.ScheduleTaskResponse;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.mapper.schedule.ScheduleMapper;
import ru.magnit.magreportbackend.mapper.schedule.ScheduleMerger;
import ru.magnit.magreportbackend.mapper.schedule.ScheduleResponseMapper;
import ru.magnit.magreportbackend.mapper.schedule.ScheduleTaskResponseMapper;
import ru.magnit.magreportbackend.repository.ScheduleCalendarInfoRepository;
import ru.magnit.magreportbackend.repository.ScheduleRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor


public class ScheduleDomainService {

    private final ScheduleRepository repository;
    private final ScheduleCalendarInfoRepository calendarRepository;

    private final ScheduleMapper scheduleMapper;
    private final ScheduleMerger scheduleMerger;
    private final ScheduleResponseMapper scheduleResponseMapper;
    private final ScheduleTaskResponseMapper scheduleTaskResponseMapper;

    @Transactional
    public Long addSchedule(UserView currentUser, ScheduleAddRequest request) {

        var schedule = scheduleMapper.from(request);
        schedule.setUser(new User(currentUser.getId()));
        var response = repository.save(schedule);
        setPlanStartDate(response.getId(), 0);

        return response.getId();
    }

    @Transactional
    public ScheduleResponse getSchedule(Long id) {
        return scheduleResponseMapper.from(repository.getReferenceById(id));
    }

    @Transactional
    public void deleteSchedule(ScheduleRequest request) {
        repository.deleteById(request.getId());
    }

    @Transactional
    public void editSchedule(ScheduleAddRequest request) {
        var schedule = repository.getReferenceById(request.getId());
        repository.save(scheduleMerger.merge(schedule, request));
        setPlanStartDate(request.getId(), 0);
    }

    @Transactional
    public List<ScheduleResponse> getAllSchedule() {
        return scheduleResponseMapper.from(repository.findAll());
    }

    @Transactional
    public void setPlanStartDate(Long id, int numberDate) {

        var schedule = repository.getReferenceById(id);
        if (schedule.getType().getId().equals(ScheduleTypeEnum.MANUAL.getId())) return;

        var dates = getNextDateSchedule(schedule.getId(), schedule.getType().getId());
        if (dates.size() >= numberDate + 1) {
            var date = dates.get(numberDate);
            var time = LocalTime.of(schedule.getHour().intValue(), schedule.getMinute().intValue(), schedule.getSecond().intValue());
            var dataTime = LocalDateTime.of(date, time).plusHours(schedule.getDifferenceTime());

            if (LocalDateTime.now().isAfter(dataTime)) {
                date = dates.get(numberDate + 1);
                time = LocalTime.of(schedule.getHour().intValue(), schedule.getMinute().intValue(), schedule.getSecond().intValue());
                dataTime = LocalDateTime.of(date, time).plusHours(schedule.getDifferenceTime());
            }

            schedule.setPlanStartDate(dataTime);
            schedule.setLastStartDate(LocalDateTime.now());
            repository.save(schedule);
        } else
            throw new InvalidParametersException("Not found next plan start date for schedule:" + id);
    }

    public List<LocalDate> getNextDateSchedule(Long id, Long typeId) {

        return switch (ScheduleTypeEnum.getById(typeId)) {
            case EVERY_DAY -> calendarRepository.getDatesForEveryDaySchedule().stream().map(ScheduleCalendarInfo::getDate).toList();
            case EVERY_WEEK -> calendarRepository.getDatesForEveryWeekSchedule(id).stream().map(ScheduleCalendarInfo::getDate).toList();
            case EVERY_MONTH -> calendarRepository.getDatesForEveryMonthSchedule(id).stream().map(ScheduleCalendarInfo::getDate).toList();
            case DAY_END_MONTH -> calendarRepository.getDatesForDayEndMonthSchedule(id).stream().map(ScheduleCalendarInfo::getDate).toList();
            case WEEK_MONTH -> calendarRepository.getDatesForWeekMonthSchedule(id).stream().map(ScheduleCalendarInfo::getDate).toList();
            case WEEK_END_MONTH -> calendarRepository.getDatesForWeekEndMonthSchedule(id).stream().map(ScheduleCalendarInfo::getDate).toList();
            case MANUAL -> Collections.emptyList();
        };
    }

    @Transactional
    public List<ScheduleTaskResponse> getTaskForDate(LocalDateTime date) {
        var response = repository.getTaskForDate(date);
        response.forEach(s -> setPlanStartDate(s.getId(), 1));

        return response
                .stream()
                .map(Schedule::getScheduleTasks)
                .flatMap(Collection::stream)
                .map(scheduleTaskResponseMapper::from)
                .toList();
    }
}
