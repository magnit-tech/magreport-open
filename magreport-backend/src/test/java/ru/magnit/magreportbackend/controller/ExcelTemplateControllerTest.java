package ru.magnit.magreportbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.magnit.magreportbackend.dto.request.exceltemplate.ExcelTemplateGetRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRequest;
import ru.magnit.magreportbackend.dto.response.exceltemplate.ExcelTemplateFolderResponse;
import ru.magnit.magreportbackend.dto.response.exceltemplate.ExcelTemplateResponse;
import ru.magnit.magreportbackend.service.ExcelTemplateService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static ru.magnit.magreportbackend.controller.ExcelTemplateController.*;
import static ru.magnit.magreportbackend.util.Constant.ISO_DATE_TIME_PATTERN;

@ExtendWith(MockitoExtension.class)
class ExcelTemplateControllerTest {

    private final Long ID = 1L;
    private final String NAME = "Test folder";
    private final String DESCRIPTION = "Folder description";
    private final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private final LocalDateTime MODIFIED_TIME = LocalDateTime.now().plusMinutes(2);

    private MockMvc mvc;
    private ObjectMapper objectMapper;
    private DateTimeFormatter formatter;

    @InjectMocks
    private ExcelTemplateController controller;

    @Mock
    private ExcelTemplateService service;

    @BeforeEach
    public void initMock() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(authentication.getName()).thenReturn("TestUser");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        this.mvc = standaloneSetup(this.controller).build();
        this.objectMapper = new ObjectMapper();
        this.formatter = DateTimeFormatter.ofPattern(ISO_DATE_TIME_PATTERN);
    }

    @Test
    void addExcelTemplateFolder() throws Exception{
        when(service.addFolder(any())).thenReturn(getFolderResponse());

        mvc
            .perform(post(EXCEL_TEMPLATE_ADD_FOLDER)
                .accept(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(getFolderAddRequest()))
                .contentType(APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.message", is("")))
            .andExpect(jsonPath("$.data.id", is(ID.intValue())))
            .andExpect(jsonPath("$.data.name", is(NAME)))
            .andExpect(jsonPath("$.data.description", is(DESCRIPTION)))
            .andExpect(jsonPath("$.data.childFolders", hasSize(1)))
            .andExpect(jsonPath("$.data.excelTemplates", hasSize(1)))
            .andExpect(jsonPath("$.data.created", is(CREATED_TIME.format(formatter))))
            .andExpect(jsonPath("$.data.modified", is(MODIFIED_TIME.format(formatter))));

        verify(service).addFolder(any());
        verifyNoMoreInteractions(service);
    }

    private ExcelTemplateFolderResponse getFolderResponse() {
        return new ExcelTemplateFolderResponse()
            .setId(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION)
            .setChildFolders(Collections.singletonList(new ExcelTemplateFolderResponse()))
            .setExcelTemplates(Collections.singletonList(new ExcelTemplateResponse()))
            .setCreated(CREATED_TIME)
            .setModified(MODIFIED_TIME);
    }

    private FolderAddRequest getFolderAddRequest() {
        return new FolderAddRequest()
            .setParentId(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION);
    }

    @Test
    void getExcelTemplateFolder() throws Exception{
        when(service.getFolder(any())).thenReturn(getFolderResponse());

        mvc
            .perform(post(EXCEL_TEMPLATE_GET_FOLDER)
                .accept(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(getFolderRequest()))
                .contentType(APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.message", is("")))
            .andExpect(jsonPath("$.data.id", is(ID.intValue())))
            .andExpect(jsonPath("$.data.name", is(NAME)))
            .andExpect(jsonPath("$.data.description", is(DESCRIPTION)))
            .andExpect(jsonPath("$.data.childFolders", hasSize(1)))
            .andExpect(jsonPath("$.data.excelTemplates", hasSize(1)))
            .andExpect(jsonPath("$.data.created", is(CREATED_TIME.format(formatter))))
            .andExpect(jsonPath("$.data.modified", is(MODIFIED_TIME.format(formatter))));

        verify(service).getFolder(any());
        verifyNoMoreInteractions(service);
    }

    private FolderRequest getFolderRequest() {
        return new FolderRequest()
            .setId(ID);
    }

    @Test
    void renameExcelTemplateFolder() throws Exception{
        when(service.renameFolder(any())).thenReturn(getFolderResponse());

        mvc
            .perform(post(EXCEL_TEMPLATE_RENAME_FOLDER)
                .accept(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(getFolderAddRequest()))
                .contentType(APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.message", is("")))
            .andExpect(jsonPath("$.data.id", is(ID.intValue())))
            .andExpect(jsonPath("$.data.name", is(NAME)))
            .andExpect(jsonPath("$.data.description", is(DESCRIPTION)))
            .andExpect(jsonPath("$.data.childFolders", hasSize(1)))
            .andExpect(jsonPath("$.data.excelTemplates", hasSize(1)))
            .andExpect(jsonPath("$.data.created", is(CREATED_TIME.format(formatter))))
            .andExpect(jsonPath("$.data.modified", is(MODIFIED_TIME.format(formatter))));

        verify(service).renameFolder(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void addExcelTemplate() throws Exception{
        when(service.addExcelTemplate(anyString(), any())).thenReturn(getExcelTemplateResponse());

        MockMultipartFile file = new MockMultipartFile("file", "testPath", MULTIPART_FORM_DATA_VALUE, "getByte".getBytes());


        mvc
            .perform(MockMvcRequestBuilders.multipart(EXCEL_TEMPLATE_ADD)
                .file(file)
                .param("params", " ")
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .accept(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.message", is("")))
            .andExpect(jsonPath("$.data.excelTemplateId", is(ID.intValue())))
            .andExpect(jsonPath("$.data.name", is(NAME)))
            .andExpect(jsonPath("$.data.description", is(DESCRIPTION)))
            .andExpect(jsonPath("$.data.created", is(CREATED_TIME.format(formatter))))
            .andExpect(jsonPath("$.data.modified", is(MODIFIED_TIME.format(formatter))));

        verify(service).addExcelTemplate(anyString(), any());
        verifyNoMoreInteractions(service);
    }

    @Test
    void getExcelTemplateFile() throws Exception {
        byte[] fileContent = "File content".getBytes();
        when(service.getExcelTemplateFile(any())).thenReturn(fileContent);

        mvc
                .perform(post(EXCEL_TEMPLATE_GET_FILE)
                        .accept(APPLICATION_EXCEL_XLSM_FILE)
                        .content(objectMapper.writeValueAsString(new ExcelTemplateGetRequest().setId(ID)))
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_EXCEL_XLSM_FILE))
                .andExpect(content().bytes(fileContent));

        verify(service).getExcelTemplateFile(any());
        verifyNoMoreInteractions(service);

    }

    private ExcelTemplateResponse getExcelTemplateResponse() {
        return new ExcelTemplateResponse()
            .setId(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION)
            .setCreated(CREATED_TIME)
            .setModified(MODIFIED_TIME);
    }
}