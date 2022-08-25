package ru.magnit.magreportbackend.mapper.filterinstance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceFolder;
import ru.magnit.magreportbackend.dto.response.permission.FilterInstanceFolderPermissionsResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FilterInstanceFolderPermissionsResponseMapper implements Mapper<FilterInstanceFolderPermissionsResponse, FilterInstanceFolder> {

    private final FilterInstanceFolderResponseMapper folderResponseMapper;
    private final RolePermissionResponseMapperFilterInstance responseMapper;

    @Override
    public FilterInstanceFolderPermissionsResponse from(FilterInstanceFolder source) {
        return new FilterInstanceFolderPermissionsResponse(
                folderResponseMapper.shallowMap(source),
                responseMapper.from(source.getFolderRoles()));
    }
}
