package ru.magnit.magreportbackend.mapper.filterinstance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceFolder;
import ru.magnit.magreportbackend.mapper.Cloner;

@Service
@RequiredArgsConstructor
public class FilterInstanceFolderCloner implements Cloner<FilterInstanceFolder> {

    private final FilterInstanceCloner filterInstanceCloner;
    private final FilterInstanceFolderRoleCloner filterInstanceFolderRoleCloner;

    @Override
    public FilterInstanceFolder clone(FilterInstanceFolder source) {
        var folder = new FilterInstanceFolder()
                .setName(source.getName())
                .setDescription(source.getDescription());

        final var filterInstances = filterInstanceCloner.clone(source.getFilterInstances());
        filterInstances.forEach(filterInstance -> filterInstance.setFolder(folder));
        folder.setFilterInstances(filterInstances);

        final var folderRoles = filterInstanceFolderRoleCloner.clone(source.getFolderRoles());
        folderRoles.forEach(f -> f.setFolder(folder));
        folder.setFolderRoles(folderRoles);

        return folder;
    }
}
