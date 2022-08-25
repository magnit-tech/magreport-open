package ru.magnit.magreportbackend.service.domain.asm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.dto.inner.asm.ExternalAuthSourceView;

@Slf4j
@Service
public class ExternalAuthRoleRefreshQueryBuilder {

    public String buildQuery(ExternalAuthSourceView securitySource) {
        String result = "SELECT\n\t" +
                securitySource.getChangeTypeField().getDataSetField().getName() +
                ",\n\t" +
                securitySource.getRoleNameField().getDataSetField().getName() +
                "\nFROM " +
                securitySource.getDataSet().getSchemaName() + "." +
                securitySource.getDataSet().getObjectName() +
                "\nORDER BY\n\t" +
                securitySource.getChangeTypeField().getDataSetField().getName();

        log.debug("Query for AMS RoleRefresh:\n" + result);

        return result;
    }
}
