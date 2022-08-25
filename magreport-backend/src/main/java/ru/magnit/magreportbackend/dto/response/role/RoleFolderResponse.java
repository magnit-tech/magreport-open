package ru.magnit.magreportbackend.dto.response.role;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.dto.request.folder.FolderTypes;

public record RoleFolderResponse(
        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        Long folderId,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        String folderName,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        FolderAuthorityEnum roleAuthority,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        FolderTypes typeFolder
) {
}
