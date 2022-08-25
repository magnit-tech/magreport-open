package ru.magnit.magreportbackend.mapper.securityfilter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstance;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterOperationTypeEnum;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilter;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterFolder;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.request.securityfilter.SecurityFilterAddRequest;
import ru.magnit.magreportbackend.dto.request.securityfilter.SecurityFilterDataSetAddRequest;

import java.time.LocalDateTime;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class SecurityFilterMergerTest {

    @Mock
    private SecurityFilterDataSetFieldMapper securityFilterDataSetFieldMapper;

    @Mock
    private SecurityFilterDataSetMapper securityFilterDataSetMapper;

    @InjectMocks
    private SecurityFilterMerger merger;

    private final static Long ID = 1L;
    private final static String NAME = "Name";
    private final static String DESCRIPTION = "Description";
    private final static LocalDateTime CREATE_TIME = LocalDateTime.now();
    private final static LocalDateTime MODIFIED_TIME = LocalDateTime.now();


    @Test
    void merge() {

        Mockito.when(securityFilterDataSetFieldMapper.from(Mockito.anyList())).thenReturn(Collections.emptyList());
        Mockito.when(securityFilterDataSetMapper.from(Mockito.anyList())).thenReturn(Collections.emptyList());

        var response = merger.merge(getSecurityFilter(), getSecurityFilterAddRequest());

        Assertions.assertEquals(ID, response.getId());
        Assertions.assertEquals(NAME, response.getName());
        Assertions.assertEquals(DESCRIPTION, response.getDescription());
        Assertions.assertEquals(CREATE_TIME, response.getCreatedDateTime());
        Assertions.assertEquals(MODIFIED_TIME, response.getModifiedDateTime());

        Assertions.assertTrue(response.getDataSets().isEmpty());
        Assertions.assertTrue(response.getFilterRoles().isEmpty());
        Assertions.assertTrue(response.getAuthSecurityFilters().isEmpty());
        Assertions.assertTrue(response.getFieldMappings().isEmpty());

        Assertions.assertNotNull(response.getUser());
        Assertions.assertNotNull(response.getFilterInstance());

        Mockito.verifyNoMoreInteractions(securityFilterDataSetFieldMapper);
    }

    private SecurityFilter getSecurityFilter() {
        return new SecurityFilter()
                .setId(ID)
                .setName("")
                .setDescription("")
                .setFolder(new SecurityFilterFolder())
                .setUser(new User())
                .setFilterInstance(new FilterInstance())
                .setDataSets(Collections.emptyList())
                .setFieldMappings(Collections.emptyList())
                .setFilterRoles(Collections.emptyList())
                .setCreatedDateTime(CREATE_TIME)
                .setModifiedDateTime(MODIFIED_TIME);
    }

    private SecurityFilterAddRequest getSecurityFilterAddRequest() {
        return new SecurityFilterAddRequest()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setOperationType(FilterOperationTypeEnum.IS_BETWEEN)
                .setFilterInstanceId(ID)
                .setFolderId(ID)
                .setDataSets(Collections.singletonList(new SecurityFilterDataSetAddRequest()));
    }

}
