package Abstract.Factories.Concrete;

import Abstract.Factories.Concrete.RegularResultsProcessing.IRegularSearchItemsProcess;
import Abstract.Factories.Concrete.RegularResultsProcessing.RegularResultsStrategyTypeOne;
import Abstract.Factories.Concrete.RegularResultsProcessing.RegularResultsStrategyTypeTwo;
import Abstract.Factories.ISearchResultFactory;
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
