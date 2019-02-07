package Models;


import Services.LogService;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {
    private List<SearchResultItem> Results;

    private static LogService logService;

    public SearchResult(Element body, LogService logService) {
        this.logService = logService;
        Results = new ArrayList<>();
        Elements items = body.select("div#links");

        if (items != null) {
            Elements resultDivs = items.select("div.web-result");

            System.out.println("___________________________________________________________________");
            for (Element div : resultDivs) {
                SearchResultItem searchResultItem = new SearchResultItem(div, logService);
                if (searchResultItem.isItemCorrect()) {
                    Results.add(new SearchResultItem(div, logService));
                }
            }
            System.out.println("___________________________________________________________________");
        }
    }

    public List<SearchResultItem> getResults() {
        return Results;
    }
}