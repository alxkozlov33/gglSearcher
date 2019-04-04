package Abstract.Factories.EngineResultsInterpretersFactory;

import Abstract.Engines.ProxyWebEngine;
import Abstract.Strategies.EngineResultsInterpreters.RegularResultsProcessing.IRegularSearchItemsProcess;
import Abstract.Strategies.EngineResultsInterpreters.RegularResultsProcessing.RegularResultsStrategyTypeOne;
import Abstract.Strategies.EngineResultsInterpreters.RegularResultsProcessing.RegularResultsStrategyTypeTwo;
import Abstract.Models.SearchResultModels.RegularSearchResultItem;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RegularResultsFactory implements ISearchResultFactory {
    @Override
    public List processBody(ProxyWebEngine proxyWebEngine) {
        return null;
    }

    @Override
    public List<RegularSearchResultItem> processBody(Element body) {
        IRegularSearchItemsProcess iRegularSearchItemsProcess = null;
        Elements items;

        items = body.select("#ires");
        if (items != null) {
            iRegularSearchItemsProcess = new RegularResultsStrategyTypeOne();
        }
        items = body.select("#rso");
        if (items != null) {
            iRegularSearchItemsProcess = new RegularResultsStrategyTypeTwo();
        }
        return new ArrayList<>(Objects.requireNonNull(iRegularSearchItemsProcess).processBody(body));
    }
}
