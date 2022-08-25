package ru.magnit.magreportbackend.service.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.dto.response.schedule.ScheduleTypeResponse;
import ru.magnit.magreportbackend.mapper.schedule.ScheduleTypeResponseMapper;
import ru.magnit.magreportbackend.repository.ScheduleTypeRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleTypeDomainService {
    private final ScheduleTypeRepository repository;
    private final ScheduleTypeResponseMapper scheduleTypeResponseMapper;

    public List<ScheduleTypeResponse> getAll() {
        return scheduleTypeResponseMapper.from(repository.findAll());
    }
}
