package Abstract.Factory;

import Abstract.GoogleSearchResultItem;
import org.jsoup.nodes.Element;

import java.util.List;

public abstract class GoogleSearchResultsFactory {

    public abstract GoogleSearchResultItem getResults();

    public List<GoogleSearchResultItem> buildResults(Element body) {
//        ArrayList<GoogleSearchResultItem> Result = new ArrayList<>();
//        Elements items = body.select("#res");
//
//        if (items != null) {
//            Elements resultDivs = items.select("div.g");
//            //logService.LogMessage("Parsed: " + resultDivs.size() + " links");
//            if (resultDivs.size() == 0) {
//                System.out.println("Empty");
//            }
//
//            for (Element div : resultDivs) {
//                SearchResultItem searchResultItem = new SearchResultItem(logService).parseInputDiv(div).initSearchExceptions(se).getItemSource();
//                searchResultItem.setCity(city);
//                searchResultItem.setCountry(country);
//                if (searchResultItem.isItemCorrect()) {
//                    Result.add(searchResultItem);
//                }
//            }
//            logService.LogMessage(Results.size() + " results will be saved.");
//            logService.drawLine();
//        }
        return null;
    }
}
