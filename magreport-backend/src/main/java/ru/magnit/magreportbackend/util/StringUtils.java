package ru.magnit.magreportbackend.util;

import org.springframework.security.crypto.codec.Hex;

import java.nio.charset.StandardCharsets;

public interface StringUtils {

    static String getFirstName(String fullName) {

        return getNamePart(fullName, 1);
    }

    static String getPatronymic(String fullName) {

        return getNamePart(fullName, 2);
    }

    static String getLastName(String fullName) {

        return getNamePart(fullName, 0);
    }

    static String getUTF8String(String source) {
        byte[] buffer = source.getBytes(StandardCharsets.UTF_8);
        final var utf8FileName = new String(Hex.encode(buffer));
        var counter = 0;
        StringBuilder result = new StringBuilder();
        for (String letter : utf8FileName.split("")) {
            if (counter++ % 2 == 0) {
                result.append("%");
            }
            result.append(letter);
        }
        return result.toString();
    }

    static String max(String o1, String o2){
        if (o1.compareTo(o2) > 0)
            return o1;
        else
            return o2;
    }

    static String min(String o1, String o2){
        if (o1.compareTo(o2) < 0)
            return o1;
        else
            return o2;
    }

    private static String getNamePart(String fullName, int partNumber) {

        if (fullName == null || fullName.isEmpty()) return "";

        final var nameParts = fullName.split(" ");

        if (nameParts.length != 3) return "";

        return nameParts[partNumber];
    }
}
