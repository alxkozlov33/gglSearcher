package Abstract.Factories.EngineResultsInterpretersFactory;

import Abstract.Strategies.EngineResultsInterpreters.ISearchRequest;
import Abstract.Strategies.EngineResultsInterpreters.RegularResultsProcessing.ResultsStrategyTypeOne;
import Abstract.Strategies.EngineResultsInterpreters.RegularResultsProcessing.ResultsStrategyTypeTwo;
import Abstract.Models.SearchResultModels.RegularSearchResultItem;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RegularResultsFactory implements ISearchResultFactory {

    @Override
    public List<RegularSearchResultItem> getRegularSearchStrategy(Element body) {
        ISearchRequest iSearchRequest = null;
        Elements items;

        items = body.select("#ires");
        if (items != null) {
            iSearchRequest = new ResultsStrategyTypeOne();
        }
        items = body.select("#rso");
        if (items != null) {
            iSearchRequest = new ResultsStrategyTypeTwo();
        }
        return new ArrayList<>(Objects.requireNonNull(iSearchRequest).processBody(body));
    }
}
