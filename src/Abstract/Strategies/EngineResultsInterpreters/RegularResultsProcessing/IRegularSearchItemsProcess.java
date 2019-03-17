package Abstract.Strategies.EngineResultsInterpreters.RegularResultsProcessing;

import Abstract.Models.SearchResultModels.RegularSearchResultItem;
import org.jsoup.nodes.Element;

import java.util.List;

public interface IRegularSearchItemsProcess {
    List<RegularSearchResultItem> processBody(Element body);
}
