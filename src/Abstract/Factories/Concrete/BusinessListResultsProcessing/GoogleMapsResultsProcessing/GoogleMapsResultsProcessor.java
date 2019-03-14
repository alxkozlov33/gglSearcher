package Abstract.Factories.Concrete.BusinessListResultsProcessing.GoogleMapsResultsProcessing;

import Abstract.Factories.Concrete.BusinessListResultsProcessing.BusinessResultItemsProcess;
import Abstract.Models.SearchResultModels.BusinessListSearchResultItem;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;

public class GoogleMapsResultsProcessor extends BusinessResultItemsProcess {
    @Override
    public List<BusinessListSearchResultItem> processBody(Element body) {
        ArrayList<BusinessListSearchResultItem> results = new ArrayList<>();
        Elements items = body.select("div.section-result-text-content");

        for (Element div : items) {
            String mainHeader = div.select("div.section-result-title").text();
            String link = "";
            String description = "";
            String city = div.select("div.section-result-location").text();

            BusinessListSearchResultItem regularSearchResultItem = new BusinessListSearchResultItem(mainHeader, "http://www.google.com" + link, description, city);
            results.add(regularSearchResultItem);
        }
        return results;
    }
}
