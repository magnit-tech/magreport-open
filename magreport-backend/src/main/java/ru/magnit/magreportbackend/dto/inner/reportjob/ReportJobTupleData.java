package ru.magnit.magreportbackend.dto.inner.reportjob;

import java.util.List;

public record ReportJobTupleData(
        List<ReportJobTupleFieldData> fieldValues
) {}