package Abstract.Factories.Concrete.BusinessListResultsProcessing;

import Abstract.Models.SearchResultModels.BusinessListSearchResultItem;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;

//Regular simple page
public class BusinessResultsStrategyTypeTwo implements IBusinessResultItemsProcess {
    @Override
    public List<BusinessListSearchResultItem> processBody(Element body) {
        ArrayList<BusinessListSearchResultItem> results = new ArrayList<>();
        Elements items = body.select("#rso");
        Elements container = items.select("h2:contains(Local Results)");
        if (container.size() > 0) {
            Element listContainer = container.first().parent();
            Elements namesElements = listContainer.select("div[aria-level=3][role=heading]");
            Elements addressesElements = listContainer.select(".rllt__details").select("span:not([class]):not([style]):not(:contains(Their website mentions))");

            for (int i = 0; i < namesElements.size(); i++) {
                String mainHeader = namesElements.get(i).text();
                String city = addressesElements.get(i).text().split(",")[0];
                String country = addressesElements.get(i).text().split(",")[1];
                String description = "Item scraped from business list";
                BusinessListSearchResultItem regularSearchResultItem = new BusinessListSearchResultItem(mainHeader, "", description, city, country);
                results.add(regularSearchResultItem);
            }
        }
        return results;
    }
}
