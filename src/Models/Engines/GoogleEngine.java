package Models.Engines;

import Abstract.WebEngine;
import Models.RequestData;
import Models.SearchResultsModels.SearchResultItem;
import org.jsoup.Connection;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GoogleEngine extends WebEngine {

    private List<SearchResultItem> results;
    public void getResultsFromGoogle(RequestData requestData) throws IOException {
        Connection.Response response = makeRequest(requestData);
        if(isValidResponse(response)) {
            parseResults(response.parse().body());
        }
    }

    private Element parseResults(Element body) {
        results = new ArrayList<>();
        Elements items = body.select("#res");

        if (items != null) {
            Elements resultDivs = items.select("div.g");
            //logService.LogMessage("Parsed: " + resultDivs.size() + " links");
            if (resultDivs.size() == 0) {
                System.out.println("Empty");
            }



            for (Element div : resultDivs) {
                SearchResultItem searchResultItem = new SearchResultItem(logService).parseInputDiv(div).initSearchExceptions(se).getItemSource();
                //searchResultItem.setCity(city);
                //searchResultItem.setCountry(country);
                if (searchResultItem.isItemCorrect()) {
                    results.add(searchResultItem);
                }
            }
            //logService.LogMessage(Results.size() + " results will be saved.");
            //logService.drawLine();
        }
        return this;
    }
}
