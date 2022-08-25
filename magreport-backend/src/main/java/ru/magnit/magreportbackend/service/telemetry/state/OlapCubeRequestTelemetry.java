package ru.magnit.magreportbackend.service.telemetry.state;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OlapCubeRequestTelemetry implements TelemetryState {
    JOB_DATA_ACQUIRING("Job data acquired"),
    CUBE_DATA_ACQUIRING("Cube data acquired"),
    CUBE_DATA_FILTERING("Cube filtered"),
    MEASURES_COLLECTING("Measures collected"),
    METRICS_CALCULATION("Metrics calculated");

    private final String description;

}
