package ru.magnit.magreportbackend.mapper.securityfilter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterDataSet;
import ru.magnit.magreportbackend.dto.request.securityfilter.SecurityFilterDataSetAddRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class SecurityFilterDataSetMapper implements Mapper<SecurityFilterDataSet, SecurityFilterDataSetAddRequest> {

    @Override
    public SecurityFilterDataSet from(SecurityFilterDataSetAddRequest source) {

        return new SecurityFilterDataSet()
                .setDataSet(new DataSet(source.getDataSetId()));
    }
}
