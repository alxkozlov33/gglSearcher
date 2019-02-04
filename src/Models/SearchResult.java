package Models;


import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {
    List<SearchResultItem> Results;

    public SearchResult(Element body) {

        Results = new ArrayList<>();
        Elements items = body.select("#res");
        Elements str = body.select("#rso > div:nth-child(1) > div > div > div:nth-child(2) > div > div:nth-child(4) > div");
        if (items != null) {
            Elements resultDivs = items.select("div.g");
            for (Element div : resultDivs) {
                Results.add(new SearchResultItem(div));
            }
        }
        System.out.println("Results: "+Results.size());
        System.out.println();
    }

    public  List<SearchResultItem> getResults() {
        return Results;
    }
}