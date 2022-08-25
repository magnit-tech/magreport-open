package ru.magnit.magreportbackend.mapper.role;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.user.RoleType;
import ru.magnit.magreportbackend.dto.response.folder.FolderNodeResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class FolderNodeResponseRoleTypeMapper implements Mapper<FolderNodeResponse, RoleType> {

    @Override
    public FolderNodeResponse from(RoleType source) {
        return new FolderNodeResponse(
                source.getId(),
                null,
                source.getName(),
                source.getDescription(),
                source.getCreatedDateTime(),
                source.getModifiedDateTime()
        );
    }
}
