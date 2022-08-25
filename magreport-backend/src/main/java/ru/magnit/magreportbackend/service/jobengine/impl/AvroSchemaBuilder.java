package ru.magnit.magreportbackend.service.jobengine.impl;

import lombok.RequiredArgsConstructor;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.springframework.stereotype.Service;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportData;

@Service
@RequiredArgsConstructor
public class AvroSchemaBuilder {

    public Schema createSchema(ReportData report) {

        var schemaBuilder = SchemaBuilder.builder("ru.magnit.magreport2.0");
        var recordBuilder = schemaBuilder.record("Report" + report.id());
        var fields = recordBuilder.fields();

        report.getVisibleFields().forEach(field -> fields.optionalString(field.columnName()));

        return fields.endRecord();
    }
}
