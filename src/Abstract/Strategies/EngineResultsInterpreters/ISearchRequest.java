package Abstract.Strategies.EngineResultsInterpreters;

import Abstract.Models.SearchResultModels.RegularSearchResultItem;
import org.jsoup.nodes.Element;

import java.util.List;

public interface ISearchRequest {
    List<RegularSearchResultItem> processBody(Element body);
}
