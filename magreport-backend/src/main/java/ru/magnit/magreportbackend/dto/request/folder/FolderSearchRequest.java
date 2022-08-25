package ru.magnit.magreportbackend.dto.request.folder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.dto.request.filterinstance.LikenessType;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@SuppressWarnings("UNUSED")
public class FolderSearchRequest {

    private Long rootFolderId;
    private LikenessType likenessType;
    private String searchString;
    private boolean recursive;
}
