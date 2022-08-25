package ru.magnit.magreportbackend.service.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplate;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateFolder;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterTemplateFolderResponse;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterTemplateResponse;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.mapper.filtertemplate.FilterFieldTypeResponseMapper;
import ru.magnit.magreportbackend.mapper.filtertemplate.FilterOperationTypeResponseMapper;
import ru.magnit.magreportbackend.mapper.filtertemplate.FilterTemplateFolderMapper;
import ru.magnit.magreportbackend.mapper.filtertemplate.FilterTemplateFolderResponseMapper;
import ru.magnit.magreportbackend.mapper.filtertemplate.FilterTemplateResponseMapper;
import ru.magnit.magreportbackend.mapper.filtertemplate.FilterTypeResponseMapper;
import ru.magnit.magreportbackend.repository.FilterFieldTypeRepository;
import ru.magnit.magreportbackend.repository.FilterOperationTypeRepository;
import ru.magnit.magreportbackend.repository.FilterTemplateFolderRepository;
import ru.magnit.magreportbackend.repository.FilterTemplateRepository;
import ru.magnit.magreportbackend.repository.FilterTypeRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilterTemplateDomainServiceTest {

    private final Long ID = 1L;
    private final String NAME = "Test";
    private final String DESCRIPTION = "description";
    private final LocalDateTime CREATED_TIME = LocalDateTime.now();
    private final LocalDateTime MODIFIED_TIME = LocalDateTime.now().plusMinutes(2);

    @InjectMocks
    private FilterTemplateDomainService domainService;

    @Mock
    private FilterTemplateFolderRepository folderRepository;

    @Mock
    private FilterTemplateRepository filterTemplateRepository;

    @Mock
    private FilterOperationTypeRepository filterOperationTypeRepository;

    @Mock
    private FilterTypeRepository filterTypeRepository;

    @Mock
    private FilterFieldTypeRepository fieldTypeRepository;

    @Mock
    private FilterTemplateFolderResponseMapper filterTemplateFolderResponseMapper;

    @Mock
    private FilterTemplateFolderMapper filterTemplateFolderMapper;

    @Mock
    private FilterOperationTypeResponseMapper filterOperationTypeResponseMapper;

    @Mock
    private FilterTypeResponseMapper filterTypeResponseMapper;

    @Mock
    private FilterFieldTypeResponseMapper filterFieldTypeResponseMapper;

    @Mock
    private FilterTemplateResponseMapper filterTemplateResponseMapper;

    @Test
    void getFolder() {
        when(folderRepository.existsById(anyLong())).thenReturn(true);
        when(folderRepository.getReferenceById(anyLong())).thenReturn(new FilterTemplateFolder());
        when(filterTemplateFolderResponseMapper.from((FilterTemplateFolder) any())).thenReturn(getFolderResponse());

        FilterTemplateFolderResponse response = domainService.getFolder(ID);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getFilterTemplates());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(folderRepository).existsById(anyLong());
        verify(folderRepository).getReferenceById(anyLong());
        verify(filterTemplateFolderResponseMapper).from((FilterTemplateFolder) any());
        verifyNoMoreInteractions(folderRepository, filterTemplateFolderResponseMapper);

        Mockito.reset(folderRepository, filterTemplateFolderResponseMapper);

        when(filterTemplateFolderResponseMapper.shallowMap(anyList())).thenReturn(Collections.emptyList());
        when(folderRepository.getAllByParentFolderIsNull()).thenReturn(Collections.emptyList());

        assertNotNull(domainService.getFolder(null));


        verify(filterTemplateFolderResponseMapper).shallowMap(anyList());
        verify(folderRepository).getAllByParentFolderIsNull();
        verifyNoMoreInteractions(folderRepository, filterTemplateFolderResponseMapper);

    }

    @Test
    void addFolder() {

        when(filterTemplateFolderMapper.from((FolderAddRequest) any())).thenReturn(new FilterTemplateFolder());
        when(folderRepository.saveAndFlush(any())).thenReturn(new FilterTemplateFolder().setId(ID));
        when(filterTemplateFolderResponseMapper.from((FilterTemplateFolder) any())).thenReturn(getFolderResponse());
        when(folderRepository.checkRingPath(anyLong())).thenReturn(Collections.emptyList());

        ReflectionTestUtils.setField(domainService, "maxLevel", 128L);

        FilterTemplateFolderResponse response = domainService.addFolder(getFolderAddRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getFilterTemplates());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());


        verify(filterTemplateFolderMapper).from((FolderAddRequest) any());
        verify(filterTemplateFolderResponseMapper).from((FilterTemplateFolder) any());
        verify(folderRepository).saveAndFlush(any());
        verify(folderRepository).checkRingPath(anyLong());
        verifyNoMoreInteractions(folderRepository, filterTemplateFolderResponseMapper, filterTemplateFolderMapper);
    }

    @Test
    void getChildFolders() {
        when(folderRepository.getAllByParentFolderId(ID)).thenReturn(Collections.singletonList(new FilterTemplateFolder()));

        when(filterTemplateFolderResponseMapper.from(Collections.singletonList(any()))).thenReturn(Collections.singletonList(getFolderResponse()));

        List<FilterTemplateFolderResponse> responseList = domainService.getChildFolders(ID);

        assertNotNull(responseList);

        FilterTemplateFolderResponse response = responseList.get(0);

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getFilterTemplates());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(folderRepository).getAllByParentFolderId(any());
        verifyNoMoreInteractions(folderRepository);
        verify(filterTemplateFolderResponseMapper).from(Collections.singletonList(any()));
        verifyNoMoreInteractions(filterTemplateFolderResponseMapper);
    }

    @Test
    void renameFolder() {
        when(folderRepository.existsById(anyLong())).thenReturn(true);
        when(folderRepository.getReferenceById(anyLong())).thenReturn(new FilterTemplateFolder());
        when(folderRepository.saveAndFlush(any())).thenReturn(new FilterTemplateFolder());
        when(filterTemplateFolderResponseMapper.from((FilterTemplateFolder) any())).thenReturn(getFolderResponse());

        FilterTemplateFolderResponse response = domainService.renameFolder(getFolderRenameRequest());

        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(DESCRIPTION, response.getDescription());
        assertNotNull(response.getChildFolders());
        assertNotNull(response.getFilterTemplates());
        assertEquals(CREATED_TIME, response.getCreated());
        assertEquals(MODIFIED_TIME, response.getModified());

        verify(folderRepository).getReferenceById(anyLong());
        verify(folderRepository).existsById(anyLong());
        verify(folderRepository).saveAndFlush(any());
        verifyNoMoreInteractions(folderRepository);
        verify(filterTemplateFolderResponseMapper).from((FilterTemplateFolder) any());
        verifyNoMoreInteractions(filterTemplateFolderResponseMapper);
    }

    @Test
    void deleteFolder() {
        when(folderRepository.existsById(anyLong())).thenReturn(true);
        when(folderRepository.existsByParentFolderId(anyLong())).thenReturn(false);
        when(filterTemplateRepository.existsByFolderId(anyLong())).thenReturn(false);
        domainService.deleteFolder(ID);

        verify(folderRepository).deleteById(anyLong());
        verifyNoMoreInteractions(folderRepository);
    }

    @Test
    void getFilterOperationTypes() {

        when(filterOperationTypeRepository.findAll()).thenReturn(Collections.emptyList());
        when(filterOperationTypeResponseMapper.from(anyList())).thenReturn(Collections.emptyList());

        assertNotNull(domainService.getFilterOperationTypes());

        verify(filterOperationTypeRepository).findAll();

        verify(filterOperationTypeResponseMapper).from(anyList());
        verifyNoMoreInteractions(filterOperationTypeRepository, filterOperationTypeResponseMapper);

    }

    @Test
    void getFilterTypes() {

        when(filterTypeRepository.findAll()).thenReturn(Collections.emptyList());
        when(filterTypeResponseMapper.from(anyList())).thenReturn(Collections.emptyList());

        assertNotNull(domainService.getFilterTypes());

        verify(filterTypeRepository).findAll();
        verify(filterTypeResponseMapper).from(anyList());
        verifyNoMoreInteractions(filterOperationTypeRepository);
    }

    @Test
    void getFilterFieldTypes() {

        when(fieldTypeRepository.findAll()).thenReturn(Collections.emptyList());
        when(filterFieldTypeResponseMapper.from(anyList())).thenReturn(Collections.emptyList());

        assertNotNull(domainService.getFilterFieldTypes());

        verify(fieldTypeRepository).findAll();
        verify(filterFieldTypeResponseMapper).from(anyList());
        verifyNoMoreInteractions(fieldTypeRepository, filterFieldTypeResponseMapper);
    }

    @Test
    void getFilterTemplate() {

        when(filterTemplateRepository.getReferenceById(anyLong())).thenReturn(new FilterTemplate());
        when(filterTemplateResponseMapper.from(any(FilterTemplate.class))).thenReturn(new FilterTemplateResponse());

        assertNotNull(domainService.getFilterTemplate(anyLong()));

        verify(filterTemplateRepository).getReferenceById(anyLong());
        verify(filterTemplateResponseMapper).from(any(FilterTemplate.class));
        verifyNoMoreInteractions(filterTemplateRepository, filterTemplateResponseMapper);
    }

    @Test
    void checkFolderExists() {

        when(folderRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(InvalidParametersException.class, () ->
                ReflectionTestUtils.invokeMethod(domainService, "checkFolderExists", ID));

        verify(folderRepository).existsById(anyLong());
        verifyNoMoreInteractions(folderRepository);
    }

    @Test
    void checkFolderEmpty() {

        when(filterTemplateRepository.existsByFolderId(anyLong())).thenReturn(true);

        assertThrows(InvalidParametersException.class, () ->
                ReflectionTestUtils.invokeMethod(domainService, "checkFolderEmpty", ID));

        verify(filterTemplateRepository).existsByFolderId(anyLong());
        verifyNoMoreInteractions(filterTemplateRepository);

    }

    private FilterTemplateFolderResponse getFolderResponse() {
        return new FilterTemplateFolderResponse()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setChildFolders(Collections.singletonList(new FilterTemplateFolderResponse()))
                .setFilterTemplates(Collections.singletonList(new FilterTemplateResponse()))
                .setCreated(CREATED_TIME)
                .setModified(MODIFIED_TIME);
    }

    private FolderAddRequest getFolderAddRequest() {
        return new FolderAddRequest()
                .setParentId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION);
    }

    private FolderRenameRequest getFolderRenameRequest() {
        return new FolderRenameRequest()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION);
    }


}
