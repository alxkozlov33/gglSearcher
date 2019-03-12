package Abstract.Factories;

import Abstract.Models.SearchResultModels.GoogleSearchResultItem;
import org.jsoup.nodes.Element;

import java.util.List;

public interface ISearchResultFactory {

    List<? extends GoogleSearchResultItem> processBody(Element body);
}
