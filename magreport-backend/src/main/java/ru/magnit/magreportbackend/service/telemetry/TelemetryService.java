package ru.magnit.magreportbackend.service.telemetry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.magnit.magreportbackend.service.telemetry.counter.TelemetryCounter;
import ru.magnit.magreportbackend.service.telemetry.state.TelemetryState;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TelemetryService {

    private static final double MILLI = 1_000_000D;
    private static final AtomicLong instanceCounter = new AtomicLong(0);

    private final Map<Long, TelemetryState> currentStates = new ConcurrentHashMap<>();
    private final Map<Long, Map<TelemetryState, Long>> stateTimes = new ConcurrentHashMap<>();
    private final Map<Long, Map<TelemetryState, Long>> timers = new ConcurrentHashMap<>();
    private final Map<Long, Map<TelemetryCounter, Long>> counters = new ConcurrentHashMap<>();

    public long init(TelemetryState state) {
        final var serialNumber = instanceCounter.getAndIncrement();
        stateTimes.put(serialNumber, state.getStatesTree());
        timers.put(serialNumber, state.getStatesTree());
        return serialNumber;
    }

    public void setState(long serialNumber, TelemetryState newState) {
        if (!stateTimes.containsKey(serialNumber))
            throw new IllegalArgumentException("Call init before setting state");
        if (currentStates.get(serialNumber) == newState) return;

        final var oldState = currentStates.get(serialNumber);
        if (oldState != null) {
            stateTimes.get(serialNumber).put(oldState, stateTimes.get(serialNumber).get(oldState) + System.nanoTime() - timers.get(serialNumber).get(oldState));
        }

        timers.get(serialNumber).put(newState, System.nanoTime());
        currentStates.put(serialNumber, newState);
    }

    public TelemetryState getCurrentState(long serialNumber) {
        return currentStates.get(serialNumber);
    }

    public void clear(long serialNumber) {
        currentStates.remove(serialNumber);
        stateTimes.remove(serialNumber);
        timers.remove(serialNumber);
        counters.remove(serialNumber);
    }

    public void logTimings(long serialNumber) {
        final var currentState = currentStates.get(serialNumber);
        final var currentTime = System.nanoTime();

        log.debug("\nTimings for series: " + serialNumber + "\n" +
                "Current state: " + currentStates.get(serialNumber) + "\n" +
                "{}", stateTimes.get(serialNumber)
                .entrySet()
                .stream()
                .sorted(Comparator.comparingInt(value -> value.getKey().ordinal()))
                .map(e -> e.getKey().getDescription() +
                        " (" + e.getKey() + "):" +
                        (e.getKey() == currentState ?
                        ((currentTime - timers.get(serialNumber).get(currentStates.get(serialNumber))) / MILLI) + " ms\n" :
                        (e.getValue() / MILLI) + " ms"))
                .collect(Collectors.joining("\n")));
    }
}
