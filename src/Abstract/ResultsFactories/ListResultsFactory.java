package Abstract.ResultsFactories;

import Abstract.SearchResultModels.RegularSearchResult;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class ListResultsFactory implements IResultSearchFactory {
    @Override
    public List<RegularSearchResult> processBody(Element body) {
        ArrayList<RegularSearchResult> results = new ArrayList<>();
        Elements items = body.select("#res");

        if (items != null) {
            Elements resultDivs = items.select("div.g");
            //logService.LogMessage("Parsed: " + resultDivs.size() + " links");
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
            //logService.LogMessage(Results.size() + " results will be saved.");
            //logService.drawLine();
        }
        return results;
    }
}
