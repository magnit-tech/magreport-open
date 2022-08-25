package ru.magnit.magreportbackend.service.telemetry.state;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public interface TelemetryState {
    int ordinal();
    String getDescription();

    default TelemetryState[] getValues(){
        return this.getClass().getEnumConstants();
    }

    default Map<TelemetryState, Long> getStatesTree(){
        return Arrays.stream(this.getValues()).collect(Collectors.toConcurrentMap(s -> s, s -> 0L));
    }
}
