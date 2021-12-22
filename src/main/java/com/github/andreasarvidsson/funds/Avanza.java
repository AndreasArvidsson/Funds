package com.github.andreasarvidsson.funds;

import com.github.andreasarvidsson.funds.util.HTTP;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.andreasarvidsson.funds.util.FileCache;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

    public static AvanzaFund getFund(final String name, final String... alternativeNames) throws IOException {
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
        throw new NoSuchElementException(String.format("Can't find Avanza fund '%s'", name));
    }

    private static AvanzaFund getFundByName(final String name) throws IOException {
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
        final String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
        final SearchResults searchResult = HTTP.get(
                String.format(
                        "%s/_cqbe/search/global-search/global-search-template?query=%s",
                        BASE, encodedName
                ),
                SearchResults.class
        );
        for (final SearchResultsGroup group : searchResult.resultGroups) {
            for (final SearchResultsHit hit : group.hits) {
                if (name.equalsIgnoreCase(hit.link.linkDisplay)) {
                    return hit.link.orderbookId;
                }
            }
        }
        throw new NoSuchElementException();
    }

}

@JsonIgnoreProperties(ignoreUnknown = true)
class SearchResults {

    public int totalNumberOfHits;
    public String searchQuery, urlEncodedSearchQuery;
    public List<SearchResultsGroup> resultGroups;

}

@JsonIgnoreProperties(ignoreUnknown = true)
class SearchResultsGroup {

    public List<SearchResultsHit> hits;
    public int numberOfHits;

}

@JsonIgnoreProperties(ignoreUnknown = true)
class SearchResultsHit {

    public SearchResultsHitLink link;
}

@JsonIgnoreProperties(ignoreUnknown = true)
class SearchResultsHitLink {

    public String orderbookId, linkDisplay;
}
