package ru.magnit.magreportbackend.service.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateFolder;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateFolderRole;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateFolderRolePermission;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthority;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.domain.user.Role;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderTypes;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterFieldTypeResponse;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterOperationTypeResponse;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterTemplateFolderResponse;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterTemplateResponse;
import ru.magnit.magreportbackend.dto.response.filtertemplate.FilterTypeResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderNodeResponse;
import ru.magnit.magreportbackend.dto.response.role.RoleFolderResponse;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.mapper.filtertemplate.FilterFieldTypeResponseMapper;
import ru.magnit.magreportbackend.mapper.filtertemplate.FilterOperationTypeResponseMapper;
import ru.magnit.magreportbackend.mapper.filtertemplate.FilterTemplateFolderMapper;
import ru.magnit.magreportbackend.mapper.filtertemplate.FilterTemplateFolderResponseMapper;
import ru.magnit.magreportbackend.mapper.filtertemplate.FilterTemplateFolderRolePermissionMapper;
import ru.magnit.magreportbackend.mapper.filtertemplate.FilterTemplateResponseMapper;
import ru.magnit.magreportbackend.mapper.filtertemplate.FilterTypeResponseMapper;
import ru.magnit.magreportbackend.mapper.filtertemplate.FolderNodeResponseFilterTemplateFolderMapper;
import ru.magnit.magreportbackend.repository.FilterFieldTypeRepository;
import ru.magnit.magreportbackend.repository.FilterOperationTypeRepository;
import ru.magnit.magreportbackend.repository.FilterTemplateFolderRepository;
import ru.magnit.magreportbackend.repository.FilterTemplateFolderRoleRepository;
import ru.magnit.magreportbackend.repository.FilterTemplateRepository;
import ru.magnit.magreportbackend.repository.FilterTypeRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilterTemplateDomainService {

    private final FilterTemplateFolderRepository folderRepository;
    private final FilterTemplateRepository filterTemplateRepository;
    private final FilterOperationTypeRepository filterOperationTypeRepository;
    private final FilterTypeRepository filterTypeRepository;
    private final FilterFieldTypeRepository fieldTypeRepository;
    private final FilterTemplateFolderRoleRepository filterTemplateFolderRoleRepository;

    private final FilterTypeResponseMapper filterTypeResponseMapper;
    private final FilterTemplateFolderMapper filterTemplateFolderMapper;
    private final FilterTemplateResponseMapper filterTemplateResponseMapper;
    private final FilterFieldTypeResponseMapper filterFieldTypeResponseMapper;
    private final FilterOperationTypeResponseMapper filterOperationTypeResponseMapper;
    private final FilterTemplateFolderResponseMapper filterTemplateFolderResponseMapper;
    private final FolderNodeResponseFilterTemplateFolderMapper folderNodeResponseFilterTemplateFolderMapper;
    private final FilterTemplateFolderRolePermissionMapper filterTemplateFolderRolePermissionMapper;

    @Value("${magreport.max-level-hierarchy}")
    Long maxLevel;

    @Transactional
    public FilterTemplateFolderResponse getFolder(Long id) {

        if (id != null) checkFolderExists(id);
        if (id == null) {
            return new FilterTemplateFolderResponse()
                    .setName("root")
                    .setChildFolders(filterTemplateFolderResponseMapper.shallowMap(folderRepository.getAllByParentFolderIsNull()))
                    .setCreated(LocalDateTime.now())
                    .setModified(LocalDateTime.now());
        } else {
            var folder = folderRepository.getReferenceById(id);
            return filterTemplateFolderResponseMapper.from(folder);
        }
    }

    @Transactional
    public FilterTemplateFolderResponse addFolder(FolderAddRequest request) {
        var folder = filterTemplateFolderMapper.from(request);
        folder = folderRepository.saveAndFlush(folder);

        if (folderRepository.checkRingPath(folder.getId()).size() == maxLevel)
            throw new InvalidParametersException("Invalid parent id folder: path is looped");

        return filterTemplateFolderResponseMapper.from(folder);
    }

    @Transactional
    public List<FilterTemplateFolderResponse> getChildFolders(Long parentId) {
        var folders = folderRepository.getAllByParentFolderId(parentId);
        return filterTemplateFolderResponseMapper.from(folders);
    }

    @Transactional
    public FilterTemplateFolderResponse renameFolder(FolderRenameRequest request) {

        checkFolderExists(request.getId());

        var folder = folderRepository.getReferenceById(request.getId());

        folder
                .setName(request.getName())
                .setDescription(request.getDescription());

        folder = folderRepository.saveAndFlush(folder);

        return filterTemplateFolderResponseMapper.from(folder);
    }

    @Transactional
    public void deleteFolder(Long id) {

        checkFolderExists(id);
        checkFolderEmpty(id);

        folderRepository.deleteById(id);
    }

    @Transactional
    public List<RoleFolderResponse> getFoldersPermittedToRole(Long roleId) {
        final var folderRoles = filterTemplateFolderRoleRepository.getAllByRoleId(roleId);

        return folderRoles.stream().map(folderRole -> new RoleFolderResponse(
                folderRole.getFolder().getId(),
                folderRole.getFolder().getName(),
                folderRole.getPermissions().stream()
                        .map(FilterTemplateFolderRolePermission::getAuthority)
                        .max(Comparator.comparing(FolderAuthority::getId))
                        .map(perm -> FolderAuthorityEnum.getById(perm.getId()))
                        .orElse(FolderAuthorityEnum.NONE),
                FolderTypes.FILTER_TEMPLATE
        )).toList();
    }

    @Transactional
    public void addFolderPermittedToRole(List<Long> folders, Long roleId, List<FolderAuthorityEnum> permissions) {

        var folderRoles = folders.stream().map(folder -> {
            var res = new FilterTemplateFolderRole()
                    .setFolder(new FilterTemplateFolder(folder))
                    .setRole(new Role(roleId))
                    .setPermissions(filterTemplateFolderRolePermissionMapper.from(permissions));
            res.getPermissions().forEach(p -> p.setFolderRole(res));
            return res;
        }).toList();

        filterTemplateFolderRoleRepository.saveAll(folderRoles);
    }

    @Transactional
    public void deleteFolderPermittedToRole(List<Long> folderIds, Long roleId) {
        filterTemplateFolderRoleRepository.deleteAllByFolderIdInAndRoleId(folderIds, roleId);
    }

    @Transactional
    public List<FilterOperationTypeResponse> getFilterOperationTypes() {
        var filterOperationTypes = filterOperationTypeRepository.findAll();
        return filterOperationTypeResponseMapper.from(filterOperationTypes);
    }

    @Transactional
    public List<FilterTypeResponse> getFilterTypes() {
        var filterOperationTypes = filterTypeRepository.findAll();
        return filterTypeResponseMapper.from(filterOperationTypes);
    }

    @Transactional
    public List<FilterFieldTypeResponse> getFilterFieldTypes() {
        var filterOperationTypes = fieldTypeRepository.findAll();
        return filterFieldTypeResponseMapper.from(filterOperationTypes);
    }

    @Transactional
    public FilterTemplateResponse getFilterTemplate(Long id) {
        var filterTemplate = filterTemplateRepository.getReferenceById(id);
        return filterTemplateResponseMapper.from(filterTemplate);
    }

    @Transactional
    public List<FolderNodeResponse> getPathToFolder(@NonNull Long folderId) {
        checkFolderExists(folderId);

        return getPath(folderRepository.getReferenceById(folderId));
    }

    private List<FolderNodeResponse> getPath(FilterTemplateFolder folder) {
        var result = new LinkedList<FolderNodeResponse>();

        result.add(folderNodeResponseFilterTemplateFolderMapper.from(folder));

        while (folder.getParentFolder() != null) {
            folder = folder.getParentFolder();
            result.addFirst(folderNodeResponseFilterTemplateFolderMapper.from(folder));
        }

        return result;
    }

    private void checkFolderExists(Long id) {

        if (!isFolderExists(id))
            throw new InvalidParametersException("Folder with id: " + id + " does not exists");
    }

    private boolean isFolderExists(Long id) {

        return folderRepository.existsById(id);
    }

    private void checkFolderEmpty(Long id) {
        if (!isFolderEmpty(id))
            throw new InvalidParametersException("Folder with id: " + id + " is not empty.");
    }

    private boolean isFolderEmpty(Long id) {

        return !folderRepository.existsByParentFolderId(id) &&
                !filterTemplateRepository.existsByFolderId(id);
    }
}
