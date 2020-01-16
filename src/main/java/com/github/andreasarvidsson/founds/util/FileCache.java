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

    static {
        final File dir = new File(DIR);
        //Threshold is 12 hours from now.
        final long threshold = System.currentTimeMillis() - 12 * MILLIS_PER_HOUR;
        //Clear cache from old files.
        if (dir.exists()) {
            for (final File f : dir.listFiles()) {
                //Delete if file is older than threshold
                if (f.lastModified() < threshold) {
                    f.delete();
                }
            }
        }
        //Create cache directory.
        else {
            dir.mkdirs();
        }
    }

    public static void store(final String fileName, final Object data) throws IOException {
        final File file = getFile(fileName);
        final JsonFactory jsonFactory = new JsonFactory();
        try (final JsonGenerator jsonGen = jsonFactory.createGenerator(file, JsonEncoding.UTF8)) {
            jsonGen.setCodec(MAPPER);
            jsonGen.writeObject(data);
        }
    }

    public static <E> E load(final String fileName, final Class<E> type) throws IOException {
        final File file = getFile(fileName);
        if (file.exists()) {
            try (final InputStream is = new FileInputStream(file)) {
                return MAPPER.readValue(is, type);
            }
        }
        return null;
    }

    private static File getFile(final String fileName) {
        return new File(String.format("%s/%s.json", DIR, fileName));
    }

}
