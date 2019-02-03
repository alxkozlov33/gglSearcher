package Models;


import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {
    List<SearchResultItem> Results;

    public SearchResult(Element body) {
        if (body == null || body.text().toLowerCase().contains("The document has moved")){
            System.out.println("Body is null");
        }
        Results = new ArrayList<>();
        Elements items = body.select("#res");
        Elements str = body.select("#rso > div:nth-child(1) > div > div > div:nth-child(2) > div > div:nth-child(4) > div");
        if (items != null) {
            Elements resultDivs = items.select("div.g");
            for (Element div : resultDivs) {
                SearchResultItem item = new SearchResultItem(div);
                if (item.getSearchedLink().toLowerCase().contains("instagram.") || item.getSearchedLink().toLowerCase().contains("ig.")) {
                    Results.add(new SearchResultItem(div));
                    System.out.println("Result item: " + div);
                    System.out.println("_____________________");
                }
            }
        }
        System.out.println("Results: "+Results.size());
        System.out.println();
    }

    public  List<SearchResultItem> getResults() {
        return Results;
    }
}