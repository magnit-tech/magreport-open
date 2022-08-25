package ru.magnit.magreportbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.magnit.magreportbackend.domain.filterreport.FilterReportGroup;

public interface FilterReportGroupRepository extends JpaRepository<FilterReportGroup, Long> {

    void deleteByReportId(Long reportId);

    FilterReportGroup getByReportIdAndParentGroupIsNull(Long reportId);
}
