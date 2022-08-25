package ru.magnit.magreportbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.magnit.magreportbackend.domain.schedule.ScheduleTypeEnum;
import ru.magnit.magreportbackend.dto.request.schedule.ScheduleRequest;
import ru.magnit.magreportbackend.dto.response.schedule.ScheduleResponse;
import ru.magnit.magreportbackend.dto.response.schedule.ScheduleTaskShortResponse;
import ru.magnit.magreportbackend.service.ScheduleService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static ru.magnit.magreportbackend.controller.ScheduleController.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;


@ExtendWith(MockitoExtension.class)
class ScheduleControllerTest {

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
    private static final String USER_NAME = "admin";
    private static final ScheduleTypeEnum TYPE_ENUM = ScheduleTypeEnum.WEEK_MONTH;
    private static final List<ScheduleTaskShortResponse> TASKS = Collections.singletonList(new ScheduleTaskShortResponse().setCode("task code"));
    private static final LocalDateTime CREATED = LocalDateTime.now().minusDays(5L);
    private static final LocalDateTime MODIFIED = LocalDateTime.now();
    private static final LocalDateTime PLAN_START_DATE = LocalDateTime.now().plusDays(2L);

    @Mock
    private ScheduleService service;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private ScheduleController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private DateTimeFormatter formatter;

    @BeforeEach
    public void initMock() {
        when(authentication.getName()).thenReturn(USER_NAME);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
        formatter = DateTimeFormatter.ofPattern(ISO_DATE_TIME_PATTERN);
    }


    @Test
    void get() throws Exception {

        when(service.getSchedule(any())).thenReturn(getScheduleResponse());

        mockMvc.perform(post(SCHEDULE_GET)
                        .accept(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(getScheduleRequest()))
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data.id", is(ID.intValue())))
                .andExpect(jsonPath("$.data.name", is(NAME)))
                .andExpect(jsonPath("$.data.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.data.hour", is(HOUR.intValue())))
                .andExpect(jsonPath("$.data.minute", is(MINUTE.intValue())))
                .andExpect(jsonPath("$.data.second", is(SECOND.intValue())))
                .andExpect(jsonPath("$.data.day", is(DAY.intValue())))
                .andExpect(jsonPath("$.data.dayWeek", is(DAY_WEEK.intValue())))
                .andExpect(jsonPath("$.data.month", is(MONTH.intValue())))
                .andExpect(jsonPath("$.data.year", is(YEAR.intValue())))
                .andExpect(jsonPath("$.data.dayEndMonth", is(DAY_END_MONTH.intValue())))
                .andExpect(jsonPath("$.data.weekMonth", is(WEEK_MONTH.intValue())))
                .andExpect(jsonPath("$.data.weekEndMonth", is(WEEK_END_MONTH.intValue())))
                .andExpect(jsonPath("$.data.planStartDate", is(PLAN_START_DATE.format(formatter))))
                .andExpect(jsonPath("$.data.userName", is(USER_NAME)))
                .andExpect(jsonPath("$.data.type", is(TYPE_ENUM.name())))
                .andExpect(jsonPath("$.data.tasks[0].code", is(TASKS.get(0).getCode())))
                .andExpect(jsonPath("$.data.created", is(CREATED.format(formatter))))
                .andExpect(jsonPath("$.data.modified", is(MODIFIED.format(formatter))));


        verify(service).getSchedule(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAll() throws Exception {

        when(service.getAllSchedules()).thenReturn(Collections.singletonList(getScheduleResponse()));

        mockMvc.perform(post(SCHEDULE_GET_ALL)
                        .accept(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(""))
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.data[0].id", is(ID.intValue())))
                .andExpect(jsonPath("$.data[0].name", is(NAME)))
                .andExpect(jsonPath("$.data[0].description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.data[0].hour", is(HOUR.intValue())))
                .andExpect(jsonPath("$.data[0].minute", is(MINUTE.intValue())))
                .andExpect(jsonPath("$.data[0].second", is(SECOND.intValue())))
                .andExpect(jsonPath("$.data[0].day", is(DAY.intValue())))
                .andExpect(jsonPath("$.data[0].dayWeek", is(DAY_WEEK.intValue())))
                .andExpect(jsonPath("$.data[0].month", is(MONTH.intValue())))
                .andExpect(jsonPath("$.data[0].year", is(YEAR.intValue())))
                .andExpect(jsonPath("$.data[0].dayEndMonth", is(DAY_END_MONTH.intValue())))
                .andExpect(jsonPath("$.data[0].weekMonth", is(WEEK_MONTH.intValue())))
                .andExpect(jsonPath("$.data[0].weekEndMonth", is(WEEK_END_MONTH.intValue())))
                .andExpect(jsonPath("$.data[0].planStartDate", is(PLAN_START_DATE.format(formatter))))
                .andExpect(jsonPath("$.data[0].userName", is(USER_NAME)))
                .andExpect(jsonPath("$.data[0].type", is(TYPE_ENUM.name())))
                .andExpect(jsonPath("$.data[0].tasks[0].code", is(TASKS.get(0).getCode())))
                .andExpect(jsonPath("$.data[0].created", is(CREATED.format(formatter))))
                .andExpect(jsonPath("$.data[0].modified", is(MODIFIED.format(formatter))));


        verify(service).getAllSchedules();
        verifyNoMoreInteractions(service);
    }

    private ScheduleRequest getScheduleRequest() {
        return new ScheduleRequest().setId(ID);
    }

    private ScheduleResponse getScheduleResponse() {
        return new ScheduleResponse()
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
                .setType(TYPE_ENUM)
                .setTasks(TASKS)
                .setPlanStartDate(PLAN_START_DATE)
                .setUserName(USER_NAME)
                .setCreated(CREATED)
                .setModified(MODIFIED);
    }


}