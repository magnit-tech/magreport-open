package ru.magnit.magreportbackend.service.telemetry;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.service.telemetry.state.TelemetryState;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Getter
@RequiredArgsConstructor
enum TelemetryTestEnum implements TelemetryState {
    TEST_STATE_1("State 1 completed"),
    TEST_STATE_2("State 2 completed"),
    TEST_STATE_3("State 3 completed");

    private final String description;
}

@Slf4j
@ExtendWith(MockitoExtension.class)
class TelemetryServiceTest {

    @InjectMocks
    private TelemetryService service;

    @Test
    void testTelemetryService() {
        final var serial = service.init(TelemetryTestEnum.TEST_STATE_1);

        pause(100);

        service.setState(serial, TelemetryTestEnum.TEST_STATE_1);
        assertEquals(TelemetryTestEnum.TEST_STATE_1, service.getCurrentState(serial));
        pause(50);

        service.setState(serial, TelemetryTestEnum.TEST_STATE_2);
        assertEquals(TelemetryTestEnum.TEST_STATE_2, service.getCurrentState(serial));
        pause(50);

        service.setState(serial, TelemetryTestEnum.TEST_STATE_3);
        assertEquals(TelemetryTestEnum.TEST_STATE_3, service.getCurrentState(serial));

        pause(50);

        service.logTimings(serial);

        service.clear(serial);
    }

    @SneakyThrows
    @Test
    void testMultiThreaded(){
        final var thread1 = new Thread(this::testTelemetryService);
        final var thread2 = new Thread(this::testTelemetryService);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        assertFalse(thread1.isAlive());
        assertFalse(thread2.isAlive());
    }

    @SneakyThrows
    @SuppressWarnings("java:S2925")
    private void pause(long millis) {
        Thread.sleep(millis);
    }
}
