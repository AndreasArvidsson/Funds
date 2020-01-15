package com.github.andreasarvidsson.founds;

import com.github.andreasarvidsson.founds.util.HTTP;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
    private static final Map<String, AvanzaFound> FOUNDS = new HashMap();

    public static List<AvanzaFound> getFounds(final List<String> foundNames) throws IOException {
        final List<AvanzaFound> res = new ArrayList();
        for (final String foundName : foundNames) {
            res.add(getFound(foundName));
        }
        return res;
    }

    public static AvanzaFound getFound(final String name, final String... alternativeNames) throws IOException {
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
        throw new NoSuchElementException(String.format("Can't find Avanza found '%s'", name));
    }

    private static AvanzaFound getFoundInner(final String name) throws IOException {
        if (!FOUNDS.containsKey(name)) {
            final String id = getId(name);
            final AvanzaFound found = HTTP.get(String.format("%s/fund/guide/%s", BASE, id),
                    AvanzaFound.class
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
