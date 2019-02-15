package Models;


import Services.LogService;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {
    private List<SearchResultItem> Results;

    private LogService logService;
    private String city;
    private String country;

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
        Results = new ArrayList<>();
        Elements items = body.select("#res");

        if (items != null) {
            Elements resultDivs = items.select("div.g");
            logService.LogMessage("Parsed: " + resultDivs.size() + " links");
            if (resultDivs.size() == 0) {
                System.out.println("Empty");
            }

            for (Element div : resultDivs) {
                SearchResultItem searchResultItem = new SearchResultItem(logService).parseInputDiv(div).initSearchExceptions(se).getItemSource();
                searchResultItem.setCity(city);
                searchResultItem.setCountry(country);
                if (searchResultItem.isItemCorrect()) {
                    Results.add(searchResultItem);
                }
            }
            logService.LogMessage(Results.size() + " results will be saved.");
            logService.drawLine();
        }
        return this;
    }

    public SearchResult initCountry(String country) {
        this.country = country;
        return this;
    }

    public SearchResult initCity(String city) {
        this.city = city;
        return this;
    }

    public List<SearchResultItem> getResults() {
        return Results;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}