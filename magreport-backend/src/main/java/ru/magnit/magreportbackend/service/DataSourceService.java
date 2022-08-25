package ru.magnit.magreportbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.domain.user.SystemRoles;
import ru.magnit.magreportbackend.dto.inner.RoleView;
import ru.magnit.magreportbackend.dto.request.ChangeParentFolderRequest;
import ru.magnit.magreportbackend.dto.request.datasource.DataSourceAddRequest;
import ru.magnit.magreportbackend.dto.request.datasource.DataSourceCheckRequest;
import ru.magnit.magreportbackend.dto.request.datasource.DataSourceObjectFieldsRequest;
import ru.magnit.magreportbackend.dto.request.datasource.DataSourceRequest;
import ru.magnit.magreportbackend.dto.request.datasource.DataSourceSchemaObjectsRequest;
import ru.magnit.magreportbackend.dto.request.datasource.DataSourceSchemasRequest;
import ru.magnit.magreportbackend.dto.request.folder.CopyFolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderSearchRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderTypes;
import ru.magnit.magreportbackend.dto.request.folderreport.FolderPermissionSetRequest;
import ru.magnit.magreportbackend.dto.request.folderreport.RoleAddPermissionRequest;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceDependenciesResponse;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceFolderResponse;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceObjectResponse;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceResponse;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceTypeResponse;
import ru.magnit.magreportbackend.dto.response.datasource.ObjectFieldResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderRoleResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderSearchResponse;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.mapper.datasource.DataSourceResponseMapper;
import ru.magnit.magreportbackend.repository.DataSourceFolderRepository;
import ru.magnit.magreportbackend.service.dao.ConnectionPoolManager;
import ru.magnit.magreportbackend.service.domain.DataSourceDomainService;
import ru.magnit.magreportbackend.service.domain.FolderEntitySearchDomainService;
import ru.magnit.magreportbackend.service.domain.FolderPermissionsDomainService;
import ru.magnit.magreportbackend.service.domain.UserDomainService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DataSourceService {

    private final DataSourceDomainService domainService;
    private final MetaDataService metaDataService;
    private final UserDomainService userDomainService;
    private final FolderPermissionsDomainService folderPermissionsDomainService;
    private final ConnectionPoolManager connectionPoolManager;
    private final FolderEntitySearchDomainService folderEntitySearchDomainService;
    private final DataSourceFolderRepository dataSourceFolderRepository;
    private final DataSourceResponseMapper dataSourceResponseMapper;
    private final PermissionCheckerSystem permissionCheckerSystem;

    public DataSourceFolderResponse getFolder(FolderRequest request) {

        final var folderResponse = domainService.getFolder(request.getId());

        final var folderIds = folderResponse.getChildFolders().stream()
            .map(DataSourceFolderResponse::getId)
            .collect(Collectors.toList());
        folderIds.add(folderResponse.getId());

        final var userRoles = userDomainService.getUserRoles(userDomainService.getCurrentUser().getName(), null).stream().map(RoleView::getId).toList();
        final var foldersPermissions = folderPermissionsDomainService.getDataSourceFolderPermissionsForRoles(folderIds, userRoles).stream().collect(Collectors.toMap(FolderRoleResponse::getFolderId, FolderRoleResponse::getAuthority));

        folderResponse.setAuthority(foldersPermissions.getOrDefault(folderResponse.getId(), FolderAuthorityEnum.NONE));
        folderResponse.getChildFolders()
                .forEach(folder -> folder.setAuthority(foldersPermissions.getOrDefault(folder.getId(), FolderAuthorityEnum.NONE)));

        if (userRoles.contains(SystemRoles.ADMIN.getId())) {
            folderResponse.setAuthority(FolderAuthorityEnum.WRITE);
            folderResponse.getChildFolders().forEach(folder -> folder.setAuthority(FolderAuthorityEnum.WRITE));
            return folderResponse;
        }

        if (folderResponse.getParentId() != null && folderResponse.getAuthority() == FolderAuthorityEnum.NONE)
            return null;

        folderResponse.getChildFolders().removeIf(folder -> folder.getAuthority() == FolderAuthorityEnum.NONE);

        return folderResponse;
    }

    public DataSourceFolderResponse addFolder(FolderAddRequest request) {
        final var currentFolderPermissions = request.getParentId() == null ?
                null :
                folderPermissionsDomainService.getDataSourceFolderPermissions(request.getParentId());

        final var result = domainService.addFolder(request);

        if (currentFolderPermissions != null) {
            folderPermissionsDomainService.setDataSourceFolderPermissions(
                    new FolderPermissionSetRequest()
                            .setFolderId(result.getId())
                            .setRoles(currentFolderPermissions
                                    .rolePermissions()
                                    .stream()
                                    .map(rfp -> new RoleAddPermissionRequest()
                                            .setRoleId(rfp.role().getId())
                                            .setPermissions(rfp.permissions()))
                                    .toList()
                            ));
        }

        return domainService.getFolder(result.getId());
    }

    public List<DataSourceFolderResponse> getChildFolders(FolderRequest request) {

        return domainService.getChildFolders(request.getId());
    }

    public DataSourceFolderResponse renameFolder(FolderRenameRequest request) {

        return domainService.renameFolder(request);
    }

    public void deleteFolder(FolderRequest request) {

        domainService.deleteFolder(request.getId());
    }

    public DataSourceResponse addDataSource(DataSourceAddRequest request) {

        final var currentUser = userDomainService.getCurrentUser();
        var dataSourceId = domainService.addDataSource(currentUser, request);

        return domainService.getDataSource(dataSourceId);
    }

    public DataSourceResponse editDataSource(DataSourceAddRequest request) {
        final var dataSourceData = domainService.getDataSourceView(request.getId());
        connectionPoolManager.deleteDataSource(dataSourceData);
        return domainService.editDataSource(request);
    }

    public void deleteDataSource(DataSourceRequest request) {

        domainService.deleteDataSource(request.getId());
    }

    public DataSourceResponse getDataSource(DataSourceRequest request) {

        return domainService.getDataSource(request.getId());
    }

    public List<String> getCatalogs(DataSourceRequest request) {

        return metaDataService.getCatalogs(domainService.getDataSourceView(request.getId()));
    }

    public List<String> getSchemas(DataSourceSchemasRequest request) {

        return metaDataService.getSchemas(
                domainService.getDataSourceView(request.getId()),
                request.getCatalogName()
        );
    }

    public List<DataSourceObjectResponse> getSchemaObjects(DataSourceSchemaObjectsRequest request) {

        return metaDataService.getSchemaObjects(
                domainService.getDataSourceView(request.getId()),
                request.getCatalogName(),
                request.getSchemaName(),
                request.getObjectType()
        );
    }

    public List<ObjectFieldResponse> getObjectFields(DataSourceObjectFieldsRequest request) {

        return metaDataService.getObjectFields(
                domainService.getDataSourceView(request.getId()),
                request.getCatalogName(),
                request.getSchemaName(),
                request.getObjectName()
        );
    }

    public List<DataSourceObjectResponse> getSchemaProcedures(DataSourceSchemaObjectsRequest request) {

        return metaDataService.getProcedures(
                domainService.getDataSourceView(request.getId()),
                request.getCatalogName(),
                request.getSchemaName()
        );
    }

    public List<ObjectFieldResponse> getSchemaProcedureFields(DataSourceObjectFieldsRequest request) {

        return metaDataService.getProcedureFields(
                domainService.getDataSourceView(request.getId()),
                request.getCatalogName(),
                request.getSchemaName(),
                request.getObjectName()
        );
    }

    public List<DataSourceTypeResponse> getDataSourceTypes() {

        return domainService.getDataSourceTypes();
    }

    public DataSourceDependenciesResponse getDataSourceDependencies(DataSourceRequest request) {

        return domainService.getDataSourceDependencies(request.getId());

    }

    public DataSourceFolderResponse changeParentFolder(FolderChangeParentRequest request) {

        var roles = userDomainService.getUserRoles(userDomainService.getCurrentUser().getName(), null).stream().map(RoleView::getId).toList();

        folderPermissionsDomainService.getDataSourceFolderPermissionsForRoles(Collections.singletonList(request.getId()), roles)
                .forEach(f -> {
                    if (f.getAuthority() == FolderAuthorityEnum.NONE)
                        throw new InvalidParametersException(String.format("Move for folder is not available with id %s: Permission denied", f.getFolderId()));

                });

        folderPermissionsDomainService.getDataSourceFolderPermissionsForRoles(Collections.singletonList(request.getParentId()), roles)
                .forEach(f -> {
                    if (f.getAuthority() != FolderAuthorityEnum.WRITE)
                        throw new InvalidParametersException("Dest folder is not available: Permission denied");
                });

        return domainService.changeParentFolder(request);
    }

    public FolderSearchResponse searchDataSource(FolderSearchRequest request) {
        return folderEntitySearchDomainService.search(request,dataSourceFolderRepository,dataSourceResponseMapper, FolderTypes.DATASOURCE);
    }

    public void changeDataSourceParentFolder(ChangeParentFolderRequest request) {
        permissionCheckerSystem.checkPermissionsOnAllFolders(request, domainService::getFolderIds, folderPermissionsDomainService::getDataSourceFolderPermissionsForRoles);
        domainService.changeDataSourceParentFolder(request);
    }

    public void checkDataSource(DataSourceRequest request) {
        metaDataService.checkDataSource(domainService.getDataSourceView(request.getId()));
    }

    public void copyDataSource(ChangeParentFolderRequest request) {
        permissionCheckerSystem.checkPermissionsOnAllFolders(request, null, folderPermissionsDomainService::getDataSourceFolderPermissionsForRoles);
        domainService.copyDataSource(request, userDomainService.getCurrentUser());
    }

    public List<DataSourceFolderResponse> copyDataSourceFolder(CopyFolderRequest request){

        var roles = userDomainService.getUserRoles(userDomainService.getCurrentUser().getName(), null).stream().map(RoleView::getId).toList();

        folderPermissionsDomainService.getDataSourceFolderPermissionsForRoles(request.getFolderIds(), roles)
                .forEach(f -> {
                    if (f.getAuthority() == FolderAuthorityEnum.NONE)
                        throw new InvalidParametersException(String.format("Copy for folder is not available with id %s: Permission denied", f.getFolderId()));

                });

        folderPermissionsDomainService.getDataSourceFolderPermissionsForRoles(Collections.singletonList(request.getDestFolderId()), roles)
                .forEach(f -> {
                    if (f.getAuthority() != FolderAuthorityEnum.WRITE)
                        throw new InvalidParametersException("Dest folder is not available: Permission denied" + f.getFolderId());
                });

        var newFolders = domainService.copyDataSetFolder(request, userDomainService.getCurrentUser());
        return newFolders.stream().map(domainService::getFolder).toList();

    }

    public void checkDataSourceConnectivity(DataSourceCheckRequest request) {
        domainService.checkDataSourceConnectivity(request);
    }
}