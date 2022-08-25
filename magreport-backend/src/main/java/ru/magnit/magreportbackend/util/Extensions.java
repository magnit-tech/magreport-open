package ru.magnit.magreportbackend.util;

import java.util.ArrayList;
import java.util.List;

public interface Extensions {

    static <T> T defaultIfNull(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    static <T> List<T> initList(int size){
        final var result = new ArrayList<T>(size);
        for (int i = 0; i < size; i++) {
            result.add(null);
        }
        return result;
    }
}
