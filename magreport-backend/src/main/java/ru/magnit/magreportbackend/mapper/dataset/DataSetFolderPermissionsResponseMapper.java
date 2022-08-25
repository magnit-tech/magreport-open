package ru.magnit.magreportbackend.mapper.dataset;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolder;
import ru.magnit.magreportbackend.dto.response.permission.DataSetFolderPermissionsResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class DataSetFolderPermissionsResponseMapper implements Mapper<DataSetFolderPermissionsResponse, DataSetFolder> {

    private final DataSetFolderResponseMapper folderResponseMapper;
    private final RolePermissionResponseMapperDataSet responseMapper;

    @Override
    public DataSetFolderPermissionsResponse from(DataSetFolder source) {
        return new DataSetFolderPermissionsResponse(
                folderResponseMapper.shallowMap(source),
                responseMapper.from(source.getFolderRoles()));
    }
}
