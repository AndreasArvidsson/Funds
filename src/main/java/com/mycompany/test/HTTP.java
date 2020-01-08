package com.mycompany.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Andreas Arvidsson
 */
public abstract class HTTP {

    public static <E> E get(final String url, final Class<E> type) throws MalformedURLException, IOException {
        final HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("GET");
        final ObjectMapper mapper = new ObjectMapper();
        final E res = mapper.readValue(con.getInputStream(), type);
        con.disconnect();
        return res;
    }

}
