package com.github.andreasarvidsson.founds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    private static final String BASE = "https://www.avanza.se/_cqbe";
    private static final Map<String, Found> FOUNDS = new HashMap();

    public static Found getFound(final String name) throws IOException {
        if (!FOUNDS.containsKey(name)) {
            final String id = getId(name);
            final Found found = HTTP.get(
                    String.format("%s/fund/guide/%s", BASE, id),
                    Found.class
            );
            FOUNDS.put(name, found);
        }
        return FOUNDS.get(name);
    }

    private static String getId(final String name) throws IOException {
        final String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
        final SearchResults searchResult = HTTP.get(
                String.format(
                        "%s/search/global-search/global-search-template?query=%s",
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
        throw new NoSuchElementException(String.format("Can't find ID for '%s'", name));
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
