package Abstract.Factories.Concrete;

import Abstract.Factories.ISearchResultFactory;
import Abstract.SearchResultModels.RegularSearchResult;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pmw.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;

public class RegularResultsFactory implements ISearchResultFactory {
    @Override
    public List<RegularSearchResult> processBody(Element body) {
        ArrayList<RegularSearchResult> results = new ArrayList<>();
        Elements items = body.select("#res");

        if (items != null) {
            Elements resultDivs = items.select("div.g");
            Logger.info("Parsed: " + resultDivs.size() + " links");
            if (resultDivs.size() == 0) {
                //logService.LogMessage("Empty");
            }

            for (Element div : resultDivs) {
                RegularSearchResult regularSearchResult = new RegularSearchResult();
                String mainHeader = div.select("h3").text();
                String link = div.select("div.r > a").attr("href");
                if (StringUtils.isEmpty(link)) {
                    link = div.select("h3.r > a").attr("href");
                }
                String description = div.select("div.s").text();

                regularSearchResult.link = link;
                regularSearchResult.description = description;
                regularSearchResult.mainHeader = mainHeader;

                results.add(regularSearchResult);
            }
            Logger.info(results.size() + " results will be saved.");
            Logger.info("________________________________________");
        }
        return results;
    }
}
