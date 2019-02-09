package Models;


import Services.LogService;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {
    private List<SearchResultItem> Results;

    private LogService logService;
    private String city;

    private SearchExceptions se;

    public SearchResult(LogService logService) {
        this.logService = logService;
        Results = new ArrayList<>();
    }

    public SearchResult initSearchExceptions(SearchExceptions se) {
        this.se = se;
        return this;
    }

    public SearchResult parsePageBody(Element body) {
        Elements items = body.select("div#links");

        if (items == null) {
            return null;
        }
        Elements resultDivs = items.select("div.web-result");

        System.out.println("___________________________________________________________________");
        for (Element div : resultDivs) {
            SearchResultItem searchResultItem = new SearchResultItem(logService).parseInputDiv(div).initSearchExceptions(se).getItemSource()
                    ;
            searchResultItem.setCity(city);
            if (searchResultItem.isItemCorrect()) {
                Results.add(new SearchResultItem(logService));
            }
        }
        System.out.println("___________________________________________________________________");
        return this;
    }

    public SearchResult initCity(String city) {
        this.city = city;
        return this;
    }

    public List<SearchResultItem> getResults() {
        return Results;
    }
}