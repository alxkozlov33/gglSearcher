package Models;


import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {
    private List<SearchResultItem> Results;

    public SearchResult(Element body) {

        Results = new ArrayList<>();
        Elements items = body.select("div.section-result-text-content");
        System.out.println(items.size());
        System.out.println("Found " + items.size() + "elements");

//        Elements businessList = body.select("#rso > div:nth-child(1) > div > div > div:nth-child(2) > div > div:nth-child(4) > div > *");
//        if (businessList != null) {
//            for (Element item : businessList) {
//                Elements el = item.select("span");
//                String galleryName = "";
//                if (el.size() > 1 && el.get(0) != null) {
//                    galleryName = el.get(0).text();
//                }
//
//                String address = "";
//                if (el.size() > 2 && el.get(el.size() - 2) != null) {
//                    address = el.get(el.size() - 2).text();
//                }
//
//                SearchResultItem resultItem = new SearchResultItem(galleryName, address, "");
//                Results.add(resultItem);
//            }
//        }
        if (items != null) {
            Elements resultDivs = items.select("div.g");
            for (Element div : resultDivs) {
                SearchResultItem searchResultItem = new SearchResultItem(div);
                if (searchResultItem.isItemCorrect()) {
                    Results.add(new SearchResultItem(div));
                }
            }
        }
    }

    public List<SearchResultItem> getResults() {
        return Results;
    }
}