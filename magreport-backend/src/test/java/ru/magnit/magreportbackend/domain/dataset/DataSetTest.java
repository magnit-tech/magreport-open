package ru.magnit.magreportbackend.domain.dataset;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.magnit.magreportbackend.domain.BaseEntityTest;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSource;
import ru.magnit.magreportbackend.domain.datasource.DataSource;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstance;
import ru.magnit.magreportbackend.domain.report.Report;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterDataSet;
import ru.magnit.magreportbackend.domain.user.User;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DataSetTest extends BaseEntityTest {

    private static final long ID = 1L;
    private static final String NAME = "DataSource";
    private static final String DESCRIPTION = "DataSource description";
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final String CATALOG_NAME = "catalogName";
    private static final String SCHEMA_NAME = "schemaName";
    private static final String OBJECT_NAME = "objectName";

    @BeforeAll
    public void init() throws ClassNotFoundException {
        checkNumberOfFields(17);
    }

    @Test
    void testNoArgsConstructor() {
        var dataSet = getDataSet();

        assertEquals(ID, dataSet.getId());
        assertEquals(NAME, dataSet.getName());
        assertEquals(DESCRIPTION, dataSet.getDescription());
        assertEquals(NOW, dataSet.getCreatedDateTime());
        assertEquals(NOW.plusMinutes(2), dataSet.getModifiedDateTime());

        assertNotNull(dataSet.getType());
        assertNotNull(dataSet.getFolder());
        assertNotNull(dataSet.getDataSource());
        assertEquals(CATALOG_NAME, dataSet.getCatalogName());
        assertEquals(SCHEMA_NAME, dataSet.getSchemaName());

        assertEquals(OBJECT_NAME, dataSet.getObjectName());
        assertNotNull(dataSet.getFields());
        assertNotNull(dataSet.getExternalAuthSources());
        assertNotNull(dataSet.getFilterInstances());
        assertNotNull(dataSet.getSecurityFilterDataSets());

        assertNotNull(dataSet.getUser());
        assertNotNull(dataSet.getReports());

        var testDataSet = new DataSet(-ID);
        assertEquals(-ID, testDataSet.getId());
    }

    private static DataSet getDataSet() {

        return new DataSet()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setCreatedDateTime(NOW)
                .setModifiedDateTime(NOW.plusMinutes(2))
                .setType(new DataSetType())
                .setFolder(new DataSetFolder())
                .setDataSource(new DataSource())
                .setCatalogName(CATALOG_NAME)
                .setSchemaName(SCHEMA_NAME)
                .setObjectName(OBJECT_NAME)
                .setFields(Collections.singletonList(new DataSetField()))
                .setExternalAuthSources(Collections.singletonList(new ExternalAuthSource()))
                .setFilterInstances(Collections.singletonList(new FilterInstance()))
                .setSecurityFilterDataSets(Collections.singletonList(new SecurityFilterDataSet()))
                .setUser(new User())
                .setReports(Collections.singletonList(new Report()));
    }
}