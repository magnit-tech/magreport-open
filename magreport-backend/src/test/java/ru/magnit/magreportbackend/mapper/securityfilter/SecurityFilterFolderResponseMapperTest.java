package ru.magnit.magreportbackend.mapper.securityfilter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterOperationTypeEnum;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilter;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterFolder;
import ru.magnit.magreportbackend.dto.response.filterinstance.FilterInstanceResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterFolderResponse;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityFilterFolderResponseMapperTest {

    private static final Long ID = 1L;
    private static final String NAME = "name";
    private static final String DESCRIPTION = "desc";
    private static final Long FOLDER_ID = 5L;
    private static final SecurityFilterFolder PARENT_FOLDER = new SecurityFilterFolder(FOLDER_ID);
    private static final List<SecurityFilterFolder> CHILD_FOLDERS = Collections.singletonList(new SecurityFilterFolder());
    private static final List<SecurityFilter> FILTERS = Collections.singletonList(new SecurityFilter());
    private static final LocalDateTime CREATED_DATE = LocalDateTime.now().minusDays(5L);
    private static final LocalDateTime MODIFIED_DATE = LocalDateTime.now();

    @Mock
    private SecurityFilterResponseMapper securityFilterResponseMapper;

    @InjectMocks
    @Spy
    private SecurityFilterFolderResponseMapper mapper;

    @Test
    void from() {

        final var source = spy(getSecurityFilterFolder());

        when(securityFilterResponseMapper.from(anyList())).thenReturn(Collections.singletonList(getSecurityFilterResponse()));
        doReturn(Collections.singletonList(new SecurityFilterFolderResponse())).when(mapper).shallowMap(anyList());

        final var result = mapper.from(source);

        assertEquals(ID, result.getId());
        assertEquals(NAME, result.getName());
        assertEquals(DESCRIPTION, result.getDescription());
        assertEquals(FOLDER_ID, result.getParentId());
        assertNotNull(result.getChildFolders());
        assertEquals(1, result.getChildFolders().size());
        assertNotNull(result.getSecurityFilters());
        assertEquals(1, result.getSecurityFilters().size());
        assertEquals(FolderAuthorityEnum.NONE, result.getAuthority());
        assertEquals(CREATED_DATE, result.getCreated());
        assertEquals(MODIFIED_DATE, result.getModified());

        verify(source).getId();
        verify(source, times(2)).getParentFolder();
        verify(source).getName();
        verify(source).getDescription();
        verify(source).getChildFolders();
        verify(source).getFilters();
        verify(source).getCreatedDateTime();
        verify(source).getModifiedDateTime();
        verify(securityFilterResponseMapper).from(anyList());

        verifyNoMoreInteractions(source, securityFilterResponseMapper);
    }

    @Test
    void shallowMap() {

        final var source = spy(getSecurityFilterFolder());

        final var result = mapper.shallowMap(source);

        assertEquals(ID, result.getId());
        assertEquals(NAME, result.getName());
        assertEquals(DESCRIPTION, result.getDescription());
        assertEquals(FOLDER_ID, result.getParentId());
        assertNotNull(result.getChildFolders());
        assertEquals(0, result.getChildFolders().size());
        assertNotNull(result.getSecurityFilters());
        assertEquals(0, result.getSecurityFilters().size());
        assertEquals(FolderAuthorityEnum.NONE, result.getAuthority());
        assertEquals(CREATED_DATE, result.getCreated());
        assertEquals(MODIFIED_DATE, result.getModified());

        verify(source).getId();
        verify(source, times(2)).getParentFolder();
        verify(source).getName();
        verify(source).getDescription();
        verify(source).getCreatedDateTime();
        verify(source).getModifiedDateTime();

        verifyNoMoreInteractions(source, securityFilterResponseMapper);
    }

    private SecurityFilterFolder getSecurityFilterFolder() {
        return new SecurityFilterFolder()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setParentFolder(PARENT_FOLDER)
                .setChildFolders(CHILD_FOLDERS)
                .setFilters(FILTERS)
                .setCreatedDateTime(CREATED_DATE)
                .setModifiedDateTime(MODIFIED_DATE);
    }

    private SecurityFilterResponse getSecurityFilterResponse() {
        return new SecurityFilterResponse(1L,
                new FilterInstanceResponse(),
                FilterOperationTypeEnum.IS_BETWEEN,
                "name",
                "desc",
                Collections.emptyList(),
                Collections.emptyList(),
                "user",
                LocalDateTime.now().minusDays(5L),
                LocalDateTime.now(),
                Collections.emptyList());
    }
}