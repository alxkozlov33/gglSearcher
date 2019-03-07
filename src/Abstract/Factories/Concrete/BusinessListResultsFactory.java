package Abstract.Factories.Concrete;

import Abstract.Factories.ISearchResultFactory;
import Abstract.SearchResultModels.RegularSearchResult;
import org.jsoup.nodes.Element;

import java.util.List;

public class BusinessListResultsFactory implements ISearchResultFactory {
    @Override
    public List<RegularSearchResult> processBody(Element body) {
       return null;
    }
}
