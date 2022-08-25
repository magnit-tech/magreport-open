package ru.magnit.magreportbackend.service.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceTypeEnum;
import ru.magnit.magreportbackend.dto.inner.asm.ExternalAuthSecurityView;
import ru.magnit.magreportbackend.dto.inner.asm.ExternalAuthSourceView;
import ru.magnit.magreportbackend.dto.inner.dataset.DataSetView;
import ru.magnit.magreportbackend.dto.inner.datasource.DataSourceData;
import ru.magnit.magreportbackend.service.UserService;
import ru.magnit.magreportbackend.service.domain.asm.ExternalAuthUserRoleRefreshQueryBuilder;
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
class ExternalAuthUserRoleRefreshServiceTest {

    @Mock
    private ExternalAuthUserRoleRefreshQueryBuilder queryBuilder;

    @Mock
    private FilterQueryExecutor filterQuery;

    @Mock
    private UserService userService;

    @InjectMocks
    private ExternalAuthUserRoleRefreshService service;


    @Test
    void refreshUserRoles() {
        when(filterQuery.getQueryResult(any(), any())).thenReturn(getQueryResult());

        service.refreshUserRoles(getExternalAuthSecurityView());

        verify(filterQuery, times(2)).executeSql(any(), any());
        verify(filterQuery).getQueryResult(any(), any());
        verify(userService, times(2)).loginUser(anyString(), any());
        verify(userService, times(2)).clearRoles(anyString(), any());
        verify(userService).setUserRoles(anyString(), any());
        verify(queryBuilder).buildQuery(any());

        verifyNoMoreInteractions(filterQuery, userService, queryBuilder);
    }

    private ExternalAuthSecurityView getExternalAuthSecurityView() {

        Map<ExternalAuthSourceTypeEnum, ExternalAuthSourceView> source = new HashMap<>();
        source.put(ExternalAuthSourceTypeEnum.USER_MAP_SOURCE,
                new ExternalAuthSourceView()
                        .setDataSet(new DataSetView(null, null, null, null,
                                new DataSourceData(null, null, null, null, null, null), null)));

        return new ExternalAuthSecurityView()
                .setSources(source);

    }

    private List<Map<String, String>> getQueryResult() {
        Map<String, String> record1 = new HashMap<>();
        record1.put("user_name", "");
        record1.put("change_type", "");

        Map<String, String> record2 = new HashMap<>();
        record2.put("user_name", "");
        record2.put("change_type", "I");

        return Arrays.asList(record1, record2);
    }
}
