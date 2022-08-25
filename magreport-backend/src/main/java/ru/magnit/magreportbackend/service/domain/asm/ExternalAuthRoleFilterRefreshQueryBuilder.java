package ru.magnit.magreportbackend.service.domain.asm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.dto.inner.asm.ExternalAuthSourceView;

import java.util.stream.Collectors;

@Slf4j
@Service
public class ExternalAuthRoleFilterRefreshQueryBuilder {


    public String buildQuery(ExternalAuthSourceView securitySource) {
        final var query = "SELECT\n\t" +
                securitySource.getRoleNameField().getDataSetField().getName() +
                ",\n\t" +
                securitySource.getFilterSettingsFields()
                        .stream()
                        .map(field -> field.getDataSetField().getName())
                        .collect(Collectors.joining(",\n\t")) +
                "\nFROM\n\t" +
                securitySource.getDataSet().getSchemaName() +
                "." +
                securitySource.getDataSet().getObjectName() +
                "\nORDER BY\n\t" +
                securitySource.getRoleNameField().getDataSetField().getName();

        log.debug("ASM Role filters query:\n" + query);
        return query;
    }

}
