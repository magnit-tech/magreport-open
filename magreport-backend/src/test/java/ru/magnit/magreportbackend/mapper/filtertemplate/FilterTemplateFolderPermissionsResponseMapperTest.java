package ru.magnit.magreportbackend.mapper.filtertemplate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateFolder;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateFolderRole;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterTemplateFolderResponse;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class FilterTemplateFolderPermissionsResponseMapperTest {

    private static final List<FilterTemplateFolderRole> FOLDER_ROLES = Collections.singletonList(null);

    @Mock
    private FilterTemplateFolderResponseMapper folderResponseMapper;

    @Mock
    private RolePermissionResponseMapperFilterTemplate responseMapper;

    @InjectMocks
    private FilterTemplateFolderPermissionsResponseMapper mapper;


    @Test
    void from() {

        final var source = spy(getFilterTemplateFolder());

        when(folderResponseMapper.shallowMap(any(FilterTemplateFolder.class))).thenReturn(new FilterTemplateFolderResponse());
        when(responseMapper.from(anyList())).thenReturn(Collections.singletonList(null));

        final var result = mapper.from(source);

        assertNotNull(result.folder());
        assertNotNull(result.rolePermissions());

        verify(source).getFolderRoles();
        verify(responseMapper).from(anyList());
        verify(folderResponseMapper).shallowMap(any(FilterTemplateFolder.class));
        verifyNoMoreInteractions(source, responseMapper, folderResponseMapper);
    }

    private FilterTemplateFolder getFilterTemplateFolder() {
        return new FilterTemplateFolder()
                .setFolderRoles(FOLDER_ROLES);
    }
}