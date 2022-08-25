package ru.magnit.magreportbackend.mapper.securityfilter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityFilterFolderMapperTest {

    private static final Long PARENT_ID = 1L;
    private static final String NAME = "name";
    private static final String DESCRIPTION = "desc";
    @InjectMocks
    private SecurityFilterFolderMapper mapper;

    @Test
    void from() {

        final var source = spy(getFolderAddRequest());

        var result = mapper.from(source);

        assertEquals(PARENT_ID, result.getParentFolder().getId());
        assertEquals(NAME, result.getName());
        assertEquals(DESCRIPTION, result.getDescription());

        verify(source, times(2)).getParentId();
        verify(source).getName();
        verify(source).getDescription();

        verifyNoMoreInteractions(source);

        //parent is null
        source.setParentId(null);
        Mockito.reset(source);

        result = mapper.from(source);

        assertNull(result.getParentFolder());
        assertEquals(NAME, result.getName());
        assertEquals(DESCRIPTION, result.getDescription());

        verify(source).getParentId();
        verify(source).getName();
        verify(source).getDescription();

        verifyNoMoreInteractions(source);
    }

    private FolderAddRequest getFolderAddRequest() {
        return new FolderAddRequest()
                .setParentId(PARENT_ID)
                .setName(NAME)
                .setDescription(DESCRIPTION);
    }
}