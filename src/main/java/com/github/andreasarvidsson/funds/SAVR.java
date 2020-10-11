package com.github.andreasarvidsson.funds;

import com.github.andreasarvidsson.funds.util.HTTP;
import com.github.andreasarvidsson.funds.util.FileCache;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author Andreas Arvidsson
 */
public abstract class SAVR {

    private static final String BASE = "https://savr.com/sv";
    private static final Map<String, SavrFund> FUNDS = new HashMap();
    private static Map<String, String> URLS_MAP;

    public static SavrFund getFund(final String name, final String... alternativeNames) throws IOException {
        try {
            return getFundByName(name);
        }
        catch (final NoSuchElementException e) {
        }
        for (int i = 0; i < alternativeNames.length; ++i) {
            try {
                return getFundByName(alternativeNames[i]);
            }
            catch (final NoSuchElementException e) {
            }
        }
        throw new NoSuchElementException(String.format("Can't find SAVR fund '%s'", name));
    }

    private static SavrFund getFundByName(final String name) throws IOException {
        if (!FUNDS.containsKey(name)) {
            final String fileName = String.format("savr_%s", name);
            SavrFund fund = FileCache.load(fileName, SavrFund.class);
            if (fund == null) {
                final String url = getUrl(name);
                fund = getFundByURL(url);
                FileCache.store(fileName, fund);
            }
            FUNDS.put(name, fund);
        }
        return FUNDS.get(name);
    }

    private static SavrFund getFundByURL(final String url) throws IOException {
        final SavrFund res = new SavrFund();
        final Document doc = HTTP.getDocument(url);
        final Element detailsNode = doc.select("div.fundDetails").first();
        final Element feeNode = detailsNode.child(0).child(1);
        final String feeText = feeNode.html().replace("&nbsp;%", "").replace(",", ".");
        res.fee = Double.parseDouble(feeText);
        return res;
    }

    private static String getUrl(final String name) throws IOException {
        if (URLS_MAP == null) {
            URLS_MAP = getUrlMap();
        }
        if (URLS_MAP.containsKey(name)) {
            return URLS_MAP.get(name);
        }
        throw new NoSuchElementException();
    }

    private static Map<String, String> getUrlMap() throws IOException {
        final Map<String, String> res = new HashMap();
        final Document doc = HTTP.getDocument(String.format(
                "%s/fondutbud", BASE
        ));
        final Element div = doc.select("div.fundList").first();
        for (final Element a : div.children()) {
            res.put(
                    a.select(".fundListItem__name").first().html(),
                    a.attr("href")
            );
        }
        return res;
    }

}
