package Abstract.Factories.EngineResultsInterpretersFactory;

import Abstract.Strategies.EngineResultsInterpreters.ISearchRequest;
import Abstract.Strategies.EngineResultsInterpreters.RegularResultsProcessing.ResultsStrategyTypeOne;
import Abstract.Strategies.EngineResultsInterpreters.RegularResultsProcessing.ResultsStrategyTypeTwo;
import Abstract.Models.SearchResultModels.RegularSearchResultItem;
import org.jsoup.nodes.Element;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RegularResultsFactory implements ISearchResultFactory {

    @Override
    public synchronized List<RegularSearchResultItem> getRegularSearchStrategy(Element body) {
        ISearchRequest iSearchRequest = null;

        if (body.select("#ires") != null) {
            iSearchRequest = new ResultsStrategyTypeOne();
        } else if (body.select("#rso") != null) {
            iSearchRequest = new ResultsStrategyTypeTwo();
        }
        return new ArrayList<>(Objects.requireNonNull(iSearchRequest).processBody(body));
    }
}
