package Abstract.Factories.Concrete.BusinessListResultsProcessing;

import Abstract.Models.SearchResultModels.BusinessListSearchResultItem;
import org.jsoup.nodes.Element;
import java.util.List;

public interface IBusinessResultItemsProcess {
    List<BusinessListSearchResultItem> processBody(Element body);
}
