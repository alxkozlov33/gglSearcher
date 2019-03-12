package Abstract.Factories.Concrete.BusinessListResultsProcessing;

import Abstract.Engines.WebUrlEngine;
import Abstract.Factories.Concrete.BusinessListResultsProcessing.GoogleMapsResultsProcessing.GoogleMapsResultsProcessor;
import Abstract.Models.RequestData;
import Abstract.Models.SearchResultModels.ListSearchResultItem;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;

// "All results" page
public class BussinesResultsStrategyTypeOne implements IBusinessResultItemsProcess {

    @Override
    public List<ListSearchResultItem> processBody(Element body) {

        ArrayList<ListSearchResultItem> results = new ArrayList<>();
        Elements items = body.select("#ires");
        String linkToMapsList = items.select("a[href^='https://maps.google.com/maps?']").attr("href");

        WebUrlEngine webUrlEngine = new WebUrlEngine();
        Element mapsBody = webUrlEngine.getWebSourceData(new RequestData(linkToMapsList));

        GoogleMapsResultsProcessor googleMapsResultsProcessor = new GoogleMapsResultsProcessor();
        List bussinessList = googleMapsResultsProcessor.processBody(mapsBody);


        return results;
    }
}
