package ru.magnit.magreportbackend.service.domain.converter.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import ru.magnit.magreportbackend.dto.inner.jobengine.CacheEntry;
import ru.magnit.magreportbackend.dto.inner.jobengine.CacheRow;
import ru.magnit.magreportbackend.dto.inner.olap.CubeData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportFieldData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobData;
import ru.magnit.magreportbackend.exception.ReportExportException;
import ru.magnit.magreportbackend.service.domain.converter.Reader;
import ru.magnit.magreportbackend.util.MemoryUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class AvroReader implements Reader {

    private final DataFileReader<GenericRecord> reader;
    private final ReportData reportData;
    private final List<ReportFieldData> visibleFields;
    private final int fieldCount;
    private final long rowCount;

    AvroReader(ReportJobData data, Path inputPath) {
        reportData = data.reportData();
        visibleFields = reportData.getVisibleFields();
        rowCount = data.rowCount();
        var inputFile = inputPath.toFile();
        var datumReader = new GenericDatumReader<GenericRecord>();
        try {
            reader = new DataFileReader<>(inputFile, datumReader);
            fieldCount = reader.getSchema().getFields().size();
        } catch (IOException ex) {
            throw new ReportExportException("Error while trying to open input avro file.", ex);
        }
    }

    @Override
    public CacheRow getRow() {

        if (!reader.hasNext()) return null;

        return getNextRow();
    }

    @Override
    public List<Map<Long, CacheEntry>> getAllRows(Set<Long> allFieldIds) {
        final var result = new LinkedList<Map<Long, CacheEntry>>();

        var freeMemoryBefore = MemoryUtils.getTotalFreeMemory();
        while (reader.hasNext()) {
            var dataRow = getNextRow();
            result.add(dataRow.entries().stream()
                    .filter(entry -> allFieldIds.contains(entry.fieldData().id()))
                    .collect(Collectors.toMap(entry -> entry.fieldData().id(), entry -> entry)));
        }

        var freeMemoryAfter = MemoryUtils.getTotalFreeMemory();
        log.debug("Total size of cube: " + (freeMemoryAfter - freeMemoryBefore));

        return result;
    }

    @Override
    public CubeData getCube() {
        final var data = new String[visibleFields.size()][(int)rowCount];
        var rowNumber = 0;

        while (reader.hasNext()) {
            final var dataRecord = reader.next();
            for (int i = 0; i < fieldCount; i++) {
                final var value = dataRecord.get(i);
                data[i][rowNumber] = value == null ? null : dataRecord.get(i).toString().intern();
            }
            rowNumber++;
        }

        return new CubeData(
                reportData,
                (int)rowCount,
                getFieldIndexes(reportData.getVisibleFields()),
                data
        );
    }

    private Map<Long, Integer> getFieldIndexes(List<ReportFieldData> fields) {
        var counter = new AtomicInteger(0);
        return fields.stream().collect(Collectors.toMap(ReportFieldData::id, o -> counter.getAndIncrement()));
    }

    @Override
    public void close() {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException ex) {
                throw new ReportExportException("Error while trying to close input avro file.", ex);
            }
        }
    }

    private CacheRow getNextRow() {
        final var genericRecord = reader.next();
        final var entries = new ArrayList<CacheEntry>(fieldCount);
        for (int i = 0; i < fieldCount; i++) {
            final var value = genericRecord.get(i);
            entries.add(new CacheEntry(visibleFields.get(i), value == null ? null : value.toString().intern()));
        }
        return new CacheRow(entries);
    }
}
