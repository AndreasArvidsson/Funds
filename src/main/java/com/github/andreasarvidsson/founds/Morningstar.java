package com.github.andreasarvidsson.founds;

import com.github.andreasarvidsson.founds.util.HTTP;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.andreasarvidsson.founds.util.FileCache;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

/**
 *
 * @author Andreas Arvidsson
 */
public abstract class Morningstar {

    public static boolean DISABLE = false;

    private static final String BASE = "https://www.morningstar.se/se";
    private static final Map<String, MorningstarFound> FOUNDS = new HashMap();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static MorningstarFound getFound(final String name, final String... alternativeNames) throws IOException {
        if (DISABLE) {
            return null;
        }
        try {
            return getFoundByName(name);
        }
        catch (final NoSuchElementException e) {
        }
        for (int i = 0; i < alternativeNames.length; ++i) {
            try {
                return getFoundByName(alternativeNames[i]);
            }
            catch (final NoSuchElementException e) {
            }
        }
        throw new NoSuchElementException(String.format("Can't find Morningstar found '%s'", name));
    }

    private static MorningstarFound getFoundByName(final String name) throws IOException {
        if (!FOUNDS.containsKey(name)) {
            final String fileName = String.format("morningstar_%s", name);
            MorningstarFound found = FileCache.load(fileName, MorningstarFound.class);
            if (found == null) {
                final String id = getId(name);
                found = getFoundByID(id);
                FileCache.store(fileName, found);
            }
            FOUNDS.put(name, found);
        }
        return FOUNDS.get(name);
    }

    private static MorningstarFound getFoundByID(final String id) throws IOException {
        final MorningstarFound res = new MorningstarFound();
        final Document doc = HTTP.getDocument(String.format(
                "%s/funds/snapshot/snapshot.aspx?id=%s&tab=3",
                BASE, id
        ));

        final Element div = doc.selectFirst("#portfolioEquityStyleDiv");
        final Element td = div.selectFirst("td.data");
        final Element table = td.select("table").get(1);
        final Elements rows = table.select("tr");
        rows.forEach(row -> {
            final Elements cells = row.children();
            if (cells.size() != 2) {
                return;
            }
            final String title = cells.get(0).text();
            final String value = cells.get(1).text().replace(",", ".");
            if (value.equals("-")) {
                return;
            }
            switch (title) {
                case "Giganter":
                case "Stora":
                    res.largeCompanies += Double.parseDouble(value);
                    break;
                case "Medel":
                    res.middleCompanies += Double.parseDouble(value);
                    break;
                case "Små":
                case "Mikro":
                    res.smallCompanies += Double.parseDouble(value);
            }
        });

        return res;
    }

    private static String getId(final String name) throws IOException {
        final String encodedName = URLEncoder
                .encode(name, StandardCharsets.UTF_8.toString())
                .toLowerCase();

        final Document doc = HTTP.getDocument(
                String.format(
                        "%s/util/SecuritySearch.ashx?q=%s",
                        BASE, encodedName
                ));
        final Element body = doc.getElementsByTag("body").first();

        for (final Node node : body.childNodes()) {
            final String str = node.toString().trim();
            final String[] parts = str.split("\\|");
            if (parts.length > 1 && name.equals(parts[0])) {
                final String jsonStr = parts[1];
                final JsonNode json = MAPPER.readTree(jsonStr);
                return json.get("i").asText();
            }
        }

        throw new NoSuchElementException();
    }

}
