package ru.magnit.magreportbackend.mapper.securityfilter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.folderreport.FolderAuthorityEnum;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterFolder;
import ru.magnit.magreportbackend.dto.response.securityfilter.SecurityFilterFolderResponse;
import ru.magnit.magreportbackend.mapper.Mapper;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class SecurityFilterFolderResponseMapper implements Mapper<SecurityFilterFolderResponse, SecurityFilterFolder> {

    private final SecurityFilterResponseMapper securityFilterResponseMapper;

    @Override
    public SecurityFilterFolderResponse from(SecurityFilterFolder source) {

        return mapBaseProperties(source);
    }

    private SecurityFilterFolderResponse mapBaseProperties(SecurityFilterFolder source) {
        return new SecurityFilterFolderResponse(
            source.getId(),
            source.getParentFolder() == null ? null : source.getParentFolder().getId(),
            source.getName(),
            source.getDescription(),
            shallowMap(source.getChildFolders()),
            securityFilterResponseMapper.from(source.getFilters()),
            FolderAuthorityEnum.NONE,
            source.getCreatedDateTime(),
            source.getModifiedDateTime());
    }


    @Override
    public SecurityFilterFolderResponse shallowMap(SecurityFilterFolder source) {

        return new SecurityFilterFolderResponse(
            source.getId(),
            source.getParentFolder() == null ? null : source.getParentFolder().getId(),
            source.getName(),
            source.getDescription(),
            Collections.emptyList(),
            Collections.emptyList(),
            FolderAuthorityEnum.NONE,
            source.getCreatedDateTime(),
            source.getModifiedDateTime());
    }
}
