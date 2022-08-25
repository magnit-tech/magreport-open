package ru.magnit.magreportbackend.service.domain.asm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.dto.inner.asm.ExternalAuthSourceView;

@Slf4j
@Service
public class ExternalAuthUserRoleRefreshQueryBuilder {

    public String buildQuery(ExternalAuthSourceView securitySource) {
        String result = "SELECT\n\t" +
                securitySource.getChangeTypeField().getDataSetField().getName() +
                ",\n\t" +
                securitySource.getUserNameField().getDataSetField().getName() +
                ",\n\t" +
                securitySource.getRoleNameField().getDataSetField().getName() +
                "\nFROM\n\t" +
                securitySource.getDataSet().getSchemaName() + "." +
                securitySource.getDataSet().getObjectName();

        log.debug("Ams UserRoles refresh query:\n" + result);

        return result;
    }
}
