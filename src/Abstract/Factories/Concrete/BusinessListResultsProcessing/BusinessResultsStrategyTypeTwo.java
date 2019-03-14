package Abstract.Factories.Concrete.BusinessListResultsProcessing;

import Abstract.Models.SearchResultModels.BusinessListSearchResultItem;
import Utils.StrUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;

//Regular simple page
public class BusinessResultsStrategyTypeTwo extends BusinessResultItemsProcess {
    @Override
    public List<BusinessListSearchResultItem> processBody(Element body) {
        ArrayList<BusinessListSearchResultItem> results = new ArrayList<>();
        Elements items = body.select("#rso");

        Elements container = items.select("h2:contains(Local Results)");
        if (container.size() == 0) {
            container = items.select("a[href^='https://maps.google.com/maps?']");
        }
        Logger.debug(items);

        if (container.size() > 0) {
            Element listContainer = container.first().parent();
            Elements namesElements = listContainer.select("div[aria-level=3][role=heading]");
            Elements addressesElements = findBussinessGeoDataByNames(namesElements);
            for (int i = 0; i < namesElements.size(); i++) {
                String mainHeader = namesElements.get(i).text();
                String city;
                if (addressesElements.get(i) == null) {
                    city = "";
                } else {
                    city = StrUtils.getCityFromAddress(addressesElements.get(i).text());
                }
                String country = StrUtils.getCountryFromAddress(addressesElements.get(i).text());
                if (country.equalsIgnoreCase("") || city.equalsIgnoreCase("")) {
                    Logger.debug("Problem");
                }
                String description = "Item scraped from business list";
                BusinessListSearchResultItem regularSearchResultItem = new BusinessListSearchResultItem(mainHeader, "", description, city, country);
                results.add(regularSearchResultItem);
            }
        }
        return results;
    }
}
