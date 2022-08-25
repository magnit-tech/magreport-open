package ru.magnit.magreportbackend.mapper.dataset;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSource;
import ru.magnit.magreportbackend.domain.dataset.DataSet;
import ru.magnit.magreportbackend.domain.dataset.DataSetFolder;
import ru.magnit.magreportbackend.dto.response.dataset.DataSetDependenciesResponse;
import ru.magnit.magreportbackend.mapper.Mapper;
import ru.magnit.magreportbackend.mapper.asm.AsmSecurityResponseMapper;
import ru.magnit.magreportbackend.mapper.auth.UserResponseMapper;
import ru.magnit.magreportbackend.mapper.filterinstance.FilterInstanceDependenciesResponseMapper;
import ru.magnit.magreportbackend.mapper.report.ReportResponseMapper;
import ru.magnit.magreportbackend.mapper.securityfilter.SecurityFilterResponseMapper;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;


@Service
@RequiredArgsConstructor
public class DataSetDependenciesResponseMapper implements Mapper<DataSetDependenciesResponse, DataSet> {

    private final UserResponseMapper userResponseMapper;
    private final ReportResponseMapper reportResponseMapper;
    private final FilterInstanceDependenciesResponseMapper filterInstanceDependenciesResponseMapper;
    private final SecurityFilterResponseMapper securityFilterResponseMapper;
    private final AsmSecurityResponseMapper asmSecurityResponseMapper;
    private final FolderNodeResponseDataSetFolderMapper folderMapper;

    @Override
    public DataSetDependenciesResponse from(DataSet source) {
        return new DataSetDependenciesResponse(
                source.getId(),
                source.getType().getId(),
                userResponseMapper.from(source.getUser()),
                source.getSchemaName(),
                source.getObjectName(),
                source.getName(),
                source.getDescription(),
                source.getFields().stream().anyMatch(field -> !field.getIsSync()),
                reportResponseMapper.from(source.getReports()),
                filterInstanceDependenciesResponseMapper.from(source.getFilterInstances()),
                securityFilterResponseMapper.from(source.getFilterInstances().stream().flatMap(fi -> fi.getSecurityFilters().stream()).distinct().collect(Collectors.toList())),
                asmSecurityResponseMapper.from(source.getExternalAuthSources().stream().map(ExternalAuthSource::getExternalAuth).distinct().collect(Collectors.toList())),
                source.getCreatedDateTime(),
                source.getModifiedDateTime(),
                folderMapper.from(getPathToRoot(source.getFolder())));
    }

    private List<DataSetFolder> getPathToRoot(DataSetFolder leafFolder) {
        if (leafFolder == null) return emptyList();

        List<DataSetFolder> result = new LinkedList<>();

        var curNode = leafFolder;
        result.add(0, curNode);
        while (curNode.getParentFolder() != null) {
            result.add(0, curNode.getParentFolder());
            curNode = curNode.getParentFolder();
        }
        return result;
    }
}
