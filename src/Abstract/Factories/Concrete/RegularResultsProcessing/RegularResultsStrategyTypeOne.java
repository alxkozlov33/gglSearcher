package Abstract.Factories.Concrete.RegularResultsProcessing;

import Abstract.Models.SearchResultModels.RegularSearchResultItem;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.tinylog.Logger;
import java.util.ArrayList;
import java.util.List;

// "All results" page
public class RegularResultsStrategyTypeOne implements IRegularSearchItemsProcess {
    @Override
    public List<RegularSearchResultItem> processBody(Element body) {
        ArrayList<RegularSearchResultItem> results = new ArrayList<>();
        Elements items = body.select("#ires");
        Elements resultDivs = items.select("div[class=g]:not(:contains(Images for))");
        Logger.tag("SYSTEM").info("Parsed: " + resultDivs.size() + " links");
        for (Element div : resultDivs) {
            String mainHeader = div.select("h3.r > a").text();
            String link = div.select("h3.r > a").attr("href").replaceFirst("/*$", "");
            String description = div.select("div.s").select("span.st").text();

            RegularSearchResultItem regularSearchResultItem = new RegularSearchResultItem(mainHeader, normalizeLink(link), description);
            results.add(regularSearchResultItem);
        }
        return results;
    }

    private String normalizeLink(String link) {
        if (link.startsWith("http://") || link.startsWith("https://")) {
            return link;
        } else {
            return "http://www.google.com"+link;
        }
    }
}
