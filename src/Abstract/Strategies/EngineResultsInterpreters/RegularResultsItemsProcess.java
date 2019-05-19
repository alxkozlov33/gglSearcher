package Abstract.Strategies.EngineResultsInterpreters;

import Abstract.Models.SearchResultModels.RegularSearchResultItem;
import Utils.StrUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.tinylog.Logger;
import java.util.ArrayList;
import java.util.List;

public class RegularResultsItemsProcess {

    public List<RegularSearchResultItem> translateBodyToModels(Element body) {
        ArrayList<RegularSearchResultItem> results = new ArrayList<>();
        if(body == null) {
            return results;
        }
        Elements elItems = body.select("#ires");
        Element items = null;
        if (elItems != null) {
            items = elItems.first();
        }

        if (items == null) {
            return results;
        }
        Elements resultDivs = items.select("div[class=g]:not(:contains(Images for))");
        Logger.tag("SYSTEM").info("Parsed: " + resultDivs.size() + " links");
        for (Element div : resultDivs) {
            String mainHeader = div.select("h3.r > a").text();
            if (StringUtils.isEmpty(mainHeader)) {
                mainHeader = div.select("h3").text();
            }
            String link = div.select("h3.r > a").attr("href").replaceFirst("/*$", "");
            if (StringUtils.isEmpty(link)) {
                link = div.select("h3").attr("href").replaceFirst("/*$", "");
            }
            if (StringUtils.isEmpty(link)) {
                link = div.select("div.r > a").attr("href").replaceFirst("/*$", "");
            }

            String description = div.select("div.s").select("span.st").text();

            RegularSearchResultItem regularSearchResultItem = new RegularSearchResultItem(mainHeader, StrUtils.extractWebSiteFromURL(link), description);
            results.add(regularSearchResultItem);
        }
        return results;
    }
}
