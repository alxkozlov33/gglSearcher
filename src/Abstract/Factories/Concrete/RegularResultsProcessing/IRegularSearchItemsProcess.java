package Abstract.Factories.Concrete.RegularResultsProcessing;

import Abstract.SearchResultModels.RegularSearchResultItem;
import org.jsoup.nodes.Element;

import java.util.List;

public interface IRegularSearchItemsProcess {
    List<RegularSearchResultItem> processBody(Element body);
}
