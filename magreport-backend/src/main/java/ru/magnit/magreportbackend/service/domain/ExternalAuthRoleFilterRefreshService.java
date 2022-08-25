package ru.magnit.magreportbackend.service.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum;
import ru.magnit.magreportbackend.dto.inner.asm.ExternalAuthSecurityView;
import ru.magnit.magreportbackend.dto.inner.asm.ExternalAuthSourceView;
import ru.magnit.magreportbackend.dto.request.securityfilter.RoleSettingsRequest;
import ru.magnit.magreportbackend.dto.request.securityfilter.SecurityFilterSetRoleRequest;
import ru.magnit.magreportbackend.dto.tuple.Tuple;
import ru.magnit.magreportbackend.dto.tuple.TupleValue;
import ru.magnit.magreportbackend.service.RoleService;
import ru.magnit.magreportbackend.service.SecurityFilterService;
import ru.magnit.magreportbackend.service.domain.asm.ExternalAuthPermissionWriter;
import ru.magnit.magreportbackend.service.jobengine.filter.FilterQueryExecutor;
import ru.magnit.magreportbackend.util.Pair;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceTypeEnum.PERMISSION_SOURCE;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalAuthRoleFilterRefreshService {

    public static final String ROLE_NAME = "role_name";
    private final ExternalAuthPermissionWriter permissionWriter;
    private final FilterQueryExecutor filterQuery;
    private final SecurityFilterService securityFilterService;
    private final RoleService roleService;

    public void refreshSecurityFilters(ExternalAuthSecurityView securityView) {
        var permissionSource = securityView.getSources().get(PERMISSION_SOURCE);

        filterQuery.executeSql(permissionSource.getDataSet().getDataSource(), permissionSource.getPreSql());

        permissionWriter.process(permissionSource, this::processPermissionSet);

        filterQuery.executeSql(permissionSource.getDataSet().getDataSource(), permissionSource.getPostSql());
    }

    public void processPermissionSet(List<Map<String, String>> permissions, ExternalAuthSourceView permissionSource) {

        if (permissions == null || permissions.isEmpty()) return;

        permissionSource.getFilters().forEach(filter -> {

            final var fieldNames = filter
                .getFilterInstance()
                .fields()
                .stream()
                .filter(f -> f.fieldType() == FilterFieldTypeEnum.CODE_FIELD)
                .map(o -> new Pair<>(o.fieldName().toLowerCase(), o.fieldId()))
                .collect(Collectors.toMap(Pair::getL, Pair::getR));


            final var tuples = permissions.stream().map(o ->
                    new Tuple(o.entrySet()
                        .stream()
                        .filter(f -> f.getValue() != null && !f.getKey().equalsIgnoreCase(ROLE_NAME))
                        .map(f -> new TupleValue(fieldNames.get(f.getKey()), f.getValue()))
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());

            final var roleName = permissions.get(0).get(ROLE_NAME);
            final var role = roleService.getRoleByName(roleName);
            if (role != null) {
                var filterParam = new SecurityFilterSetRoleRequest()
                    .setSecurityFilterId(filter.getSecurityFilterId())
                    .setRoleSettings(Collections.singletonList(new RoleSettingsRequest()
                        .setRoleId(role.getId())
                        .setTuples(tuples)));

                securityFilterService.updateRoleSettings(filterParam);
            }
        });
    }
}
