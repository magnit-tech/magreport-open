package ru.magnit.magreportbackend.mapper.securityfilter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterFolder;
import ru.magnit.magreportbackend.dto.response.permission.SecurityFilterFolderPermissionsResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class SecurityFilterFolderPermissionsResponseMapper implements Mapper<SecurityFilterFolderPermissionsResponse, SecurityFilterFolder> {

    private final SecurityFilterFolderResponseMapper folderResponseMapper;
    private final RolePermissionResponseMapperSecurityFilter responseMapper;

    @Override
    public SecurityFilterFolderPermissionsResponse from(SecurityFilterFolder source) {
        return new SecurityFilterFolderPermissionsResponse(
            folderResponseMapper.shallowMap(source),
            responseMapper.from(source.getFolderRoles()));
    }
}
