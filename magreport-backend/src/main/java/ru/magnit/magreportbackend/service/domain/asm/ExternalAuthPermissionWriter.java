package ru.magnit.magreportbackend.service.domain.asm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.dto.inner.asm.ExternalAuthSourceView;
import ru.magnit.magreportbackend.service.dao.ConnectionPoolManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalAuthPermissionWriter {

    private final ExternalAuthRoleFilterRefreshQueryBuilder queryBuilder;
    private final ConnectionPoolManager poolManager;

    public void process(ExternalAuthSourceView source, BiConsumer<List<Map<String, String>>, ExternalAuthSourceView> processor) {
        var dataSourceView = source.getDataSet().getDataSource();
        var query = queryBuilder.buildQuery(source);

        try (
                Connection connection = poolManager.getConnection(dataSourceView);
                Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                ResultSet resultSet = statement.executeQuery(query)
        ) {
            processRecord(processor, resultSet, source);
        } catch (SQLException ex) {
            log.error("FilterQueryImpl.getHierarchyAsTuples(): Error trying getting filter field values.", ex);
        }
    }

    private void processRecord(BiConsumer<List<Map<String, String>>, ExternalAuthSourceView> processor, ResultSet resultSet, ExternalAuthSourceView securitySource) throws SQLException {

        List<String> columnNames = getColumnNames(resultSet.getMetaData());
        String roleNameColumn = securitySource.getRoleNameField().getDataSetField().getName().toLowerCase();
        String lastRole = "";
        List<Map<String, String>> buffer = new LinkedList<>();

        while (resultSet.next()) {
            Map<String, String> row = new HashMap<>();
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                row.put(columnNames.get(i - 1), resultSet.getString(i));
            }
            if (!lastRole.equalsIgnoreCase(row.get(roleNameColumn))) {
                if (!buffer.isEmpty()) processor.accept(buffer, securitySource);
                buffer.clear();
                lastRole = row.get(roleNameColumn);
            }
            buffer.add(row);
        }

        processor.accept(buffer, securitySource);
    }

    private static List<String> getColumnNames(ResultSetMetaData metaData) throws SQLException {
        return IntStream
                .rangeClosed(1, metaData.getColumnCount())
                .mapToObj(i -> getColumnName(metaData, i))
                .collect(Collectors.toList());
    }

    private static String getColumnName(ResultSetMetaData metaData, int columnNumber) {
        String result = null;
        try {
            result = metaData.getColumnLabel(columnNumber).toLowerCase();
        } catch (SQLException ex) {
            log.error("Error trying to get column name");
        }
        return result;
    }

}
