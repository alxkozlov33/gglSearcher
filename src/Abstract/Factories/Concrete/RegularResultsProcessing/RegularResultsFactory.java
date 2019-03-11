package Abstract.Factories.Concrete.RegularResultsProcessing;

import Abstract.Factories.ISearchResultFactory;
import Abstract.SearchResultModels.RegularSearchResultItem;
import org.jsoup.nodes.Element;
import java.util.ArrayList;
import java.util.List;

public class RegularResultsFactory implements ISearchResultFactory {
    @Override
    public List<RegularSearchResultItem> processBody(Element body) {
        ArrayList<RegularSearchResultItem> results = new ArrayList<>();

        IRegularSearchItemsProcess iRegularSearchItemsProcess;
        if (body.getElementsContainingText("All results").size() > 0) {
            iRegularSearchItemsProcess = new RegularResultsStrategyTypeOne(); // "All results" page
        } else {
            iRegularSearchItemsProcess = new RegularResultsStrategyTypeTwo();  //Regular simple page
        }
        results.addAll(iRegularSearchItemsProcess.processBody(body));
        return results;
    }
}
