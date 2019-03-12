package Abstract.Factories.Concrete.BusinessListResultsProcessing;

import Abstract.Models.SearchResultModels.ListSearchResultItem;
import Utils.StrUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;

//Regular simple page
public class BussinesResultsStrategyTypeTwo implements IBusinessResultItemsProcess {
    @Override
    public List<ListSearchResultItem> processBody(Element body) {
        ArrayList<ListSearchResultItem> results = new ArrayList<>();

        Elements items = body.select("#rso");
        Elements resultDivs = items.select("h2:contains(Local Results)");
        Elements elements = new Elements();

        for (Element resultDiv: resultDivs) {
            elements.addAll(resultDiv.parent().select("a[href][tabindex=0]"));
        }

        //TODO:
        for (Element div : elements) {
            String mainHeader = div.select("h3.r > a").text();
            String link = div.select("h3.r > a").attr("href").replaceFirst("/*$", "");
            String description = div.select("div.s").select("span.st").text();

            ListSearchResultItem regularSearchResultItem = new ListSearchResultItem(mainHeader, StrUtils.normalizeGoogleLink(link), description);
            results.add(regularSearchResultItem);
        }
        return results;
    }
}
