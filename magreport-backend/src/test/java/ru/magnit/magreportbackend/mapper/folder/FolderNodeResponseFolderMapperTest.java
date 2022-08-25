package ru.magnit.magreportbackend.mapper.folder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.folderreport.Folder;
import ru.magnit.magreportbackend.domain.folderreport.FolderReport;
import ru.magnit.magreportbackend.domain.folderreport.FolderRole;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class FolderNodeResponseFolderMapperTest {

    private static final long ID = 1L;
    private static final String NAME = "Folder name";
    private static final String DESCRIPTION = "Folder description";
    private static final long PARENT_FOLDER_ID = 2L;
    private static final long FOLDER_REPORT_ID = 3L;
    private static final long FOLDER_ROLE_ID = 4L;
    private static final long CHILD_FOLDER_ID = 5L;
    private static final LocalDateTime CREATED_DATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFIED_DATE_TIME = CREATED_DATE_TIME.plusSeconds(2L);

    @InjectMocks
    private FolderNodeResponseFolderMapper mapper;

    @Test
    void from() {
        var response = mapper.from(getFolder());

        assertEquals(ID, response.id());
        assertEquals(NAME, response.name());
        assertEquals(DESCRIPTION, response.description());
    }

    private Folder getFolder() {
        return new Folder()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setParentFolder(new Folder(PARENT_FOLDER_ID))
                .setFolderReports(Collections.singletonList(new FolderReport(FOLDER_REPORT_ID)))
                .setFolderRoles(Collections.singletonList(new FolderRole(FOLDER_ROLE_ID)))
                .setChildFolders(Collections.singletonList(new Folder(CHILD_FOLDER_ID)))
                .setCreatedDateTime(CREATED_DATE_TIME)
                .setModifiedDateTime(MODIFIED_DATE_TIME);
    }
}