package ru.magnit.magreportbackend.mapper.dataset;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolder;
import ru.magnit.magreportbackend.domain.dataset.DataSetType;
import ru.magnit.magreportbackend.domain.datasource.DataSource;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.response.user.UserResponse;
import ru.magnit.magreportbackend.mapper.asm.AsmSecurityResponseMapper;
import ru.magnit.magreportbackend.mapper.auth.UserResponseMapper;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterInstanceDependenciesResponseMapper;
import ru.magnit.magreportbackend.mapper.report.ReportResponseMapper;
import ru.magnit.magreportbackend.mapper.securityfilter.SecurityFilterResponseMapper;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataSetDependenciesResponseMapperTest {
    @Mock
    private UserResponseMapper userResponseMapper;

    @Mock
    private ReportResponseMapper reportResponseMapper;

    @Mock
    private FilterInstanceDependenciesResponseMapper filterInstanceDependenciesResponseMapper;

    @Mock
    private SecurityFilterResponseMapper securityFilterResponseMapper;

    @Mock
    private AsmSecurityResponseMapper asmSecurityResponseMapper;

    @Mock
    private FolderNodeResponseDataSetFolderMapper folderMapper;

    @InjectMocks
    private DataSetDependenciesResponseMapper mapper;


    private final static Long ID = 1L;
    private final static String NAME = "Name";
    private final static String DESCRIPTION = "Description";
    private final static String SCHEMA = "Schema";
    private final static String OBJECT = "Object";
    private final static String CATALOG = "CATALOG";
    private final static LocalDateTime CREATE_TIME = LocalDateTime.now();
    private final static LocalDateTime MODIFIED_TIME = LocalDateTime.now();

    @Test
    void from() {
        when(userResponseMapper.from(any(User.class))).thenReturn(new UserResponse());
        when(reportResponseMapper.from(anyList())).thenReturn(Collections.emptyList());
        when(filterInstanceDependenciesResponseMapper.from(anyList())).thenReturn(Collections.emptyList());
        when(securityFilterResponseMapper.from(anyList())).thenReturn(Collections.emptyList());
        when(asmSecurityResponseMapper.from(anyList())).thenReturn(Collections.emptyList());

        var response = mapper.from(getDataset());

        assertEquals(ID, response.getId());
        assertEquals(ID, response.getTypeId());
        assertEquals(new UserResponse(), response.getCreator());
        assertEquals(SCHEMA, response.getSchemaName());
        assertEquals(OBJECT, response.getObjectName());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertFalse(response.getValid());
        assertEquals(0, response.getFilterInstances().size());
        assertEquals(0, response.getSecurityFilters().size());
        assertEquals(0, response.getAsmSecurities().size());
        assertEquals(CREATE_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(userResponseMapper).from(any(User.class));
        verify(reportResponseMapper).from(anyList());
        verify(filterInstanceDependenciesResponseMapper).from(anyList());
        verify(securityFilterResponseMapper).from(anyList());
        verify(asmSecurityResponseMapper).from(anyList());
        verifyNoMoreInteractions(userResponseMapper);

    }

    private DataSet getDataset() {
        return new DataSet()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setSchemaName(SCHEMA)
                .setObjectName(OBJECT)
                .setCatalogName(CATALOG)
                .setType(new DataSetType().setId(ID))
                .setFolder(new DataSetFolder().setParentFolder(new DataSetFolder()))
                .setUser(new User())
                .setFields(Collections.emptyList())
                .setFilterInstances(Collections.emptyList())
                .setReports(Collections.emptyList())
                .setSecurityFilterDataSets(Collections.emptyList())
                .setExternalAuthSources(Collections.emptyList())
                .setDataSource(new DataSource())
                .setCreatedDateTime(CREATE_TIME)
                .setModifiedDateTime(MODIFIED_TIME);
    }
}
