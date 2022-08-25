package ru.magnit.magreportbackend.util;

public class FileUtils {

    private static final String USER_HOME = "user.home";

    private FileUtils() {
    }

    public static String replaceHomeShortcut(String sourcePath) {
        return sourcePath.replaceFirst("^~", System.getProperty(USER_HOME).replace("\\", "/"));
    }

}
