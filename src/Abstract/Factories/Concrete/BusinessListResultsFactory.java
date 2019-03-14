package Abstract.Factories.Concrete;

import Abstract.Factories.Concrete.BusinessListResultsProcessing.BusinessResultsStrategyTypeOne;
import Abstract.Factories.Concrete.BusinessListResultsProcessing.BusinessResultsStrategyTypeTwo;
import Abstract.Factories.Concrete.BusinessListResultsProcessing.IBusinessResultItemsProcess;
import Abstract.Factories.ISearchResultFactory;
import Abstract.Models.SearchResultModels.BusinessListSearchResultItem;
import org.jsoup.nodes.Element;
import java.util.List;

public class BusinessListResultsFactory implements ISearchResultFactory {

    @Override
    public List<BusinessListSearchResultItem> processBody(Element body) {
        IBusinessResultItemsProcess iBusinessResultItemsProcess;
        if (body.getElementsContainingText("All results").size() > 0) {
            iBusinessResultItemsProcess = new BusinessResultsStrategyTypeOne(); // "All results" page
        } else {
            iBusinessResultItemsProcess = new BusinessResultsStrategyTypeTwo();  //Regular simple page
        }
        return iBusinessResultItemsProcess.processBody(body);
    }
}
