package ru.magnit.magreportbackend.mapper.folderreport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.folderreport.Folder;
import ru.magnit.magreportbackend.dto.response.permission.FolderPermissionsResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FolderReportPermissionsResponseMapper implements Mapper<FolderPermissionsResponse, Folder> {

    private final FolderResponseMapper folderResponseMapper;
    private final RolePermissionResponseMapper responseMapper;

    @Override
    public FolderPermissionsResponse from(Folder source) {
        return new FolderPermissionsResponse(
                folderResponseMapper.shallowMap(source),
                responseMapper.from(source.getFolderRoles()));
    }
}