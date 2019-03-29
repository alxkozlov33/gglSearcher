package Abstract.Factories.EngineResultsInterpretersFactory;

import Abstract.Engines.ProxyWebEngine;
import Abstract.Strategies.EngineResultsInterpreters.BusinessListResultsProcessing.BusinessResultsStrategyTypeOne;
import Abstract.Strategies.EngineResultsInterpreters.BusinessListResultsProcessing.BusinessResultsStrategyTypeTwo;
import Abstract.Strategies.EngineResultsInterpreters.BusinessListResultsProcessing.BusinessResultItemsProcess;
import Abstract.Models.SearchResultModels.BusinessListSearchResultItem;
import org.jsoup.nodes.Element;

import java.util.List;

public class BusinessListResultsFactory implements ISearchResultFactory {

    @Override
    public List<BusinessListSearchResultItem> processBody(ProxyWebEngine proxyWebEngine) {
        BusinessResultItemsProcess businessResultItemsProcess;
        if (proxyWebEngine.webDriver.getPageSource().contains("All results")) {
            businessResultItemsProcess = new BusinessResultsStrategyTypeOne(); // "All results" page
        } else {
            businessResultItemsProcess = new BusinessResultsStrategyTypeTwo();  //Regular simple page
        }
        return businessResultItemsProcess.processBody(proxyWebEngine);
    }

    @Override
    public List processBody(Element body) {
        return null;
    }
}
