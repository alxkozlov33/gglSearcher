package Abstract.Factories.Concrete;

import Abstract.Factories.Concrete.BusinessListResultsProcessing.BussinesResultsStrategyTypeOne;
import Abstract.Factories.Concrete.BusinessListResultsProcessing.BussinesResultsStrategyTypeTwo;
import Abstract.Factories.Concrete.BusinessListResultsProcessing.IBusinessResultItemsProcess;
import Abstract.Factories.ISearchResultFactory;
import Abstract.Models.SearchResultModels.ListSearchResultItem;
import org.jsoup.nodes.Element;
import java.util.ArrayList;

public class BusinessListResultsFactory implements ISearchResultFactory {

    @Override
    public ArrayList<ListSearchResultItem> processBody(Element body) {
        IBusinessResultItemsProcess iBusinessResultItemsProcess;
        if (body.getElementsContainingText("All results").size() > 0) {
            iBusinessResultItemsProcess = new BussinesResultsStrategyTypeOne(); // "All results" page
        } else {
            iBusinessResultItemsProcess = new BussinesResultsStrategyTypeTwo();  //Regular simple page
        }
        return new ArrayList<>(iBusinessResultItemsProcess.processBody(body));
    }
}
