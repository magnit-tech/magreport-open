package ru.magnit.magreportbackend.domain;

import java.time.LocalDateTime;
import java.util.List;

public interface FolderEntity {

    Long getId();

    String getName();

    EntityWithName setName (String name);

    String getDescription();

    EntityWithName setDescription (String description);

    EntityWithName setParentFolder(EntityWithName parentFolder);

    EntityWithName getParentFolder();

    List<? extends EntityWithName> getChildFolders();

    EntityWithName setChildFolders (List<? extends EntityWithName> list);

    List<? extends EntityWithName> getChildElements();

    EntityWithName setChildElements (List<? extends EntityWithName> list);

    LocalDateTime getCreatedDateTime();

    LocalDateTime getModifiedDateTime();
}
