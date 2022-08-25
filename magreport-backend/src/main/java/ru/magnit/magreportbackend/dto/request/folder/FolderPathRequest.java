package ru.magnit.magreportbackend.dto.request.folder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@NoArgsConstructor
public class FolderPathRequest {
    private FolderTypes folderType;
    private Long id;
}
