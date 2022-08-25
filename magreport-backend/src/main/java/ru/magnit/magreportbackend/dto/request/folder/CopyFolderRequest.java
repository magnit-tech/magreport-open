package ru.magnit.magreportbackend.dto.request.folder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@NoArgsConstructor
public class CopyFolderRequest {
    private List<Long> folderIds;
    private Long destFolderId;
    private boolean inheritParentRights;
    private boolean inheritRightsRecursive;
}

