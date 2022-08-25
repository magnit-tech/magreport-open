package ru.magnit.magreportbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.dto.inner.datasource.DataSourceData;
import ru.magnit.magreportbackend.dto.response.datasource.DataSourceObjectResponse;
import ru.magnit.magreportbackend.service.domain.TemplateParserService;
import ru.magnit.magreportbackend.service.jobengine.filter.FilterQueryExecutor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.magnit.magreportbackend.util.Constant.PATH_SEPARATOR;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcedureReportService {

    private final MetaDataService metaDataService;
    private final TemplateParserService templateParserService;
    private final FilterQueryExecutor filterQueryExecutor;

    @Value("${magreport.query-templates.proc-meta.tables}")
    private String[] tables;

    @Value("${magreport.query-templates.proc-meta.path}")
    private String templatePath;

   public boolean checkProcedureReportSchemaMetaData(DataSourceData dataSource, String schema) {
        return checkTables(dataSource, schema);
    }

   public void createProcedureReportMetaData(DataSourceData dataSource, String schema) {
        getMissingTables(dataSource, schema).forEach(tableName -> createDbObject(dataSource, schema, tableName));
    }

    private void createDbObject(DataSourceData dataSource, String schema, String tableName) {
        final var path = templatePath + PATH_SEPARATOR + dataSource.type().name().toLowerCase() + PATH_SEPARATOR + tableName.toLowerCase() + ".ftl";
        final var schemaParam = Map.of("schema", schema);
        final var query = templateParserService.parseTemplate(path, schemaParam);
        filterQueryExecutor.executeSql(dataSource, query);
    }

    private List<String> getMissingTables(DataSourceData dataSource, String schema) {
        final var schemaTables = metaDataService.getSchemaObjects(dataSource, null, schema, "TABLE");

        final var existingTables = schemaTables.stream()
                .map(DataSourceObjectResponse::getName)
                .map(String::toUpperCase)
                .collect(Collectors.toSet());

        return Arrays.stream(tables).filter(name -> !existingTables.contains(name)).toList();
    }

    private boolean checkTables(DataSourceData dataSource, String schema) {
        final var schemaTables = metaDataService.getSchemaObjects(dataSource, null, schema, "TABLE");

        final var existingTables = schemaTables.stream()
                .map(DataSourceObjectResponse::getName)
                .map(String::toUpperCase)
                .collect(Collectors.toSet());

        return Arrays.stream(tables).allMatch(existingTables::contains);
    }
}
