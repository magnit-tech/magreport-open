package ru.magnit.magreportbackend.mapper.folderreport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthority;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.domain.folderreport.FolderRolePermission;
import ru.magnit.magreportbackend.mapper.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FolderRolePermissionMapper implements Mapper<FolderRolePermission, FolderAuthorityEnum> {

    @Override
    public FolderRolePermission from(FolderAuthorityEnum source) {
        return null;
    }

    @Override
    public List<FolderRolePermission> from(List<FolderAuthorityEnum> sources) {
        return mapBaseProperties(sources);
    }

    private List<FolderRolePermission> mapBaseProperties(List<FolderAuthorityEnum> sources) {

        return sources
                .stream()
                .map(perm -> new FolderRolePermission()
                        .setAuthority(new FolderAuthority(perm)))
                .collect(Collectors.toList());
    }
}
