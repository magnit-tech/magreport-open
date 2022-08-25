package ru.magnit.magreportbackend.dto.inner.olap;

import ru.magnit.magreportbackend.dto.inner.reportjob.ReportData;

import java.util.Map;
import java.util.Objects;

public record CubeData (
    ReportData reportMetaData,
    int numRows,
    Map<Long, Integer> fieldIndexes,
    String[][] data
) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CubeData cubeData)) return false;
        return reportMetaData.equals(cubeData.reportMetaData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reportMetaData);
    }

    @Override
    public String toString() {
        return "CubeData{" +
                "reportMetaData=" + reportMetaData +
                '}';
    }
}
