package ru.magnit.magreportbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataSetTypeEnum;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.domain.user.SystemRoles;
import ru.magnit.magreportbackend.dto.inner.RoleView;
import ru.magnit.magreportbackend.dto.request.ChangeParentFolderRequest;
import ru.magnit.magreportbackend.dto.request.dataset.DataSetAddRequest;
import ru.magnit.magreportbackend.dto.request.dataset.DataSetCreateFromMetaDataRequest;
import ru.magnit.magreportbackend.dto.request.dataset.DataSetRequest;
import ru.magnit.magreportbackend.dto.request.folder.CopyFolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderAddRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderChangeParentRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRenameRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderSearchRequest;
import ru.magnit.magreportbackend.dto.request.folder.FolderTypes;
import ru.magnit.magreportbackend.dto.request.folderreport.FolderPermissionSetRequest;
import ru.magnit.magreportbackend.dto.request.folderreport.RoleAddPermissionRequest;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetDataTypeResponse;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetDependenciesResponse;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetFolderResponse;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetResponse;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetTypeResponse;
import ru.magnit.magreportbackend.dto.response.datasource.ObjectFieldResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderRoleResponse;
import ru.magnit.magreportbackend.dto.response.folder.FolderSearchResponse;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.mapper.dataset.DataSetResponseMapper;
import ru.magnit.magreportbackend.repository.DataSetFolderRepository;
import ru.magnit.magreportbackend.service.domain.DataSetDomainService;
import ru.magnit.magreportbackend.service.domain.DataSourceDomainService;
import ru.magnit.magreportbackend.service.domain.FilterInstanceDomainService;
import ru.magnit.magreportbackend.service.domain.FolderEntitySearchDomainService;
import ru.magnit.magreportbackend.service.domain.FolderPermissionsDomainService;
import ru.magnit.magreportbackend.service.domain.ReportDomainService;
import ru.magnit.magreportbackend.service.domain.SecurityFilterDomainService;
import ru.magnit.magreportbackend.service.domain.UserDomainService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.magnit.magreportbackend.domain.dataset.DataSetTypeEnum.PROCEDURE;

@Service
@RequiredArgsConstructor
public class DataSetService {

    private final DataSetDomainService domainService;
    private final MetaDataService metaDataService;
    private final DataSourceDomainService dataSourceService;
    private final UserDomainService userDomainService;
    private final ReportDomainService reportDomainService;
    private final FolderPermissionsDomainService folderPermissionsDomainService;
    private final ProcedureReportService procedureReportService;
    private final SecurityFilterDomainService securityFilterDomainService;
    private final FilterInstanceDomainService filterInstanceDomainService;
    private final FolderEntitySearchDomainService folderEntitySearchDomainService;
    private final DataSetFolderRepository dataSetFolderRepository;
    private final DataSetResponseMapper dataSetResponseMapper;
    private final PermissionCheckerSystem permissionCheckerSystem;

    public DataSetFolderResponse getFolder(FolderRequest request) {
        final var folderResponse = domainService.getFolder(request.getId());

        final var folderIds = folderResponse.getChildFolders().stream().map(DataSetFolderResponse::getId).collect(Collectors.toList());
        folderIds.add(folderResponse.getId());

        final var userRoles = userDomainService.getUserRoles(userDomainService.getCurrentUser().getName(), null).stream().map(RoleView::getId).toList();
        final var foldersPermissions = folderPermissionsDomainService.getDataSetFolderPermissionsForRoles(folderIds, userRoles).stream().collect(Collectors.toMap(FolderRoleResponse::getFolderId, FolderRoleResponse::getAuthority));

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

    public DataSetFolderResponse addFolder(FolderAddRequest request) {
        final var currentFolderPermissions = request.getParentId() == null ?
                null :
                folderPermissionsDomainService.getDataSetFolderPermissions(request.getParentId());

        final var result = domainService.addFolder(request);

        if (currentFolderPermissions != null) {
            folderPermissionsDomainService.setDataSetFolderPermissions(
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

    public List<DataSetFolderResponse> getChildFolders(FolderRequest request) {
        return domainService.getChildFolders(request.getId());
    }

    public DataSetFolderResponse renameFolder(FolderRenameRequest request) {
        return domainService.renameFolder(request);
    }

    public void deleteFolder(FolderRequest request) {
        domainService.deleteFolder(request.getId());
    }

    public DataSetResponse editDataSet(DataSetAddRequest request) {

        var dataSource = dataSourceService.getDataSourceView(request.getDataSourceId());

        if (!metaDataService.checkObjectExists(dataSource,request.getSchemaName(),request.getObjectName().toUpperCase()) && !DataSetTypeEnum.PROCEDURE.equalsIsLong(request.getTypeId())) {
            throw new InvalidParametersException( String.format("Object %s doesn't exist",request.getObjectName()));
        }
        domainService.editDataSet(request);
        return domainService.getDataSet(request.getId());



    }

    public void deleteDataSet(DataSetRequest request) {


        var reports = reportDomainService.checkReportExistsForDataset(request.getId());
        var filters = filterInstanceDomainService.checkFilterExistsForDataset(request.getId());
        var securityFilter = securityFilterDomainService.checkSecurityFilterExistsForDataset(request.getId());


        if (!reports.isEmpty() || !filters.isEmpty() || !securityFilter.isEmpty())
            throw new InvalidParametersException("Набор данных не может быть удален пока существуют отчеты и фильтры, которые его используют");

        domainService.deleteDataSet(request.getId());
    }

    public DataSetResponse getDataSet(DataSetRequest request) {
        return domainService.getDataSet(request.getId());
    }

    public DataSetResponse createDataSetFromDBMetaData(DataSetCreateFromMetaDataRequest request) {

        final var currentUser = userDomainService.getCurrentUser();
        var dataSource = dataSourceService.getDataSourceView(request.getDataSourceId());
        Long dataSetId;

        if (PROCEDURE.equalsIsLong(request.getTypeId())) {

            if (!procedureReportService.checkProcedureReportSchemaMetaData(dataSource, request.getSchemaName()))
                procedureReportService.createProcedureReportMetaData(dataSource, request.getSchemaName());

            var objectFields = metaDataService.getProcedureFields2(
                    dataSource,
                    request.getCatalogName(),
                    request.getSchemaName(),
                    request.getObjectName());

            dataSetId = domainService.addDataSetFromProcedure(currentUser, request, objectFields);
        } else {
            if (metaDataService.checkObjectExists(dataSource,request.getSchemaName(),request.getObjectName().toUpperCase())) {
                var objectFields = metaDataService.getObjectFields(
                        dataSource,
                        request.getCatalogName(),
                        request.getSchemaName(),
                        request.getObjectName());
                dataSetId = domainService.createDataSetFromMetaData(currentUser, request, objectFields);
            }
            else throw new InvalidParametersException( String.format("Object %s doesn't exist",request.getObjectName()));
        }
        return domainService.getDataSet(dataSetId);
    }

    public List<DataSetTypeResponse> getDataSetTypes() {

        return domainService.getDataSetTypes();
    }

    public List<DataSetDataTypeResponse> getDataSetDataTypes() {
        return domainService.getDataSetDataTypes();
    }

    public DataSetResponse refreshDataSet(DataSetRequest request) {

        final var dataSet = domainService.getDataSet(request.getId());

        final var dataSource = dataSourceService.getDataSourceView(dataSet.getDataSource().id());

        List<ObjectFieldResponse> objectFields;
        if (PROCEDURE.equalsIsLong(dataSet.getTypeId())) {

            objectFields = metaDataService.getProcedureFields2(
                    dataSource,
                    dataSet.getCatalogName(),
                    dataSet.getSchemaName(),
                    dataSet.getObjectName());
        } else {
            objectFields = metaDataService.getObjectFields(
                    dataSource,
                    dataSet.getCatalogName(),
                    dataSet.getSchemaName(),
                    dataSet.getObjectName());
        }

        final var fields = domainService.getUnlinkedInvalidFields(dataSet, objectFields);
        domainService.deleteFields(fields);

        var addedFields = domainService.refreshDataSet(dataSet, objectFields);

        var mapOrdinalFields = objectFields.stream().collect(Collectors.toMap(o-> o.getFieldName().toUpperCase(), ObjectFieldResponse::getOrdinalPosition));

        final var reportIds = domainService.getReportIds(request.getId());
        reportIds.forEach(reportId -> {
            reportDomainService.addFields(reportId, addedFields);
            if (PROCEDURE.equalsIsLong(dataSet.getTypeId())) reportDomainService.updateOrdinalFields(reportId, mapOrdinalFields);
        });
        domainService.actualizeDataSet(request.getId());

        return domainService.getDataSet(request.getId());
    }

    public DataSetDependenciesResponse getDataSetDependants(DataSetRequest request) {

        var response = domainService.getDataSetDependants(request.getId());

        response.getReports().forEach(reportResponse -> reportResponse.setPath(reportDomainService.getPathReport(reportResponse.getId())));
        response.getSecurityFilters().forEach(securityFilterResponse -> securityFilterResponse.path().addAll(securityFilterDomainService.getPathSecurityFilter(securityFilterResponse.id())));
        response.getFilterInstances().forEach(filter -> filter.setPath(filterInstanceDomainService.getPathFilter(filter.getId())));

        return response;
    }

    public DataSetResponse addDataSetFromProcedure(DataSetCreateFromMetaDataRequest request) {

        final var currentUser = userDomainService.getCurrentUser();
        var dataSource = dataSourceService.getDataSourceView(request.getDataSourceId());

        if (procedureReportService.checkProcedureReportSchemaMetaData(dataSource, request.getSchemaName()))
            procedureReportService.createProcedureReportMetaData(dataSource, request.getSchemaName());

        var objectFields = metaDataService.getProcedureFields2(
                dataSource,
                request.getCatalogName(),
                request.getSchemaName(),
                request.getObjectName());

        var dataSetId = domainService.addDataSetFromProcedure(currentUser, request, objectFields);
        return domainService.getDataSet(dataSetId);

    }

    public DataSetFolderResponse changeParentFolder(FolderChangeParentRequest request) {
        var roles = userDomainService.getUserRoles(userDomainService.getCurrentUser().getName(), null).stream().map(RoleView::getId).toList();

        folderPermissionsDomainService.getDataSetFolderPermissionsForRoles(Collections.singletonList(request.getId()), roles)
                .forEach(f -> {
                    if (f.getAuthority() == FolderAuthorityEnum.NONE)
                        throw new InvalidParametersException(String.format("Move for folder is not available with id %s: Permission denied", f.getFolderId()));

                });

        folderPermissionsDomainService.getDataSetFolderPermissionsForRoles(Collections.singletonList(request.getParentId()), roles)
                .forEach(f -> {
                    if (f.getAuthority() != FolderAuthorityEnum.WRITE)
                        throw new InvalidParametersException("Dest folder is not available: Permission denied");
                });


        return domainService.changeParentFolder(request);
    }

    public void changeDataSetParentFolder(ChangeParentFolderRequest request) {
        permissionCheckerSystem.checkPermissionsOnAllFolders(request, domainService::getFolderIds, folderPermissionsDomainService::getDataSetFolderPermissionsForRoles);
        domainService.changeDataSetParentFolder(request);
    }

    public FolderSearchResponse searchDataSet(FolderSearchRequest request) {
        return folderEntitySearchDomainService.search(request, dataSetFolderRepository, dataSetResponseMapper, FolderTypes.DATASET );
    }

    public void copyDataSets(ChangeParentFolderRequest request) {
        permissionCheckerSystem.checkPermissionsOnAllFolders(request, null, folderPermissionsDomainService::getDataSetFolderPermissionsForRoles);
        domainService.copyDataSet(request, userDomainService.getCurrentUser());
    }

    public List<DataSetFolderResponse> copyDataSetFolders(CopyFolderRequest request) {
        var roles = userDomainService.getUserRoles(userDomainService.getCurrentUser().getName(), null).stream().map(RoleView::getId).toList();

        folderPermissionsDomainService.getDataSetFolderPermissionsForRoles(request.getFolderIds(), roles)
                .forEach(f -> {
                    if (f.getAuthority() == FolderAuthorityEnum.NONE)
                        throw new InvalidParametersException(String.format("Copy for folder is not available with id %s: Permission denied", f.getFolderId()));

                });

        folderPermissionsDomainService.getDataSetFolderPermissionsForRoles(Collections.singletonList(request.getDestFolderId()), roles)
                .forEach(f -> {
                    if (f.getAuthority() != FolderAuthorityEnum.WRITE)
                        throw new InvalidParametersException("Dest folder is not available: Permission denied");
                });

        var newFolders = domainService.copyDataSetFolder(request, userDomainService.getCurrentUser());
        return newFolders.stream().map(domainService::getFolder).toList();
    }

}
