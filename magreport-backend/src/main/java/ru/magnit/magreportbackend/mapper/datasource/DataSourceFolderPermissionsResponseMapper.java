package ru.magnit.magreportbackend.mapper.datasource;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.datasource.DataSourceFolder;
import ru.magnit.magreportbackend.dto.response.permission.DataSourceFolderPermissionsResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class DataSourceFolderPermissionsResponseMapper implements Mapper<DataSourceFolderPermissionsResponse, DataSourceFolder> {

    private final DataSourceFolderResponseMapper folderResponseMapper;
    private final RolePermissionResponseMapperDataSource responseMapper;

    @Override
    public DataSourceFolderPermissionsResponse from(DataSourceFolder source) {

        return new DataSourceFolderPermissionsResponse(
                folderResponseMapper.shallowMap(source),
                responseMapper.from(source.getFolderRoles()));
    }
}
