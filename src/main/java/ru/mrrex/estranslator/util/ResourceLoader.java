package ru.mrrex.estranslator.util;

import java.io.InputStream;

public abstract class ResourceLoader {

    private ResourceLoader() {}

    private static InputStream getStreamViaContextClassLoader(String path) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        if (classLoader == null)
            return null;

        return classLoader.getResourceAsStream(path);
    }

    private static InputStream getStreamViaClassLoader(String path) {
        ClassLoader classLoader = ResourceLoader.class.getClassLoader();

        if (classLoader == null)
            return null;

        return classLoader.getResourceAsStream(path);
    }

    public static InputStream getResourceAsStream(String path) {
        InputStream inputStream = getStreamViaContextClassLoader(path);

        if (inputStream != null)
            return inputStream;

        inputStream = getStreamViaClassLoader(path);

        if (inputStream != null)
            return inputStream;

        return getResourceAsStream('/' + path);
    }
}
