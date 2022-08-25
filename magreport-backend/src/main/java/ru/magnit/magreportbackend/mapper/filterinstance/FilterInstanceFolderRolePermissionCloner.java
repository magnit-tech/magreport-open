package ru.magnit.magreportbackend.mapper.filterinstance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceFolderRolePermission;
import ru.magnit.magreportbackend.mapper.Cloner;

@Service
@RequiredArgsConstructor
public class FilterInstanceFolderRolePermissionCloner implements Cloner<FilterInstanceFolderRolePermission> {
    @Override
    public FilterInstanceFolderRolePermission clone(FilterInstanceFolderRolePermission source) {
        return new FilterInstanceFolderRolePermission().setAuthority(source.getAuthority());
    }
}
