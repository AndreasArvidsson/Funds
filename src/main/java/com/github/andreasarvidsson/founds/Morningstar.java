package com.github.andreasarvidsson.founds;

import com.github.andreasarvidsson.founds.util.HTTP;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.andreasarvidsson.founds.util.FileCache;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author Andreas Arvidsson
 */
public abstract class Morningstar {

    private static final String BASE = "https://www.morningstar.se";
    private static final Map<String, MorningstarFound> FOUNDS = new HashMap();

    public static MorningstarFound getFound(final String name, final String... alternativeNames) throws IOException {
        try {
            return getFoundInner(name);
        }
        catch (final NoSuchElementException e) {
        }
        for (int i = 0; i < alternativeNames.length; ++i) {
            try {
                return getFoundInner(alternativeNames[i]);
            }
            catch (final NoSuchElementException e) {
            }
        }
        throw new NoSuchElementException(String.format("Can't find Morningstar found '%s'", name));
    }

    private static MorningstarFound getFoundInner(final String name) throws IOException {
        if (!FOUNDS.containsKey(name)) {
            final String fileName = String.format("morningstar_%s", name);
            MorningstarFound found = FileCache.load(fileName, MorningstarFound.class);
            if (found == null) {
                final String id = getId(name);
                final Document doc = HTTP.getDocument(String.format(
                        "%s/Funds/Quicktake/Portfolio.aspx?perfid=%s",
                        BASE, id
                ));
                found = new MorningstarFound();
                found.largeCompanies = getCompanySize(doc, "Stora bolag");
                found.middleCompanies = getCompanySize(doc, "Medelstora bolag");
                found.smallCompanies = getCompanySize(doc, "Små bolag");
                FileCache.store(fileName, found);
            }
            FOUNDS.put(name, found);
        }
        return FOUNDS.get(name);
    }

    private static double getCompanySize(final Document doc, final String title) {
        final Element span = doc.select(String.format("span[title='%s']", title)).first();
        final String text = span.child(0).text();
        if (text.equals("-")) {
            return 0.0;
        }
        return Double.parseDouble(text.replace(",", "."));
    }

    private static String getId(final String name) throws IOException {
        final String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
        final List<SearchResult> searchResult = HTTP.getList(
                String.format(
                        "%s/Handlers/FundSearchHandler.ashx?s=%s",
                        BASE, encodedName
                ),
                SearchResult.class);
        for (final SearchResult result : searchResult) {
            if (name.equalsIgnoreCase(result.f)) {
                return result.p;
            }
        }

//        Web scraper alternative.
//        final Document doc = getDocument(String.format(
//                "%s/Funds/Quickrank.aspx?search=%s",
//                 BASE, encodedName
//        ));
//        final Elements rows = doc.select("table.rgMasterTable > tbody > tr");
//        for (final Element tr : rows) {
//            if (!tr.hasClass("rgNoRecords")) {
//                final Element td = tr.child(2);
//                if (name.equalsIgnoreCase(td.text())) {
//                    final String url = td.child(0).attr("href");
//                    final int i1 = url.indexOf("perfid=") + 7;
//                    final int i2 = url.indexOf("&", i1);
//                    return url.substring(i1, i2);
//                }
//            }
//        }
//
        throw new NoSuchElementException();
    }

}

@JsonIgnoreProperties(ignoreUnknown = true)
class SearchResult {

    public String p, f;

}
