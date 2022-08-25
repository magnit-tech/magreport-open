package ru.magnit.magreportbackend.dto.request.folderreport;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class FolderPermissionSetRequest {

    private Long folderId;
    private List<RoleAddPermissionRequest> roles;
}
