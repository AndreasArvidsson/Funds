package com.github.andreasarvidsson.founds.util;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Andreas Arvidsson
 */
public abstract class FileCache {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String DIR = "cache";
    private static final long MILLIS_PER_HOUR = 3600000;

    public static void store(final String fileName, final Object data) throws IOException {
        final File file = getFile(fileName);
        file.getParentFile().mkdirs();
        final JsonFactory jsonFactory = new JsonFactory();
        try (final JsonGenerator jsonGen = jsonFactory.createGenerator(file, JsonEncoding.UTF8)) {
            jsonGen.setCodec(MAPPER);
            jsonGen.writeObject(data);
        }
    }

    public static <E> E load(final String fileName, final Class<E> type) throws IOException {
        final File file = getFile(fileName);
        if (file.exists()) {
            if (currentFile(file)) {
                try (final InputStream is = new FileInputStream(file)) {
                    return MAPPER.readValue(is, type);
                }
            }
            else {
                file.delete();
            }
        }
        return null;
    }

    private static File getFile(final String fileName) {
        return new File(String.format("%s/%s.json", DIR, fileName));
    }

    private static boolean currentFile(final File file) {
        //True if timestamp is newer than 12 hours.
        return System.currentTimeMillis() - file.lastModified() <= 12 * MILLIS_PER_HOUR;
    }

}
