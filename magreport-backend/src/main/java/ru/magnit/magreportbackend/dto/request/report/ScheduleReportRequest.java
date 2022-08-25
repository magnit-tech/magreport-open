package ru.magnit.magreportbackend.dto.request.report;

public record ScheduleReportRequest (
    Long id,
    Long scheduleTaskId
){}
