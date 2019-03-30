package Abstract.Strategies.EngineResultsInterpreters.BusinessListResultsProcessing;

import Abstract.Engines.ProxyWebEngine;
import Abstract.Models.SearchResultModels.BusinessListSearchResultItem;
import java.util.List;

//Regular simple page
public class BusinessResultsStrategyTypeTwo extends BusinessResultItemsProcess {

    @Override
    public List<BusinessListSearchResultItem> processBody(ProxyWebEngine proxyWebEngine) {
        return requestToGoogleMaps(proxyWebEngine);
    }
}
