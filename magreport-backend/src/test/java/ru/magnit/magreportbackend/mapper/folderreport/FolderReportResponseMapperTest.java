package ru.magnit.magreportbackend.mapper.folderreport;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.domain.folderreport.FolderReport;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.response.user.UserResponse;
import ru.magnit.magreportbackend.mapper.auth.UserResponseMapper;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class FolderReportResponseMapperTest {

    @Mock
    private UserResponseMapper userResponseMapper;

    @InjectMocks
    private FolderReportResponseMapper mapper;

    private final static Long ID = 1L;
    private final static String NAME = "Name";
    private final static String DESCRIPTION = "Description";
    private final static String URL = "URL";
    private final static LocalDateTime CREATE_TIME = LocalDateTime.now();
    private final static LocalDateTime MODIFIED_TIME = LocalDateTime.now();


    @Test
    void from() {

        when(userResponseMapper.from(any(User.class))).thenReturn(new UserResponse());

        var response = mapper.from(getFolderReport());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(CREATE_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());
        assertEquals(URL, response.getRequirementsLink());
        assertTrue(response.getIsValid());

        verifyNoMoreInteractions(userResponseMapper);

    }

    @Test
    void fromList() {
        when(userResponseMapper.from(any(User.class))).thenReturn(new UserResponse());

        var responses = mapper.from(Collections.singletonList(getFolderReport()));
        assertEquals(1, responses.size());

        var response = responses.get(0);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(CREATE_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());
        assertEquals(URL, response.getRequirementsLink());
        assertTrue(response.getIsValid());

        verifyNoMoreInteractions(userResponseMapper);

    }

    private FolderReport getFolderReport() {
        return new FolderReport()
                .setUser(new User())
                .setReport(
                        new Report()
                                .setId(ID)
                                .setName(NAME)
                                .setDescription(DESCRIPTION)
                                .setDataSet(
                                        new DataSet()
                                                .setId(ID)
                                                .setFields(Collections.emptyList()))
                                .setRequirementsLink(URL)
                                .setCreatedDateTime(CREATE_TIME)
                                .setModifiedDateTime(MODIFIED_TIME)

                );
    }
}
