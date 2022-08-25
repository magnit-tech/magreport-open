package ru.magnit.magreportbackend.mapper.folderreport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.dto.inner.folderreport.FolderRoleView;
import ru.magnit.magreportbackend.dto.request.folderreport.FolderPermissionSetRequest;
import ru.magnit.magreportbackend.mapper.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FolderRoleViewMapper implements Mapper<List<FolderRoleView>, FolderPermissionSetRequest> {

    @Override
    public List<FolderRoleView> from(FolderPermissionSetRequest source) {
        return mapBaseProperties(source);
    }

    private List<FolderRoleView> mapBaseProperties(FolderPermissionSetRequest source) {

        return source.getRoles()
                .stream()
                .map(role -> new FolderRoleView()
                        .setFolderId(source.getFolderId())
                        .setRoleId(role.getRoleId())
                        .setPermissions(role.getPermissions()))
                .collect(Collectors.toList());
    }
}
