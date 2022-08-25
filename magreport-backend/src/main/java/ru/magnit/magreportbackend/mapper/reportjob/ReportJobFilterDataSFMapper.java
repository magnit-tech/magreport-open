package ru.magnit.magreportbackend.mapper.reportjob;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterOperationTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTypeEnum;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterRole;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobFilterData;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ReportJobFilterDataSFMapper implements Mapper<ReportJobFilterData, SecurityFilterRole> {

    private final ReportJobTupleDataSFMapper tupleDataSFMapper;

    @Override
    public ReportJobFilterData from(SecurityFilterRole source) {

        return new ReportJobFilterData(
                source.getSecurityFilter().getFilterInstance().getId(),
                FilterTypeEnum.getByOrdinal(source.getSecurityFilter().getFilterInstance().getFilterTemplate().getType().getId()),
                FilterOperationTypeEnum.IS_IN_LIST,
                source.getSecurityFilter().getFilterInstance().getCode(),
                tupleDataSFMapper.from(source.getFilterRoleTuples())
        );
    }
}
