package ru.magnit.magreportbackend.dto.response.role;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.magnit.magreportbackend.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record RoleFolderPermissionResponse(
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    List<RoleFolderResponse> filterTemplateFolders,

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    List<RoleFolderResponse> excelTemplateFolders,

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    List<RoleFolderResponse> filterInstanceFolders,

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    List<RoleFolderResponse> securityFilterFolders,

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    List<RoleFolderResponse> datasourceFolders,

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    List<RoleFolderResponse> datasetFolders,

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    List<RoleFolderResponse> devReportFolders,

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    List<RoleFolderResponse> userReportFolders
) {
    public  RoleFolderPermissionResponse() {
        this(Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList());
    }

    public boolean isEmpty() {
        return filterTemplateFolders.isEmpty() &&
            excelTemplateFolders.isEmpty() &&
            filterInstanceFolders.isEmpty() &&
            securityFilterFolders.isEmpty() &&
            datasourceFolders.isEmpty() &&
            datasetFolders.isEmpty() &&
            devReportFolders.isEmpty() &&
            userReportFolders.isEmpty();
    }

    public List<Pair<String, List<RoleFolderResponse>>> allRoleFolders() {
        final var result = new ArrayList<Pair<String, List<RoleFolderResponse>>>();
        if (!filterTemplateFolders.isEmpty()) {
            result.add(new Pair<>("Каталоги шаблонов фильтров", filterTemplateFolders));
        }
        if (!excelTemplateFolders.isEmpty()) {
            result.add(new Pair<>("Каталоги шаблонов отчетов", excelTemplateFolders));
        }
        if (!filterInstanceFolders.isEmpty()) {
            result.add(new Pair<>("Каталоги экземпляров фильтров", filterInstanceFolders));
        }
        if (!securityFilterFolders.isEmpty()) {
            result.add(new Pair<>("Каталоги фильтров безопасности", securityFilterFolders));
        }
        if (!datasourceFolders.isEmpty()) {
            result.add(new Pair<>("Каталоги источников данных", datasourceFolders));
        }
        if (!datasetFolders.isEmpty()) {
            result.add(new Pair<>("Каталоги наборов данных", datasetFolders));
        }
        if (!devReportFolders.isEmpty()) {
            result.add(new Pair<>("Каталоги разработки отчетов", devReportFolders));
        }
        if (!userReportFolders.isEmpty()) {
            result.add(new Pair<>("Каталоги пользовательских отчетов", userReportFolders));
        }
        return result;
    }
}
