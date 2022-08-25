package ru.magnit.magreportbackend.util;

import lombok.SneakyThrows;

public interface MemoryUtils {

    @SneakyThrows
    static long getTotalFreeMemory(){
        var runtime = Runtime.getRuntime();
        System.gc();
        System.runFinalization();
        Thread.sleep(1000);

        return runtime.totalMemory() - runtime.freeMemory();
    }
}
