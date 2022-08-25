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
public class FolderRenameRequest {

    private Long id;
    private String name;
    private String description;
}
