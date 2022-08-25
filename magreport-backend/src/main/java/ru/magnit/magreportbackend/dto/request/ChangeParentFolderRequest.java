package ru.magnit.magreportbackend.dto.request;

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
public class ChangeParentFolderRequest {
    private List<Long> objIds;
    private Long destFolderId;
}
