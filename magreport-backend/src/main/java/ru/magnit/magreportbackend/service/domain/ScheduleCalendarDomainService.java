package ru.magnit.magreportbackend.service.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.schedule.ScheduleCalendarInfo;
import ru.magnit.magreportbackend.repository.ScheduleCalendarInfoRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ScheduleCalendarDomainService {

    private final ScheduleCalendarInfoRepository repository;


    public void addNewDates(Long countDay) {

        var date = LocalDate.now().minusDays(1);
        var lastRecordTable = repository.findFirstByOrderByDateDesc();

        if (lastRecordTable != null) date = lastRecordTable.getDate();
        var stopDate = date.plusDays(countDay);

        while (date.isBefore(stopDate)) {
            date = date.plusDays(1);
            repository.save(new ScheduleCalendarInfo(date));
        }

        repository.removeOldDate(LocalDate.now().minusDays(1));
    }

    public boolean checkDates() {
        return repository.findAll().isEmpty();
    }
}
