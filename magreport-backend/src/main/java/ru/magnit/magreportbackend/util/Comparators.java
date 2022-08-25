package ru.magnit.magreportbackend.util;

import java.util.Comparator;
import java.util.List;

public interface Comparators {

    static Comparator<List<String>> listOfStringsComparator() {
        return (o1, o2) -> {
            for (var i = 0; i < Math.min(o1.size(), o2.size()); i++) {
                if (o1.get(i) == null && o2.get(i) != null) return -1;
                if ((o1.get(i) != null && o2.get(i) == null)) return 1;
                final var result = (o1.get(i) == null && o2.get(i) == null) ? 0 : o1.get(i).compareTo(o2.get(i));
                if (result != 0) return result;
            }
            return Integer.compare(o1.size(), o2.size());
        };
    }
}
