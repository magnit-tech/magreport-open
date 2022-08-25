package ru.magnit.magreportbackend.service.domain;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolder;
import ru.magnit.magreportbackend.dto.inner.UserView;
import ru.magnit.magreportbackend.dto.request.filterinstance.LikenessType;
import ru.magnit.magreportbackend.dto.request.folder.FolderSearchRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderTypes;
import ru.magnit.magreportbackend.mapper.dataset.DataSetResponseMapper;
import ru.magnit.magreportbackend.mapper.folderentitty.FolderNodeResponseFolderEntityMapper;
import ru.magnit.magreportbackend.repository.DataSetFolderRepository;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FolderEntitySearchDomainServiceTest {

    @Mock
    private DataSetFolderRepository dataSetFolderRepository;

    @Mock
    private DataSetResponseMapper mapper;

    @Mock
    private FolderNodeResponseFolderEntityMapper folderNodeResponseFolderEntityMapper;

    @Mock
    private UserDomainService userDomainService;

    @Mock
    private FolderPermissionsDomainService folderPermissionsDomainService;

    @InjectMocks
    private FolderEntitySearchDomainService domainService;

    private final static String NAME = "name";
    private final static String DESCRIPTION = "description";
    private final static Long ID = 1L;


    @Test
    void search() {

        when(dataSetFolderRepository.getReferenceById(anyLong())).thenReturn(getFolder());
        when(userDomainService.getCurrentUser()).thenReturn(new UserView().setName(NAME));


        assertNotNull(domainService.search(getFolderSearchRequest(1L), dataSetFolderRepository, mapper, FolderTypes.DATASET));

        verify(dataSetFolderRepository).getReferenceById(anyLong());
        verifyNoMoreInteractions(mapper, dataSetFolderRepository);

        Mockito.reset(mapper, dataSetFolderRepository);

        when(dataSetFolderRepository.findAll()).thenReturn(Collections.singletonList(getFolder()));

        assertNotNull(domainService.search(getFolderSearchRequest(null), dataSetFolderRepository, mapper, FolderTypes.DATASET));

        verify(dataSetFolderRepository).findAll();
        verifyNoMoreInteractions(mapper, dataSetFolderRepository);

    }


    private FolderSearchRequest getFolderSearchRequest(Long idRootFolder) {
        return new FolderSearchRequest()
                .setRootFolderId(idRootFolder)
                .setSearchString("")
                .setRecursive(true)
                .setLikenessType(LikenessType.CONTAINS);
    }

    private DataSetFolder getFolder() {
        return new DataSetFolder()
                .setId(ID)
                .setChildElements(
                        Collections.singletonList(new DataSet()
                                .setName(NAME)
                                .setDescription(DESCRIPTION)))
                .setName(NAME)
                .setDescription(DESCRIPTION);
    }
}
