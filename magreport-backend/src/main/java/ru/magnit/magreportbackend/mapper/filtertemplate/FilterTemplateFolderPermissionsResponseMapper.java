package ru.magnit.magreportbackend.mapper.filtertemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filtertemplate.FilterTemplateFolder;
import ru.magnit.magreportbackend.dto.response.permission.FilterTemplateFolderPermissionsResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FilterTemplateFolderPermissionsResponseMapper implements Mapper<FilterTemplateFolderPermissionsResponse, FilterTemplateFolder> {

    private final FilterTemplateFolderResponseMapper folderResponseMapper;
    private final RolePermissionResponseMapperFilterTemplate responseMapper;

    @Override
    public FilterTemplateFolderPermissionsResponse from(FilterTemplateFolder source) {
        return new FilterTemplateFolderPermissionsResponse(
                folderResponseMapper.shallowMap(source),
                responseMapper.from(source.getFolderRoles()));
    }
}
