package ru.magnit.magreportbackend.service.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.schedule.Schedule;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.request.schedule.ScheduleAddRequest;
import ru.magnit.magreportbackend.mapper.schedule.ScheduleMapper;
import ru.magnit.magreportbackend.repository.ScheduleRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduleDomainServiceTest {

    private final static Long ID = 1L;
    @Mock
    private ScheduleRepository repository;

    @Mock
    private ScheduleMapper scheduleMapper;

    @Spy
    @InjectMocks
    private ScheduleDomainService domainService;

    @Test
    void addSchedule() {

        final var request = new ScheduleAddRequest();
        final var currentUser = new UserView();
        final var schedule = spy(new Schedule());
        final var savedSchedule = new Schedule().setId(ID);

        doNothing().when(domainService).setPlanStartDate(anyLong(), anyInt());
        when(scheduleMapper.from(any(ScheduleAddRequest.class))).thenReturn(schedule);
        when(repository.save(any())).thenReturn(savedSchedule);

        var result = domainService.addSchedule(currentUser, request);

        assertEquals(ID, result);

        verify(domainService).setPlanStartDate(anyLong(), anyInt());
        verify(schedule).setUser(any());
        verify(scheduleMapper).from(any(ScheduleAddRequest.class));
        verify(repository).save(any());

        verifyNoMoreInteractions(repository, scheduleMapper);
    }

}
