package Abstract.Strategies.EngineResultsInterpreters.RegularResultsProcessing;

import Abstract.Models.SearchResultModels.RegularSearchResultItem;
import Utils.StrUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.tinylog.Logger;
import java.util.ArrayList;
import java.util.List;

public class RegularResultsItemsProcess {

    public List<RegularSearchResultItem> translateBodyToModels(Element body) {
        ArrayList<RegularSearchResultItem> results = new ArrayList<>();
        Elements items = body.select("#ires");
        if (items == null) {
            return results;
        }
        Elements resultDivs = items.select("div[class=g]:not(:contains(Images for))");
        Logger.tag("SYSTEM").info("Parsed: " + resultDivs.size() + " links");
        for (Element div : resultDivs) {
            String mainHeader = div.select("h3.r > a").text();
            String link = div.select("h3.r > a").attr("href").replaceFirst("/*$", "");
            String description = div.select("div.s").select("span.st").text();

            RegularSearchResultItem regularSearchResultItem = new RegularSearchResultItem(mainHeader, StrUtils.normalizeGoogleLink(link), description);
            results.add(regularSearchResultItem);
        }
        return results;
    }
}
