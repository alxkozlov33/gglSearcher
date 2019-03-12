package Abstract.Factories.Concrete.BusinessListResultsProcessing;

import Abstract.Models.SearchResultModels.ListSearchResultItem;
import org.jsoup.nodes.Element;
import java.util.List;

public interface IBusinessResultItemsProcess {
    List<ListSearchResultItem> processBody(Element body);
}
