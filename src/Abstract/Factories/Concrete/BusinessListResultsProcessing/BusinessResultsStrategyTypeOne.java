package Abstract.Factories.Concrete.BusinessListResultsProcessing;

import Abstract.Models.SearchResultModels.BusinessListSearchResultItem;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;

// "All results" page
public class BusinessResultsStrategyTypeOne implements IBusinessResultItemsProcess {

    @Override
    public List<BusinessListSearchResultItem> processBody(Element body) {

        ArrayList<BusinessListSearchResultItem> results = new ArrayList<>();
        Elements items = body.select("#ires");

        Element listContainer = items.select("h2:contains(Local Results)").first().parent();
        Elements namesElements = listContainer.select("div[aria-level=3][role=heading]");
        Elements addressesElements = listContainer.select(".rllt__details").select("span:not([class]):not([style]):not(:contains(Their website mentions))");

        for (int i = 0; i < addressesElements.size(); i++) {
            String mainHeader = namesElements.get(i).text();
            String city = addressesElements.get(i).text().split(",")[0];
            String description = "Item scraped from business list";
            BusinessListSearchResultItem regularSearchResultItem = new BusinessListSearchResultItem(mainHeader, "", description, city);
            results.add(regularSearchResultItem);
        }
        return results;

        //TODO: Select google maps items in future updates
        //String linkToMapsList = items.select("a[href^='https://maps.google.com/maps?']").attr("href");
        //WebUrlEngine webUrlEngine = new WebUrlEngine();
        //Element mapsBody = webUrlEngine.getWebSourceData(new RequestData(linkToMapsList));
        //GoogleMapsResultsProcessor googleMapsResultsProcessor = new GoogleMapsResultsProcessor();
        //List businessList = googleMapsResultsProcessor.processBody(mapsBody);
    }
}
