package Abstract.Factories.Concrete.RegularResultsProcessing;

import Abstract.SearchResultModels.RegularSearchResultItem;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.tinylog.Logger;
import java.util.ArrayList;
import java.util.List;

//Regular simple page
public class RegularResultsStrategyTypeTwo implements IRegularSearchItemsProcess {
    @Override
    public List<RegularSearchResultItem> processBody(Element body) {
        ArrayList<RegularSearchResultItem> results = new ArrayList<>();
        Elements items = body.select("#rso");
        Elements resultDivs = items.select("h2:contains(Web results)");
        Elements elements = new Elements();

        for (Element resultDiv: resultDivs) {
            elements.addAll(resultDiv.parent().select("div.g"));
        }

        Logger.tag("SYSTEM").info("Parsed: " + elements.size() + " links");
        for (Element div : elements) {
            String mainHeader = div.select("div.r").select("h3").text();
            String link = div.select("div.r > a").attr("href").replaceFirst("/*$", "");
            String description = div.select("div.s").select("span.st").text();

            RegularSearchResultItem regularSearchResultItem = new RegularSearchResultItem(mainHeader, link, description);
            results.add(regularSearchResultItem);
        }
        return results;
    }
}
