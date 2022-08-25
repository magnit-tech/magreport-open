package ru.magnit.magreportbackend.service.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceTypeEnum;
import ru.magnit.magreportbackend.domain.datasource.DataSourceTypeEnum;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterFieldTypeEnum;
import ru.magnit.magreportbackend.domain.user.RoleTypeEnum;
import ru.magnit.magreportbackend.dto.inner.asm.ExternalAuthSecurityFilterView;
import ru.magnit.magreportbackend.dto.inner.asm.ExternalAuthSecurityView;
import ru.magnit.magreportbackend.dto.inner.asm.ExternalAuthSourceView;
import ru.magnit.magreportbackend.dto.inner.dataset.DataSetView;
import ru.magnit.magreportbackend.dto.inner.datasource.DataSourceData;
import ru.magnit.magreportbackend.dto.inner.filter.FilterData;
import ru.magnit.magreportbackend.dto.inner.filter.FilterFieldData;
import ru.magnit.magreportbackend.dto.response.user.RoleResponse;
import ru.magnit.magreportbackend.service.RoleService;
import ru.magnit.magreportbackend.service.SecurityFilterService;
import ru.magnit.magreportbackend.service.domain.asm.ExternalAuthPermissionWriter;
import ru.magnit.magreportbackend.service.jobengine.filter.FilterQueryExecutor;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalAuthRoleFilterRefreshServiceTest {

    @Mock
    private ExternalAuthPermissionWriter permissionWriter;

    @Mock
    private FilterQueryExecutor filterQuery;

    @Mock
    private SecurityFilterService securityFilterService;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private ExternalAuthRoleFilterRefreshService service;


    @Test
    void refreshSecurityFilters() {

        service.refreshSecurityFilters(getExternalAuthSecurityView());

        verify(filterQuery, times(2)).executeSql(any(), any());
        verify(permissionWriter).process(any(), any());
        verify(filterQuery, times(2)).executeSql(any(), anyString());
        verifyNoMoreInteractions(filterQuery, permissionWriter);

    }

    @Test
    void processPermissionSet() {

        when(roleService.getRoleByName(anyString())).thenReturn(new RoleResponse().setId(1L));

        service.processPermissionSet(getPermissions(), getExternalAuthSourceView());

        verify(roleService).getRoleByName(anyString());
        verify(securityFilterService).updateRoleSettings(any());
        verifyNoMoreInteractions(roleService, securityFilterService);
    }

    private ExternalAuthSecurityView getExternalAuthSecurityView() {

        var source = new HashMap<ExternalAuthSourceTypeEnum, ExternalAuthSourceView>();
        source.put(ExternalAuthSourceTypeEnum.PERMISSION_SOURCE, getExternalAuthSourceView());

        return new ExternalAuthSecurityView()
                .setSources(source)
                .setRoleType(RoleTypeEnum.SYSTEM);

    }

    private ExternalAuthSourceView getExternalAuthSourceView() {
        return new ExternalAuthSourceView()
                .setPreSql("")
                .setPostSql("")
                .setFilters(Collections.singletonList(
                        new ExternalAuthSecurityFilterView()
                                .setFilterInstance(
                                        new FilterData(null,
                                                1L,
                                                null,
                                                null,
                                                null,
                                                null,
                                                null,
                                                null,
                                                Collections.singletonList(
                                                        new FilterFieldData(
                                                                0L, 0, "name", null, "field_name", null, FilterFieldTypeEnum.CODE_FIELD)
                                                )))))
                .setDataSet(new DataSetView(null, null, null, null,
                        new DataSourceData(1L, DataSourceTypeEnum.H2, null, null, null, null),
                        null));
    }

    private List<Map<String, String>> getPermissions() {

        Map<String, String> permission1 = new HashMap<>();
        permission1.put("role_name", "");

        Map<String, String> permission2 = new HashMap<>();
        permission2.put("1", "");

        return Arrays.asList(permission1, permission2);
    }
}
