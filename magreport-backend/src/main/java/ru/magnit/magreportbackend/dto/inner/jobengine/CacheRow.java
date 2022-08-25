package ru.magnit.magreportbackend.dto.inner.jobengine;


import java.util.List;

public record CacheRow(
        List<CacheEntry> entries
) {
}
