package com.github.andreasarvidsson.funds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.andreasarvidsson.funds.util.FileCache;
import com.github.andreasarvidsson.funds.util.HTTP;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 *
 * @author Andreas Arvidsson
 */
public abstract class Avanza {

    private static final String BASE = "https://www.avanza.se";
    private static final Map<String, AvanzaFund> FUNDS = new HashMap<>();

    public static AvanzaFund getFund(
        final String name,
        final String... alternativeNames
    ) throws IOException {
        try {
            return getFundByName(name);
        } catch (final NoSuchElementException e) {}
        for (int i = 0; i < alternativeNames.length; ++i) {
            try {
                return getFundByName(alternativeNames[i]);
            } catch (final NoSuchElementException e) {}
        }
        throw new NoSuchElementException(
            String.format("Can't find Avanza fund '%s'", name)
        );
    }

    private static AvanzaFund getFundByName(final String name)
        throws IOException {
        if (!FUNDS.containsKey(name)) {
            final String fileName = String.format("avanza_%s", name);
            AvanzaFund fund = FileCache.load(fileName, AvanzaFund.class);
            if (fund == null) {
                final String id = getId(name);
                fund = HTTP.get(
                    String.format("%s/_api/fund-guide/guide/%s", BASE, id),
                    AvanzaFund.class
                );
                FileCache.store(fileName, fund);
            }
            fund.compile();
            FUNDS.put(name, fund);
        }
        return FUNDS.get(name);
    }

    private static String getId(final String name) throws IOException {
        final Map<String, String> body = new HashMap<>();
        body.put("query", name);
        final SearchResults searchResult = HTTP.post(
            String.format("%s/_api/search/filtered-search", BASE),
            body,
            SearchResults.class
        );
        for (final SearchResultsHit hit : searchResult.hits) {
            if (name.equalsIgnoreCase(hit.title)) {
                return hit.orderBookId;
            }
        }
        throw new NoSuchElementException();
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class SearchResults {

    public int totalNumberOfHits;
    public String searchQuery;
    public List<SearchResultsHit> hits;
}

@JsonIgnoreProperties(ignoreUnknown = true)
class SearchResultsHit {

    public String title, orderBookId;
}
