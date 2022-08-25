package ru.magnit.magreportbackend.service.jobengine.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.magnit.magreportbackend.domain.dataset.DataSetTypeEnum;
import ru.magnit.magreportbackend.dto.inner.jobengine.CacheEntry;
import ru.magnit.magreportbackend.dto.inner.jobengine.CacheRow;
import ru.magnit.magreportbackend.dto.inner.jobengine.ReportReaderData;
import ru.magnit.magreportbackend.dto.inner.reportjob.ReportFieldData;
import ru.magnit.magreportbackend.exception.QueryExecutionException;
import ru.magnit.magreportbackend.exception.ReportExportException;
import ru.magnit.magreportbackend.service.dao.ConnectionPoolManager;
import ru.magnit.magreportbackend.service.jobengine.QueryBuilder;
import ru.magnit.magreportbackend.service.jobengine.ReaderStatus;
import ru.magnit.magreportbackend.service.jobengine.ReportReader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class ReportReaderImpl implements ReportReader {

    private ReaderStatus status = ReaderStatus.IDLE;
    private final ReportReaderData readerData;
    private final QueryBuilder queryBuilder;
    private final ConnectionPoolManager poolManager;
    private final Long maxRows;
    private long rowCount;
    private boolean isCanceled;
    private String errorDescription = null;
    private final AtomicReference<PreparedStatement> runningStatement = new AtomicReference<>();

    @Override
    public void run() {
        try (
                var connection = poolManager.getConnection(readerData.dataSource())
        ) {
            status = ReaderStatus.RUNNING;
            processData(connection);
            status = isCanceled ? ReaderStatus.CANCELED : ReaderStatus.FINISHED;
        } catch (Exception ex) {
            status = ReaderStatus.FAILED;
            errorDescription = ex.getMessage() + "\n" + ex;
            status = isCanceled ? ReaderStatus.CANCELED : ReaderStatus.FAILED;
            if (status == ReaderStatus.FAILED) throw new QueryExecutionException("Error trying to execute query", ex);
        }
    }

    private void processData(Connection connection) throws SQLException {
        var query = queryBuilder.getQuery(readerData.report());

        log.debug("Query for job " + readerData.jobId() + ": " + query);

        if (isCanceled) return;

        try (PreparedStatement statement = connection.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
        ) {
            runningStatement.set(statement);
            try (ResultSet resultSet = statement.executeQuery()
            ) {
                while (resultSet.next() && !isCanceled) {
                    processDataEntry(resultSet);
                }
            }
        }
    }

    @SneakyThrows(InterruptedException.class)
    private void processDataEntry(ResultSet resultSet) {


        var cacheRow = new CacheRow(
                DataSetTypeEnum.PROCEDURE.equalsIsLong(readerData.report().dataSetTypeId()) ?
                        getListCacheEntryForProcedure(resultSet)
                        :
                        getListCacheEntry(resultSet));

        while (!readerData.cache().offer(cacheRow) && !isCanceled) {
            //noinspection BusyWait
            Thread.sleep(10);
        }
        rowCount++;
        if (rowCount > maxRows)
            throw new ReportExportException("Превышено максимально допустимое количество строк отчета:" + maxRows);
    }

    private CacheEntry getCacheEntryFromResultSet(ResultSet resultSet, ReportFieldData field, int fieldNumber) {

        try {
            return new CacheEntry(field, resultSet.getString(fieldNumber));
        } catch (SQLException ex) {
            throw new QueryExecutionException("Error while fetching data", ex);
        }
    }

    private List<CacheEntry> getListCacheEntry(ResultSet resultSet) {
        final var fieldCounter = new AtomicInteger(1);
        return readerData.report().reportData().fields()
                .stream()
                .filter(ReportFieldData::visible)
                .map(field -> getCacheEntryFromResultSet(resultSet, field, fieldCounter.getAndIncrement()))
                .collect(Collectors.toList());
    }

    private List<CacheEntry> getListCacheEntryForProcedure(ResultSet resultSet) {
        final var fieldCounter = new AtomicInteger(1);
        return readerData.report().reportData().fields()
                .stream()
                .map(field -> getCacheEntryFromResultSet(resultSet, field, fieldCounter.getAndIncrement()))
                .filter(field -> field.fieldData().visible())
                .collect(Collectors.toList());

    }

    @Override
    public ReaderStatus getReaderStatus() {
        return status;
    }

    @Override
    public boolean isFinished() {
        return status.isFinalStatus();
    }

    @Override
    public boolean isFailed() {
        return status == ReaderStatus.FAILED;
    }

    @Override
    public long getRowCount() {
        return rowCount;
    }

    @Override
    public void cancel() {
        isCanceled = true;

        PreparedStatement currentStatement = runningStatement.get();
        if (currentStatement != null) {
            try {
                currentStatement.cancel();
            } catch (SQLException ignored) {
                log.debug("Job: " + readerData.jobId() + " was canceled.");
            }
        }
    }

    @Override
    public boolean isCanceled() {
        return status == ReaderStatus.CANCELED;
    }

    @Override
    public String getErrorDescription() {
        return errorDescription;
    }
}
