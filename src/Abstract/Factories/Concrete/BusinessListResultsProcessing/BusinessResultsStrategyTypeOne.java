package Abstract.Factories.Concrete.BusinessListResultsProcessing;

import Abstract.Models.SearchResultModels.BusinessListSearchResultItem;
import Utils.StrUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;

// "All results" page
public class BusinessResultsStrategyTypeOne extends BusinessResultItemsProcess {

    @Override
    public List<BusinessListSearchResultItem> processBody(Element body) {

        ArrayList<BusinessListSearchResultItem> results = new ArrayList<>();
        Elements items = body.select("#ires");

        Elements container = items.select("h2:contains(Local Results)");
        if (container.size() == 0) {
            container = items.select("a[href^='https://maps.google.com/maps?']");
        }
        Logger.debug(items);

        if (container.size() > 0) {
            Element listContainer = container.first().parent();
            Elements namesElements = listContainer.select("div[aria-level=3][role=heading]");
            Elements addressesElements = findBussinessGeoDataByNames(namesElements);

            for (int i = 0; i < addressesElements.size(); i++) {
                String mainHeader = namesElements.get(i).text();
                String city;
                if (addressesElements.size() == i || addressesElements.get(i) == null) {
                    city = "";
                } else {
                    city = StrUtils.getCityFromAddress(addressesElements.get(i).text());
                }
                String country = StrUtils.getCountryFromAddress(addressesElements.get(i).text());
                String description = "Item scraped from business list";
                BusinessListSearchResultItem regularSearchResultItem = new BusinessListSearchResultItem(mainHeader, "", description, city, country);
                results.add(regularSearchResultItem);
            }
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
