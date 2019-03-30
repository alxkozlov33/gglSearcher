package Abstract.Strategies.EngineResultsInterpreters.BusinessListResultsProcessing;

import Abstract.Engines.ProxyWebEngine;
import Abstract.Models.SearchResultModels.BusinessListSearchResultItem;
import java.util.List;

// "All results" page
public class BusinessResultsStrategyTypeOne extends BusinessResultItemsProcess {

    @Override
    public List<BusinessListSearchResultItem> processBody(ProxyWebEngine proxyWebEngine) {
        return requestToGoogleMaps(proxyWebEngine);
    }
}
