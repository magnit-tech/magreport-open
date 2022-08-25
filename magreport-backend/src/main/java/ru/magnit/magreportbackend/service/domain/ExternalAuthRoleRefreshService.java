package ru.magnit.magreportbackend.service.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.dto.inner.asm.ExternalAuthSecurityView;
import ru.magnit.magreportbackend.dto.inner.asm.ExternalAuthSourceView;
import ru.magnit.magreportbackend.dto.request.user.RoleAddRequest;
import ru.magnit.magreportbackend.service.RoleService;
import ru.magnit.magreportbackend.service.domain.asm.ExternalAuthRoleRefreshQueryBuilder;
import ru.magnit.magreportbackend.service.jobengine.filter.FilterQueryExecutor;

import java.util.List;
import java.util.Map;

import static ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceTypeEnum.GROUP_SOURCE;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalAuthRoleRefreshService {

    private static final String CHANGE_TYPE = "change_type";
    private static final String DELETE = "D";
    private static final String ROLE_NAME = "role_name";
    private static final String ROLE_DESCRIPTION = "Роль для автоматического создания секьюрити фильтров AMS";
    private final ExternalAuthRoleRefreshQueryBuilder amsRoleSettingsQueryBuilder;
    private final FilterQueryExecutor filterQuery;
    private final RoleService roleService;

    public void refreshRoles(ExternalAuthSecurityView securityView) {
        ExternalAuthSourceView roleNamesSource = securityView.getSources().get(GROUP_SOURCE);

        filterQuery.executeSql(roleNamesSource.getDataSet().getDataSource(), roleNamesSource.getPreSql());

        List<Map<String, String>> queryResult = filterQuery.getQueryResult(roleNamesSource.getDataSet().getDataSource(), amsRoleSettingsQueryBuilder.buildQuery(roleNamesSource));

        processRecords(queryResult, securityView);

        filterQuery.executeSql(roleNamesSource.getDataSet().getDataSource(), roleNamesSource.getPostSql());
    }

    private void processRecords(List<Map<String, String>> records, ExternalAuthSecurityView securityView) {

        records.forEach(currentRecord -> {
            if (currentRecord.get(CHANGE_TYPE).equalsIgnoreCase(DELETE)) {
                roleService.deleteRole(currentRecord.get(ROLE_NAME));
            } else {
                roleService.addRole(new RoleAddRequest(null, (long) securityView.getRoleType().ordinal(), currentRecord.get(ROLE_NAME), ROLE_DESCRIPTION));
            }
        });
    }
}
