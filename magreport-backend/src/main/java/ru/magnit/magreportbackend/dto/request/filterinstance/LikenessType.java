package ru.magnit.magreportbackend.dto.request.filterinstance;

public enum LikenessType {
    STARTS,
    CONTAINS,
    ENDS;

    public boolean check(String searchString, String destination) {

        final var src = searchString.trim().toLowerCase();
        final var dst = destination.trim().toLowerCase();

        return switch (this) {
            case STARTS -> dst.startsWith(src);
            case CONTAINS -> dst.contains(src);
            case ENDS -> dst.endsWith(src);
        };
    }
}
