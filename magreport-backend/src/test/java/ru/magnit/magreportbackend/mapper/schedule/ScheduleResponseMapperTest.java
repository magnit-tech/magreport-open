package ru.magnit.magreportbackend.mapper.schedule;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.schedule.Schedule;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTask;
import ru.magnit.magreportbackend.domain.schedule.ScheduleType;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTypeEnum;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.response.schedule.ScheduleTaskShortResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ScheduleResponseMapperTest {

    private static final Long ID = 1L;
    private static final String NAME = "schedule";
    private static final String DESCRIPTION = "description";
    private static final Long HOUR = 23L;
    private static final Long MINUTE = 59L;
    private static final Long SECOND = 58L;
    private static final Long DAY = 5L;
    private static final Long DAY_WEEK = 6L;
    private static final Long MONTH = 12L;
    private static final Long YEAR = 2000L;
    private static final Long DAY_END_MONTH = 7L;
    private static final Long WEEK_MONTH = 2L;
    private static final Long WEEK_END_MONTH = 3L;
    private static final User USER = new User(-1L).setName("John Doe");
    private static final LocalDateTime CREATED = LocalDateTime.now().minusDays(1L);
    private static final LocalDateTime MODIFIED = LocalDateTime.now();
    private static final ScheduleTypeEnum TYPE_ENUM = ScheduleTypeEnum.DAY_END_MONTH;
    private static final ScheduleType TYPE = new ScheduleType(TYPE_ENUM.getId()).setName(TYPE_ENUM.name());
    private static final List<ScheduleTask> TASKS = Collections.emptyList();
    private static final List<ScheduleTaskShortResponse> MAPPED_TASKS = Collections.emptyList();
    private static final LocalDateTime PLAN_START_DATE = LocalDateTime.now().plusDays(3L);


    @InjectMocks
    private ScheduleResponseMapper mapper;

    @Mock
    private ScheduleTaskShortResponseMapper taskMapper;

    @Test
    void from() {
        when(taskMapper.from(anyList())).thenReturn(MAPPED_TASKS);
        var schedule = getSchedule();

        var response = mapper.from(schedule);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(HOUR, response.getHour());
        assertEquals(MINUTE, response.getMinute());
        assertEquals(SECOND, response.getSecond());
        assertEquals(DAY, response.getDay());
        assertEquals(DAY_WEEK, response.getDayWeek());
        assertEquals(MONTH, response.getMonth());
        assertEquals(YEAR, response.getYear());
        assertEquals(DAY_END_MONTH, response.getDayEndMonth());
        assertEquals(WEEK_MONTH, response.getWeekMonth());
        assertEquals(WEEK_END_MONTH, response.getWeekEndMonth());
        assertEquals(USER.getName(), response.getUserName());
        assertEquals(TYPE_ENUM, response.getType());
        assertEquals(MAPPED_TASKS, response.getTasks());
        assertEquals(CREATED, response.getCreated());
        assertEquals(MODIFIED, response.getModified());
        assertEquals(PLAN_START_DATE, response.getPlanStartDate());


        verify(taskMapper).from(anyList());

    }


    private Schedule getSchedule() {
        var schedule = new Schedule()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setHour(HOUR)
                .setMinute(MINUTE)
                .setSecond(SECOND)
                .setDay(DAY)
                .setDayWeek(DAY_WEEK)
                .setMonth(MONTH)
                .setYear(YEAR)
                .setDayEndMonth(DAY_END_MONTH)
                .setWeekMonth(WEEK_MONTH)
                .setWeekEndMonth(WEEK_END_MONTH)
                .setType(TYPE)
                .setScheduleTasks(TASKS)
                .setUser(USER)
                .setPlanStartDate(PLAN_START_DATE);
        schedule.setCreatedDateTime(CREATED);
        schedule.setModifiedDateTime(MODIFIED);

        return schedule;
    }
}