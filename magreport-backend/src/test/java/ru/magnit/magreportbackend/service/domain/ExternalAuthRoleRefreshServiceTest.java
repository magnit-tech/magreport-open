package ru.magnit.magreportbackend.service.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceTypeEnum;
import ru.magnit.magreportbackend.domain.datasource.DataSourceTypeEnum;
import ru.magnit.magreportbackend.domain.user.RoleTypeEnum;
import ru.magnit.magreportbackend.dto.inner.asm.ExternalAuthSecurityView;
import ru.magnit.magreportbackend.dto.inner.asm.ExternalAuthSourceView;
import ru.magnit.magreportbackend.dto.inner.dataset.DataSetView;
import ru.magnit.magreportbackend.dto.inner.datasource.DataSourceData;
import ru.magnit.magreportbackend.service.RoleService;
import ru.magnit.magreportbackend.service.domain.asm.ExternalAuthRoleRefreshQueryBuilder;
import ru.magnit.magreportbackend.service.jobengine.filter.FilterQueryExecutor;

import java.util.Arrays;
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
class ExternalAuthRoleRefreshServiceTest {

    @Mock
    private ExternalAuthRoleRefreshQueryBuilder amsRoleSettingsQueryBuilder;

    @Mock
    private FilterQueryExecutor filterQuery;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private ExternalAuthRoleRefreshService service;

    @Test
    void refreshRoles() {

        when(filterQuery.getQueryResult(any(),any())).thenReturn(getQueryResult());

        service.refreshRoles(getExternalAuthSourceView());

        verify(filterQuery).getQueryResult(any(),any());
        verify(filterQuery,times(2)).executeSql(any(),any());
        verify(roleService).deleteRole(anyString());
        verify(roleService).addRole(any());
        verify(amsRoleSettingsQueryBuilder).buildQuery(any());
        verifyNoMoreInteractions(filterQuery,roleService,amsRoleSettingsQueryBuilder);
    }

    private ExternalAuthSecurityView getExternalAuthSourceView() {

        var source = new HashMap<ExternalAuthSourceTypeEnum, ExternalAuthSourceView>();
        source.put(ExternalAuthSourceTypeEnum.GROUP_SOURCE,
                new ExternalAuthSourceView()
                        .setDataSet(new DataSetView(null, null, null, null,
                                new DataSourceData(1L, DataSourceTypeEnum.H2, null, null, null, null),
                                null)));

        return new ExternalAuthSecurityView()
                .setSources(source)
                .setRoleType(RoleTypeEnum.SYSTEM);

    }

    private List<Map<String,String>> getQueryResult() {
        Map<String,String> map1 = new HashMap<>();
        map1.put("change_type","D");
        map1.put("role_name","");

        Map<String,String> map2 = new HashMap<>();
        map2.put("change_type","");
        map2.put("role_name","");

        return Arrays.asList(map1,map2);
    }


}
