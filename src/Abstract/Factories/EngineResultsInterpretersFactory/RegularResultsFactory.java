package Abstract.Factories.EngineResultsInterpretersFactory;

import Abstract.Strategies.EngineResultsInterpreters.RegularResultsProcessing.IRegularSearchItemsProcess;
import Abstract.Strategies.EngineResultsInterpreters.RegularResultsProcessing.RegularResultsStrategyTypeOne;
import Abstract.Strategies.EngineResultsInterpreters.RegularResultsProcessing.RegularResultsStrategyTypeTwo;
import Abstract.Models.SearchResultModels.RegularSearchResultItem;
import org.jsoup.nodes.Element;
import java.util.ArrayList;
import java.util.List;

public class RegularResultsFactory implements ISearchResultFactory {
    @Override
    public List<RegularSearchResultItem> processBody(Element body) {

        IRegularSearchItemsProcess iRegularSearchItemsProcess;
        if (body.getElementsContainingText("All results").size() > 0) {
            iRegularSearchItemsProcess = new RegularResultsStrategyTypeOne(); // "All results" page
        } else {
            iRegularSearchItemsProcess = new RegularResultsStrategyTypeTwo();  //Regular simple page
        }
        return new ArrayList<>(iRegularSearchItemsProcess.processBody(body));
    }
}
