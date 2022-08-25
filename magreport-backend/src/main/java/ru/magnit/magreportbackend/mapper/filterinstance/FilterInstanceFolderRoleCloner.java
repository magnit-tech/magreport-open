package ru.magnit.magreportbackend.mapper.filterinstance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceFolderRole;
import ru.magnit.magreportbackend.mapper.Cloner;

@Service
@RequiredArgsConstructor
public class FilterInstanceFolderRoleCloner implements Cloner<FilterInstanceFolderRole> {

    private final FilterInstanceFolderRolePermissionCloner filterInstanceFolderRolePermissionCloner;

    @Override
    public FilterInstanceFolderRole clone(FilterInstanceFolderRole source) {
        var folderRole = new FilterInstanceFolderRole()
                .setRole(source.getRole());
        var permissions = filterInstanceFolderRolePermissionCloner.clone(source.getPermissions());
        permissions.forEach(p -> p.setFolderRole(folderRole));
        folderRole.setPermissions(permissions);

        return folderRole;
    }
}
