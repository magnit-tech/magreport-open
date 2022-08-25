package ru.magnit.magreportbackend.mapper.olap;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.olap.ReportOlapConfiguration;
import ru.magnit.magreportbackend.mapper.Cloner;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReportOlapConfigurationCloner implements Cloner<ReportOlapConfiguration> {

    private final OlapConfigurationCloner olapConfigurationCloner;

    @Override
    public ReportOlapConfiguration clone(ReportOlapConfiguration source) {
        return new ReportOlapConfiguration()
                .setReport(source.getReport())
                .setCreator(source.getCreator())
                .setReportJob(source.getReportJob())
                .setUser(source.getUser())
                .setOlapConfiguration(olapConfigurationCloner.clone(source.getOlapConfiguration()))
                .setIsShared(source.getIsShared())
                .setIsCurrent(source.getIsCurrent())
                .setIsDefault(source.getIsDefault())
                .setCreatedDateTime(LocalDateTime.now())
                .setModifiedDateTime(LocalDateTime.now());
    }
}
