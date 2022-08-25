package ru.magnit.magreportbackend.mapper.asm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSecurityFilter;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilter;
import ru.magnit.magreportbackend.dto.request.asm.AsmSecuritySecurityFilterAddRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ExternalAuthSecurityFilterMapper implements Mapper<ExternalAuthSecurityFilter, AsmSecuritySecurityFilterAddRequest> {

    @Override
    public ExternalAuthSecurityFilter from(AsmSecuritySecurityFilterAddRequest source) {

        return new ExternalAuthSecurityFilter(source.getId())
                .setSecurityFilter(new SecurityFilter(source.getSecurityFilterId()));
    }
}
