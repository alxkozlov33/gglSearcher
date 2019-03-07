package Abstract.Factories.Concrete;

import Abstract.Factories.ISearchResultFactory;
import Abstract.SearchResultModels.RegularSearchResultItem;
import org.jsoup.nodes.Element;

import java.util.List;

public class BusinessListResultsFactory implements ISearchResultFactory {
    @Override
    public List<RegularSearchResultItem> processBody(Element body) {
       return null;
    }
}
