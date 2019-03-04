package Abstract.ResultsFactories;

import Abstract.SearchResultModels.GoogleSearchResultItem;
import org.jsoup.nodes.Element;

import java.util.List;

public interface IResultSearchFactory {

    public List<? extends GoogleSearchResultItem> processBody(Element body);
}
