package ru.magnit.magreportbackend.mapper.securityfilter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstance;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterOperationType;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterOperationTypeEnum;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilter;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterFolder;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetResponse;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.RoleSettingsResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterDataSetResponse;
import ru.magnit.magreportbackend.dto.response.user.RoleResponse;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterInstanceResponseMapper;

import java.time.LocalDateTime;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class SecurityFilterResponseMapperTest {

    @Mock
    private SecurityFilterDataSetResponseMapper securityFilterDataSetResponseMapper;

    @Mock
    private FilterInstanceResponseMapper filterInstanceResponseMapper;

    @Mock
    private RoleSettingsResponseMapper roleSettingsResponseMapper;

    @InjectMocks
    private SecurityFilterResponseMapper mapper;

    private final static Long ID = 1L;
    private final static String NAME = "Name";
    private final static String USER = "User";
    private final static String DESCRIPTION = "Description";
    private final static LocalDateTime CREATE_TIME = LocalDateTime.now();
    private final static LocalDateTime MODIFIED_TIME = LocalDateTime.now();


    @Test
    void from() {

        Mockito.when(filterInstanceResponseMapper.from(Mockito.any(FilterInstance.class))).thenReturn(new FilterInstanceResponse());
        Mockito.when(securityFilterDataSetResponseMapper.from(Mockito.anyList())).thenReturn(Collections.singletonList(getSecurityFilterDataSetResponse()));
        Mockito.when(roleSettingsResponseMapper.from(Mockito.anyList())).thenReturn(Collections.singletonList(getRoleSettingsResponse()));

        var response = mapper.from(getSecurityFilter());

        Assertions.assertEquals(ID, response.id());
        Assertions.assertNotNull(response.filterInstance());
        Assertions.assertEquals(FilterOperationTypeEnum.IS_NOT_IN_LIST, response.operationType());
        Assertions.assertEquals(NAME, response.name());
        Assertions.assertEquals(1, response.dataSets().size());
        Assertions.assertEquals(1, response.roleSettings().size());
        Assertions.assertEquals(USER, response.userName());
        Assertions.assertEquals(CREATE_TIME, response.created());
        Assertions.assertEquals(MODIFIED_TIME, response.modified());
        Assertions.assertEquals(0, response.path().size());

        Mockito.verify(filterInstanceResponseMapper).from(Mockito.any(FilterInstance.class));
        Mockito.verify(securityFilterDataSetResponseMapper).from(Mockito.anyList());
        Mockito.verify(roleSettingsResponseMapper).from(Mockito.anyList());
        Mockito.verifyNoMoreInteractions(securityFilterDataSetResponseMapper, filterInstanceResponseMapper, roleSettingsResponseMapper);

    }

    @Test
    void fromList() {

        Mockito.when(filterInstanceResponseMapper.from(Mockito.any(FilterInstance.class))).thenReturn(new FilterInstanceResponse());
        Mockito.when(securityFilterDataSetResponseMapper.from(Mockito.anyList())).thenReturn(Collections.singletonList(getSecurityFilterDataSetResponse()));
        Mockito.when(roleSettingsResponseMapper.from(Mockito.anyList())).thenReturn(Collections.singletonList(getRoleSettingsResponse()));

        var responses = mapper.from(Collections.singletonList(getSecurityFilter()));
        Assertions.assertEquals(1, responses.size());
        var response = responses.get(0);

        Assertions.assertEquals(ID, response.id());
        Assertions.assertNotNull(response.filterInstance());
        Assertions.assertEquals(FilterOperationTypeEnum.IS_NOT_IN_LIST, response.operationType());
        Assertions.assertEquals(NAME, response.name());
        Assertions.assertEquals(1, response.dataSets().size());
        Assertions.assertEquals(1, response.roleSettings().size());
        Assertions.assertEquals(USER, response.userName());
        Assertions.assertEquals(CREATE_TIME, response.created());
        Assertions.assertEquals(MODIFIED_TIME, response.modified());
        Assertions.assertEquals(0, response.path().size());

        Mockito.verify(filterInstanceResponseMapper).from(Mockito.any(FilterInstance.class));
        Mockito.verify(securityFilterDataSetResponseMapper).from(Mockito.anyList());
        Mockito.verify(roleSettingsResponseMapper).from(Mockito.anyList());
        Mockito.verifyNoMoreInteractions(securityFilterDataSetResponseMapper, filterInstanceResponseMapper, roleSettingsResponseMapper);

    }


    private SecurityFilter getSecurityFilter() {
        return new SecurityFilter()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setFilterInstance(new FilterInstance())
                .setFolder(new SecurityFilterFolder())
                .setUser(new User().setName(USER))
                .setFieldMappings(Collections.emptyList())
                .setDataSets(Collections.emptyList())
                .setFilterRoles(Collections.emptyList())
                .setOperationType(new FilterOperationType().setId(ID))
                .setCreatedDateTime(CREATE_TIME)
                .setModifiedDateTime(MODIFIED_TIME);
    }

    private SecurityFilterDataSetResponse getSecurityFilterDataSetResponse() {
        return new SecurityFilterDataSetResponse(
                new DataSetResponse(),
                Collections.emptyList()
        );
    }

    private RoleSettingsResponse getRoleSettingsResponse() {
        return new RoleSettingsResponse(
                new RoleResponse(),
                Collections.emptyList()
        );
    }


}
