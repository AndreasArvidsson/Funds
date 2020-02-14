package com.github.andreasarvidsson.founds.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Andreas Arvidsson
 */
public abstract class HTTP {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final int TIMEOUT = 30 * 1000; //30sec

    public static <E> E get(final String url, final Class<E> type) throws IOException {
        final HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("GET");
        final E res = MAPPER.readValue(con.getInputStream(), type);
        con.disconnect();
        return res;
    }

    public static <E> List<E> getList(final String url, final Class<E> type) throws IOException {
        final HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("GET");
        final List<E> res = MAPPER.readValue(
                con.getInputStream(),
                MAPPER.getTypeFactory().constructCollectionType(List.class, type)
        );
        con.disconnect();
        return res;
    }

    public static Document getDocument(final String url) throws IOException {
        return Jsoup.parse(new URL(url), TIMEOUT);
    }

}
